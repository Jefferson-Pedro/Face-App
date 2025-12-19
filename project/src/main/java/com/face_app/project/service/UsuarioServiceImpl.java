package com.face_app.project.service;

import com.face_app.project.dto.UsuarioDTO;
import com.face_app.project.dto.UsuarioResponse;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Override
    public Boolean register(@Valid UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public Boolean update(UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public UsuarioResponse findById(Integer id) {
        return null;
    }
}
