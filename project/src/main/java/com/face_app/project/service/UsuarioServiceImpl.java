package com.face_app.project.service;

import com.face_app.project.dto.UsuarioDTO;
import com.face_app.project.dto.UsuarioResponse;
import com.face_app.project.model.Usuario;
import com.face_app.project.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository repository;

    @Override
    public Boolean register(@Valid UsuarioDTO usuarioDTO) {

        Usuario res = repository.findByEmailOrCpfOrLogin(usuarioDTO.email(), usuarioDTO.cpf(), usuarioDTO.login());

        if(res != null){
            return false;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Usuario usuario = usuarioDTO.toUsuario();
        String senhaCript = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCript);

        repository.save(usuario);
        return true;

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
