package ru.skillbox.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ c токеном доступа")
public record JwtDto(
        @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1...")
        String token
) {
}
