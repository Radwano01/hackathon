package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.PostPlaceDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PlaceService{

    private final CountryUtils countryUtils;
    private final PlaceUtils placeUtils;
    private final PlaceDetailsUtils placeDetailsUtils;
    private final S3Service s3Service;

    @Autowired
    public PlaceService(CountryUtils countryUtils,
                        PlaceUtils placeUtils,
                        PlaceDetailsUtils placeDetailsUtils,
                        S3Service s3Service) {
        this.countryUtils = countryUtils;
        this.placeUtils = placeUtils;
        this.placeDetailsUtils = placeDetailsUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> createPlace(int countryId,
                                         @NonNull PostPlaceDto postPlaceDto) {
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);

            String placeImageName = s3Service.uploadFile(postPlaceDto.getMainImage());

            PlaceEntity place = new PlaceEntity(
                    postPlaceDto.getPlace(),
                    placeImageName,
                    country
            );

            placeUtils.save(place);
            country.getPlaces().add(place);
            countryUtils.save(country);

            String placeDetailsImageNameOne = s3Service.uploadFile(postPlaceDto.getImageOne());
            String placeDetailsImageNameTwo = s3Service.uploadFile(postPlaceDto.getImageTwo());
            String placeDetailsImageNameThree = s3Service.uploadFile(postPlaceDto.getImageThree());

            PlaceDetailsEntity placeDetails = new PlaceDetailsEntity(
                    placeDetailsImageNameOne,
                    placeDetailsImageNameTwo,
                    placeDetailsImageNameThree,
                    postPlaceDto.getDescription(),
                    place
            );
            placeDetailsUtils.save(placeDetails);
            place.setPlaceDetails(placeDetails);
            placeUtils.save(place);
            return ResponseEntity.ok("Place created successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getPlacesByCountry(int countryId){
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);
            List<PlaceEntity> placeEntities =  country.getPlaces();
            return ResponseEntity.ok(placeEntities);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPlace(int countryId,
                                       int placeId,
                                       EditPlaceDto editPlaceDto) {
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);
            Optional<PlaceEntity> place = country.getPlaces().stream()
                    .filter((data)-> data.getId() == placeId).findFirst();
            if(place.isPresent()) {
                editHelper(place.get(), editPlaceDto);
                placeUtils.save(place.get());
                countryUtils.save(country);
                return ResponseEntity.ok("Place updated successfully");
            }else{
                return notFoundException("Place not found in country data");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePlace(int countryId,
                                         int placeId) {
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);
            Optional<PlaceEntity> place = country.getPlaces().stream()
                    .filter((data)-> data.getId() == placeId).findFirst();

            if(place.isPresent()) {
                PlaceDetailsEntity placeDetails = place.get().getPlaceDetails();
                String[] ls = new String[]{
                        placeDetails.getImageOne(),
                        placeDetails.getImageTwo(),
                        placeDetails.getImageThree()
                };

                s3Service.deleteFiles(ls);
                placeDetailsUtils.delete(placeDetails);

                s3Service.deleteFile(place.get().getMainImage());
                placeUtils.delete(place.get());
                countryUtils.save(country);
                return ResponseEntity.ok("Place deleted successfully");
            }else{
                return notFoundException("Place not found in country data");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PlaceEntity place,
                            EditPlaceDto editPlaceDto) {
        if(editPlaceDto.getPlace() != null){
            place.setPlace(editPlaceDto.getPlace());
        }
        if(editPlaceDto.getMainImage() != null){
            String placeMainImageName = s3Service.uploadFile(editPlaceDto.getMainImage());
            place.setMainImage(placeMainImageName);
        }
    }
}
