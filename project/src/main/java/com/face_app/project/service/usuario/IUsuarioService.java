package com.face_app.project.service.usuario;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.UserRegistrationRequest;
import com.face_app.project.dto.UserResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface IUsuarioService {

    public boolean registerUser(@Valid UserRegistrationRequest usuarioRequest);

    public boolean registerUserFace(UUID userId, FaceDTO faceDTO);

    public boolean update (UserRegistrationRequest usuarioRequest);

    public UserResponse findById(Integer id);
}
