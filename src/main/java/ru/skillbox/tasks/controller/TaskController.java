package ru.skillbox.tasks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.tasks.dto.CommentDto;
import ru.skillbox.tasks.dto.TaskDto;
import ru.skillbox.tasks.model.Task;
import ru.skillbox.tasks.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody TaskDto taskDto,
                                       @RequestHeader("Username") String username) {
        return ResponseEntity.ok(taskService.create(taskDto, username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable("id") Long id,
                                       @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.update(id, taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Task> addComment(@PathVariable("id") Long taskId,
                                           @RequestHeader("Username") String username,
                                           @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(taskService.addComment(taskId, username, commentDto));
    }

}
