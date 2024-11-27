package ru.skillbox.tasks.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Фильтр списка задач")
public record TaskFilter(

        @Schema(description = "ID автора задачи")
        Long creatorId,

        @Schema(description = "ID исполнителя задачи")
        Long assigneeId
) {
}
