package com.face_app.project.controller;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.UserRegistrationRequest;
import com.face_app.project.service.usuario.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private IUsuarioService service;

    @PostMapping("/user/register")
    public ResponseEntity<String> register (@Valid @RequestBody UserRegistrationRequest usuarioRequest){
        try{
            Boolean res = service.registerUser(usuarioRequest);
            if(res){
                return ResponseEntity.status(201).body("Usuario " + usuarioRequest.nome() + " cadastrado com sucesso!");
            }
            return  ResponseEntity.status(409).body("Usuário já cadastrado na base de dados!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRO: Dados incompletos! " + e);
        }
    }

    @PostMapping("/{userId}/face")
    public ResponseEntity<?> registerFace(UUID userId, @RequestBody FaceDTO faceDTO){

        boolean res = service.registerUserFace(userId, faceDTO);
        if(res){
            return ResponseEntity.ok().body("Face cadastrada com sucesso! Você já pode fazer login.");
        }
        return ResponseEntity.noContent().build();
    }

}
