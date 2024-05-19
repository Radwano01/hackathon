package com.hackathon.backend.services.package_.packageFeatures;


import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class RoadmapService{

    private final RoadmapUtils roadmapUtils;
    private final PackageDetailsUtils packageDetailsUtils;

    @Autowired
    public RoadmapService(RoadmapUtils roadmapUtils,
                          PackageDetailsUtils packageDetailsUtils){
        this.roadmapUtils = roadmapUtils;
        this.packageDetailsUtils = packageDetailsUtils;
    }

    public ResponseEntity<?> createRoadmap(String roadmap){
        try{
            if(roadmapUtils.existsByRoadmap(roadmap)) {
                return alreadyValidException("Roadmap already exists");
            }
            RoadmapEntity roadmapEntity = new RoadmapEntity(
                roadmap
            );
            roadmapUtils.save(roadmapEntity);
            return ResponseEntity.ok("Roadmap created successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getRoadmaps(){
        try{
            List<RoadmapEntity> roadmaps = roadmapUtils.findAll();
            return ResponseEntity.ok(roadmaps);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editRoadmap(int roadmapId,String roadmap){
        try{
            RoadmapEntity roadmapEntity = roadmapUtils.findById(roadmapId);
            if(roadmap != null){
                roadmapEntity.setRoadmap(roadmap);
            }
            roadmapUtils.save(roadmapEntity);
            return ResponseEntity.ok("Roadmap edited successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteRoadmap(int roadmapId){
        try{
            RoadmapEntity roadmapEntity = roadmapUtils.findById(roadmapId);
            if(roadmapEntity != null) {
                List<PackageDetailsEntity> packageDetailsEntities = packageDetailsUtils.findAll();
                for(PackageDetailsEntity packageDetails:packageDetailsEntities){
                    if(packageDetails.getRoadmaps() != null) {
                        List<RoadmapEntity> roadmapEntities = packageDetails.getRoadmaps();
                        boolean removeIf = roadmapEntities.removeIf((roadmap) -> roadmap.getId() == roadmapId);
                        if (removeIf) {
                            packageDetailsUtils.save(packageDetails);
                        }
                    }else{
                        return notFoundException("This package has not roadmaps");
                    }
                }
                roadmapUtils.delete(roadmapEntity);
                return ResponseEntity.ok("Roadmap deleted successfully");
            }else{
                return notFoundException("Roadmap not found");
            }
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

}