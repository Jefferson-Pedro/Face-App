package com.face_app.project.controller;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.service.facerecognition.IFaceRecognition;
import com.face_app.project.util.FaceMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class FaceController {

    @Autowired
    private IFaceRecognition service;

    @PostMapping("/face/register")
    public ResponseEntity<?> registerFace(@RequestBody FaceDTO faceDTO){

        String filename = service.toImage(faceDTO.getData());
        if(service.cropFace(filename, "-crop.png", FaceMode.REGISTER)){
            service.performTraining();
            System.out.println("Gerando arquivo: " + filename);
            return ResponseEntity.ok(filename);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/face/training")
    public String performFaceTraining(){
        System.out.println("Performing Face Training...");
        return service.performTraining();
    }

    @PostMapping("/face/recognize")
    public String recognizeFace(@RequestBody FaceDTO faceDTO){
        String filename = service.toImage(faceDTO.getData());
        if(service.cropFace(filename, "-recog.png", FaceMode.RECOGNITION)){
            service.performRecognition(faceDTO);
        }
        return "OK";
    }
}
