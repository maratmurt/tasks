package ru.skillbox.tasks.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskDto {

    @NotBlank
    @Size(min = 1, max = 50, message = "Длина заголовка от {min} до {max} символов")
    private String title;

    @Size(max = 500, message = "Длина текста до {max} символов")
    private String description;

    @NotBlank
    private String status;

    @NotBlank
    private String priority;

    private Long assigneeId;
}
