package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaneSeatsRepositoryTest {

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    PlaneSeatsRepository planeSeatsRepository;

    @BeforeEach
    void setUp(){
        PlaneEntity plane = new PlaneEntity(
                "testName",
                100
        );
        plane.setId(1);
        planeRepository.save(plane);

        PlaneSeatsEntity planeSeats = new PlaneSeatsEntity(
                'A',
                1,
                plane

        );
        planeSeatsRepository.save(planeSeats);
    }

    @AfterEach
    void tearDown(){
        planeSeatsRepository.deleteAll();
        planeRepository.deleteAll();
    }

    @Test
    void findAllSeatsByPlaneId() {
        //given
        long planeId = 1;
        //when
        Optional<List<PlaneSeatsEntity>> response = planeSeatsRepository.findAllSeatsByPlaneId(planeId);

        //then
        assertTrue(response.isPresent());
        assertEquals(response.get().get(0).getSeatClass(), 'A');
        assertEquals(response.get().get(0).getSeatNumber(), 1);
        assertEquals(response.get().get(0).getPlane().getPlaneCompanyName(), "testName");
    }
}