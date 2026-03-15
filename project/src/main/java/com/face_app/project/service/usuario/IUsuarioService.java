package com.face_app.project.service.usuario;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.UserRegistrationRequest;
import com.face_app.project.dto.UserResponse;
import com.face_app.project.model.Users;
import jakarta.validation.Valid;

import java.util.UUID;

public interface IUsuarioService {

    public UserResponse registerUser(@Valid UserRegistrationRequest usuarioRequest);

    public boolean registerUserFace(Integer userId, FaceDTO faceDTO);

    public boolean update (UserRegistrationRequest usuarioRequest);

    public Users findById(Integer id);
}
