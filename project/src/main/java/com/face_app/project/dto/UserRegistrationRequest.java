package com.face_app.project.dto;

import com.face_app.project.model.Users;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public record UserRegistrationRequest(
        @NotNull String nome,
        @NotNull @Length(min = 11, max = 11) String cpf,
        @NotNull @Length(min = 11, max = 100) String email,
        @NotNull @Length(min = 10, max = 20) String telefone,
        @NotNull @Length(min = 3, max = 45) String login,
        @NotNull @Length(min = 6, max = 255) String senha,
        @NotNull String faceImage) {

    public Users toUsuario(){

        Users user = new Users();
        user.setNome(nome);
        user.setCpf(cpf);
        user.setTelefone(telefone);
        user.setEmail(email);
        user.setLogin(login);
        user.setSenha(new BCryptPasswordEncoder().encode(senha));
        user.setData_criacao(LocalDateTime.now());
        user.setFaceCadastrada(Boolean.FALSE);
        user.setAtivo(Boolean.TRUE);
        return user;
    }
}
