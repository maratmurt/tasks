package ru.skillbox.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление комментария к задаче.")
public class CommentDto {

    @NotBlank
    @Size(max = 500, message = "Длина текста от 1 до {max} символов")
    @Schema(description = "Текст комментария")
    private String text;

}
