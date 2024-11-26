package ru.skillbox.tasks.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank
    @Email(message = "Проверьте корректность введённого адреса электронной почты")
    private String email;

    @NotBlank
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от {min} до {max} символов")
    private String password;

}
