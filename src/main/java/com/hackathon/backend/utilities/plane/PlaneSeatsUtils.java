package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.dto.planeDto.EditPlaneSeatDto;
import com.hackathon.backend.dto.planeDto.PlaneSeatDto;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlaneSeatsUtils {

    private final PlaneSeatsRepository planeSeatsRepository;

    @Autowired
    public PlaneSeatsUtils(PlaneSeatsRepository planeSeatsRepository) {
        this.planeSeatsRepository = planeSeatsRepository;
    }

    public List<PlaneSeatsEntity> findAllSeatsByPlaneId(long planeId) {
        return planeSeatsRepository.findAllSeatsByPlaneId(planeId)
                .orElseThrow(()-> new EntityNotFoundException("Plane id not found"));
    }

    public void deleteById(long planeId) {
        planeSeatsRepository.deleteById(planeId);
    }

    public void save(PlaneSeatsEntity planeSeats) {
        planeSeatsRepository.save(planeSeats);
    }

    public PlaneSeatsEntity findById(long seatId) {
        return planeSeatsRepository.findById(seatId)
                .orElseThrow(()-> new EntityNotFoundException("plane seat id Not Found"));
    }

    public void delete(PlaneSeatsEntity planeSeats) {
        planeSeatsRepository.delete(planeSeats);
    }

    public boolean checkHelper(EditPlaneSeatDto editPlaneSeatDto){
        return  editPlaneSeatDto.getSeatClass() != null ||
                editPlaneSeatDto.getSeatNumber() != null;
    }

    public void editHelper(PlaneSeatsEntity planeSeats,
                            EditPlaneSeatDto editPlaneSeatDto) {
        if(planeSeats.getPlane().getNumSeats() > editPlaneSeatDto.getSeatNumber()){
            planeSeats.setSeatNumber(editPlaneSeatDto.getSeatNumber());
        }
        switch (editPlaneSeatDto.getSeatClass()){
            case 'A':
            case 'B':
            case 'C':
            case 'D':
                planeSeats.setSeatClass(editPlaneSeatDto.getSeatClass());
                break;
            default:
                break;
        }
    }
}
