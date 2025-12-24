package com.face_app.project.dto;

import com.face_app.project.model.Usuario;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record UsuarioDTO(
        @NotNull String nome,
        @NotNull @Length(min = 11, max = 11) String cpf,
        @NotNull @Length(min = 11, max = 100) String email,
        @NotNull @Length(min = 10, max = 20) String telefone,
        @NotNull @Length(min = 3, max = 45) String login,
        @NotNull @Length(min = 6, max = 255) String senha ) {

    public Usuario toUsuario(){

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setTelefone(telefone);
        usuario.setEmail(email);
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setData_criacao(LocalDateTime.now());
        usuario.setAtivo(Boolean.TRUE);
        return usuario;
    }
}
