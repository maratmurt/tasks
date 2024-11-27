package ru.skillbox.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание или обновление задачи.")
public class TaskDto {

    @NotBlank
    @Size(min = 1, max = 50, message = "Длина заголовка от {min} до {max} символов")
    @Schema(description = "Заголовок", example = "Покормить собаку")
    private String title;

    @Size(max = 500, message = "Длина текста до {max} символов")
    @Schema(description = "Описание задачи", example = "Пойти в магазин, купить корм,...")
    private String description;

    @NotBlank
    @Schema(description = "Статус выполнения", example = "DONE")
    private String status;

    @NotBlank
    @Schema(description = "Приоритет задачи", example = "HIGH")
    private String priority;

    @Schema(description = "ID исполнителя задачи", example = "42")
    private Long assigneeId;
}
