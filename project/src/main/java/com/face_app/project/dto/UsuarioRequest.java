package com.face_app.project.dto;

import com.face_app.project.model.User;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record UsuarioRequest(
        @NotNull String nome,
        @NotNull @Length(min = 11, max = 11) String cpf,
        @NotNull @Length(min = 11, max = 100) String email,
        @NotNull @Length(min = 10, max = 20) String telefone,
        @NotNull @Length(min = 3, max = 45) String login,
        @NotNull @Length(min = 6, max = 255) String senha ) {

    public User toUsuario(){

        User user = new User();
        user.setNome(nome);
        user.setCpf(cpf);
        user.setTelefone(telefone);
        user.setEmail(email);
        user.setLogin(login);
        user.setSenha(senha);
        user.setData_criacao(LocalDateTime.now());
        user.setAtivo(Boolean.TRUE);
        return user;
    }
}
