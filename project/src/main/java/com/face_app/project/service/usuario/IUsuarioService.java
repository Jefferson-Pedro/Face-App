package com.face_app.project.service.usuario;

import com.face_app.project.dto.UserLoginRequest;
import com.face_app.project.dto.UsuarioRequest;
import com.face_app.project.dto.UsuarioResponse;

public interface IUsuarioService {

    public Boolean register (UsuarioRequest usuarioRequest);

    public Boolean login (UserLoginRequest loginRequest);

    public Boolean update (UsuarioRequest usuarioRequest);

    public UsuarioResponse findById(Integer id);
}
