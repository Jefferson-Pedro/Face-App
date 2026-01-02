package com.face_app.project.controller;

import com.face_app.project.dto.UserLoginRequest;
import com.face_app.project.dto.UsuarioRequest;
import com.face_app.project.service.usuario.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private IUsuarioService service;

    @PostMapping("/user/new")
    public ResponseEntity<String> register (@Valid @RequestBody UsuarioRequest usuarioRequest){
        try{
            Boolean res = service.register(usuarioRequest);
            if(res){
                return ResponseEntity.status(201).body("Usuario " + usuarioRequest.nome() + " cadastrado com sucesso!");
            }
            return  ResponseEntity.status(409).body("Usuário já cadastrado na base de dados!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRO: Dados incompletos! " + e);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> register (@Valid @RequestBody UserLoginRequest loginRequest){
        try{
            Boolean res = service.login(loginRequest);
            if(res){
                return ResponseEntity.ok().build();
            }
            return  ResponseEntity.status(401).body("Acesso negado!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRO: Dados incompletos! " + e);
        }
    }
}
