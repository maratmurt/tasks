package ru.skillbox.tasks.dto;

public record TaskDto(
        String title,
        String description,
        String status,
        String priority,
        Long assigneeId
) {
}
