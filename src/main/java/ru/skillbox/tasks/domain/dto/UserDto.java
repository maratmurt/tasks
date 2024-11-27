package ru.skillbox.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию или аутентификацию пользователя")
public class UserDto {

    @NotBlank
    @Email(message = "Проверьте корректность введённого адреса электронной почты")
    @Schema(description = "Email пользователя")
    private String email;

    @NotBlank
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от {min} до {max} символов")
    @Schema(description = "Пароль пользователя")
    private String password;

}
