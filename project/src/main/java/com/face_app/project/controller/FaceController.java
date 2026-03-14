package com.face_app.project.controller;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.RecognitionDTO;
import com.face_app.project.model.Users;
import com.face_app.project.service.face.IFaceRecognition;
import com.face_app.project.service.face.IFaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin("*")
public class FaceController {

    @Autowired
    private IFaceRecognition serviceRecognition;

    @Autowired
    private IFaceService serviceFace;

    /*@PostMapping("/face/register/{userId}")
    public ResponseEntity<?> registerFace(UUID userId, @RequestBody FaceDTO faceDTO){

        Boolean res = serviceFace.registerFace(userId, faceDTO);

            return ResponseEntity.ok().build();
        }
         return ResponseEntity.noContent();
    }*/


    @GetMapping("/face/training")
    public String performFaceTraining(){
        System.out.println("Performing Face Training...");
        return serviceRecognition.performTraining();
    }

    @PostMapping("/face/recognize")
    public ResponseEntity<?> recognizeFace(@RequestBody FaceDTO faceDTO){
        RecognitionDTO confidence = serviceRecognition.performRecognition(faceDTO);
        if(confidence.confidence() > 50.0){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
