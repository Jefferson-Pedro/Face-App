package com.face_app.project.service.usuario;

import com.face_app.project.dto.UserLoginRequest;
import com.face_app.project.dto.UsuarioRequest;
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
    public Boolean register(@Valid UsuarioRequest usuarioRequest) {

        Usuario res = repository.findByEmailOrCpfOrLogin(usuarioRequest.email(), usuarioRequest.cpf(), usuarioRequest.login());

        if(res != null){
            return false;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Usuario usuario = usuarioRequest.toUsuario();
        String senhaCript = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCript);

        repository.save(usuario);
        return true;
    }

    @Override
    public Boolean login(UserLoginRequest loginRequest) {

        Usuario usuario = repository.findByLogin(loginRequest.login());

        if(usuario != null){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(loginRequest.senha(), usuario.getSenha());
        }
        return false;
    }

    @Override
    public Boolean update(UsuarioRequest usuarioRequest) {
        return null;
    }

    @Override
    public UsuarioResponse findById(Integer id) {
        return null;
    }
}
