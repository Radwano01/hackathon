package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.EditPlaneSeatDto;
import com.hackathon.backend.dto.planeDto.PlaneSeatDto;
import com.hackathon.backend.dto.planeDto.ValidSeatDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.utilities.plane.PlaneSeatsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneSeatsService {

    private final PlaneSeatsUtils planeSeatsUtils;
    private final PlaneUtils planeUtils;

    @Autowired
    public PlaneSeatsService(PlaneSeatsUtils planeSeatsUtils,
                             PlaneUtils planeUtils) {
        this.planeSeatsUtils = planeSeatsUtils;
        this.planeUtils = planeUtils;
    }

    @Transactional
    public ResponseEntity<String> addSeat(long planeId,
                                     @NonNull PlaneSeatDto planeSeatDto) {
        try{
            PlaneEntity plane = planeUtils.findPlaneById(planeId);
            if(plane.getNumSeats() < plane.getPlaneSeats().size()){
                return badRequestException("Plane visas jumped above 100");
            }
            PlaneSeatsEntity planeSeats = new PlaneSeatsEntity(
                    planeSeatDto.getSeatClass(),
                    planeSeatDto.getSeatNumber(),
                    plane
            );
            planeSeatsUtils.save(planeSeats);
            plane.getPlaneSeats().add(planeSeats);
            planeUtils.save(plane);
            return ResponseEntity.ok("Plane seat created successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getValidSeats(long planeId) {
        try{
            PlaneEntity planeEntities = planeUtils.findById(planeId);
            List<ValidSeatDto> dto = new ArrayList<>();
            for(PlaneSeatsEntity planeSeats:planeEntities.getPlaneSeats()){
                if(planeSeats.isStatus()){
                    ValidSeatDto validSeatDto = new ValidSeatDto(
                            planeSeats.getId(),
                            planeSeats.getSeatClass(),
                            planeSeats.getSeatNumber()
                    );
                    dto.add(validSeatDto);
                }else{
                    return badRequestException("No seats found on this plane");
                }
            }
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editSeat(long seatId,
                                      EditPlaneSeatDto editPlaneSeatDto){
        try{
            if(!planeSeatsUtils.checkHelper(editPlaneSeatDto)){
                return badRequestException("you sent an empty data to change");
            }
            PlaneSeatsEntity planeSeats = planeSeatsUtils.findById(seatId);
            planeSeatsUtils.editHelper(planeSeats, editPlaneSeatDto);
            planeSeatsUtils.save(planeSeats);
            return ResponseEntity.ok("Plane seat updated Successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteSeat(long planeId,
                                        long seatId) {
        try{
            PlaneEntity plane = planeUtils.findPlaneById(planeId);
            plane.getPlaneSeats().removeIf((seats)-> seats.getId() == seatId);

            planeSeatsUtils.deleteById(seatId);
            planeUtils.save(plane);
            return ResponseEntity.ok("Plane seat deleted successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
