package ru.skillbox.tasks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.tasks.domain.dto.CommentDto;
import ru.skillbox.tasks.domain.dto.TaskDto;
import ru.skillbox.tasks.domain.dto.TaskFilter;
import ru.skillbox.tasks.domain.model.Status;
import ru.skillbox.tasks.domain.model.Task;
import ru.skillbox.tasks.domain.model.User;
import ru.skillbox.tasks.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Основной интерфейс приложения")
@SecurityRequirement(name = "bearer-key", scopes = {"ROLE_ADMIN", "ROLE_USER"})
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Получение всех созданных задач")
    @GetMapping
    public ResponseEntity<List<Task>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestBody(required = false) TaskFilter filter) {
        return ResponseEntity.ok(taskService.getAll(page, size, filter));
    }

    @Operation(summary = "Получение задачи по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @Operation(
            summary = "Создание новой задачи. Доступно только администратору.",
            security = @SecurityRequirement(name = "bearer-key", scopes = "ROLE_ADMIN"))
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> create(@RequestBody @Valid TaskDto taskDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(taskService.create(taskDto, user.getUsername()));
    }

    @Operation(
            summary = "Обновление задачи. Доступно только администратору.",
            security = @SecurityRequirement(name = "bearer-key", scopes = "ROLE_ADMIN"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> update(@PathVariable("id") Long id,
                                       @RequestBody @Valid TaskDto taskDto) {
        return ResponseEntity.ok(taskService.update(id, taskDto));
    }

    @Operation(
            summary = "Удаление задачи. Доступно только администратору.",
            security = @SecurityRequirement(name = "bearer-key", scopes = "ROLE_ADMIN"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Добавление комментария к задаче. Доступно только администратору и исполнителю.")
    @PostMapping("/{id}/comment")
    public ResponseEntity<Task> addComment(@PathVariable("id") Long taskId,
                                           @RequestBody @Valid CommentDto commentDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(taskService.addComment(taskId, user.getUsername(), commentDto));
    }

    @Operation(summary = "Выставление статуса задачи. Доступно только администратору и исполнителю.")
    @PatchMapping("/{id}")
    public ResponseEntity<Task> setStatus(@PathVariable("id") Long taskId,
                                          @RequestParam String status) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(taskService.setStatus(taskId, user.getUsername(), Status.valueOf(status)));
    }

}
