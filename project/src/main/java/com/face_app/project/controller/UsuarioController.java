package com.face_app.project.controller;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.UserRegistrationRequest;
import com.face_app.project.dto.UserResponse;
import com.face_app.project.service.usuario.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private IUsuarioService service;

    @PostMapping("/user/new")
    public ResponseEntity<?> register (@Valid @RequestBody UserRegistrationRequest usuarioRequest){
        try{
            UserResponse user = service.registerUser(usuarioRequest);
            if(user != null){
                return ResponseEntity.status(201).body(Map.of(
                        "mensagem", "Usuário " + user.nome()+ " cadastrado com sucesso!",
                        "userId", user.id()
                ));
            }
            return  ResponseEntity.status(409).body("Usuário já cadastrado na base de dados!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRO: Dados incompletos! " + e);
        }
    }
}
