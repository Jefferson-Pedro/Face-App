package com.face_app.project.controller;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.RecognitionDTO;
import com.face_app.project.model.Users;
import com.face_app.project.service.face.IFaceRecognition;
import com.face_app.project.service.face.IFaceService;
import com.face_app.project.service.usuario.IUsuarioService;
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

    @Autowired
    private IUsuarioService serviceUser;

    @PostMapping("/face/register/{userId}")
    public ResponseEntity<?> registerFace(@PathVariable Integer userId, @RequestBody FaceDTO faceDTO){

        boolean res = serviceUser.registerUserFace(userId, faceDTO);
        if(res){
            return ResponseEntity.ok().body("Face cadastrada com sucesso! Você já pode fazer login.");
        }
        return ResponseEntity.noContent().build();
    }

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
