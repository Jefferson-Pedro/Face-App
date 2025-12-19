package com.face_app.project.service;

import com.face_app.project.dto.UsuarioDTO;
import com.face_app.project.dto.UsuarioResponse;

public interface IUsuarioService {

    public Boolean register (UsuarioDTO usuarioDTO);

    public Boolean update (UsuarioDTO usuarioDTO);

    public UsuarioResponse findById(Integer id);
}
