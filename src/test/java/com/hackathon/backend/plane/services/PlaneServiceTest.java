package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.services.plane.PlaneService;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneSeatsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaneServiceTest {

    @Mock
    PlaneUtils planeUtils;

    @Mock
    PlaneSeatsUtils planeSeatsUtils;

    @Mock
    PlaneFlightsUtils planeFlightsUtils;

    @InjectMocks
    PlaneService planeService;

    @Test
    void createPlane() {
        //given
        PlaneDto planeDto = new PlaneDto();
        planeDto.setPlaneCompanyName("testPlane");
        planeDto.setNumSeats(100);

        //when
        ResponseEntity<?> response = planeService.createPlane(planeDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editPlane() {
        //given
        long planeId = 1;

        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);
        plane.setPlaneCompanyName("testPlane");
        plane.setNumSeats(100);

        PlaneDto planeDto = new PlaneDto();
        planeDto.setPlaneCompanyName("testPlane1");
        planeDto.setNumSeats(101);

        //behavior
        when(planeUtils.findPlaneById(planeId)).thenReturn(plane);

        //when
        ResponseEntity<?> response = planeService.editPlane(planeId, planeDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planeDto.getPlaneCompanyName(), plane.getPlaneCompanyName());
        assertEquals(planeDto.getNumSeats(), plane.getNumSeats());
        verify(planeUtils).findPlaneById(planeId);
        verify(planeUtils).save(plane);
    }

    @Test
    void deletePlane() {
        //given
        long planeId = 1L;
        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);

        PlaneSeatsEntity planeSeatsEntity = new PlaneSeatsEntity();
        plane.getPlaneSeats().add(planeSeatsEntity);

        PlaneFlightsEntity planeFlightsEntity = new PlaneFlightsEntity();
        plane.setFlight(planeFlightsEntity);

        //behavior
        when(planeUtils.findById(planeId)).thenReturn(plane);

        //when
        ResponseEntity<?> response = planeService.deletePlane(planeId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeUtils).findById(planeId);
        verify(planeSeatsUtils).delete(planeSeatsEntity);
        verify(planeFlightsUtils).delete(planeFlightsEntity);
        verify(planeUtils).delete(plane);
    }
}