package com.face_app.project.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequest(
        @NotNull @Length(min = 3, max = 45) String login,
        @NotNull @Length(min = 6, max = 255) String senha) {
}
