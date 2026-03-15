package com.face_app.project.dto;

import com.face_app.project.model.Users;

public record UserResponse(Integer id, String nome, String email) {

    public static UserResponse toUserResponse(Users user){
        return new UserResponse(
                user.getIdUsuario(),
                user.getNome(),
                user.getEmail()
        );
    }
}
