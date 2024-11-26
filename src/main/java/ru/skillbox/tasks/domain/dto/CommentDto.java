package ru.skillbox.tasks.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {

    @NotBlank
    @Size(max = 500, message = "Длина текста от 1 до {max} символов")
    private String text;

}
