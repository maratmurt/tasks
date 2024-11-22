package ru.skillbox.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.tasks.dto.CommentDto;
import ru.skillbox.tasks.dto.TaskDto;
import ru.skillbox.tasks.model.*;
import ru.skillbox.tasks.repository.CommentRepository;
import ru.skillbox.tasks.repository.TaskRepository;
import ru.skillbox.tasks.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public Task create(TaskDto taskDto, String username) {
        User creator = userRepository.findByEmail(username).orElseThrow();
        User assignee = userRepository.findById(taskDto.assigneeId()).orElseThrow();
        Task task = new Task();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(Status.valueOf(taskDto.status()));
        task.setPriority(Priority.valueOf(taskDto.priority()));
        task.setAssignee(assignee);
        task.setCreator(creator);
        return taskRepository.save(task);
    }

    public Task update(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(Status.valueOf(taskDto.status()));
        task.setPriority(Priority.valueOf(taskDto.priority()));
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task addComment(Long taskId, String username, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        User user = userRepository.findByEmail(username).orElseThrow();
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setText(commentDto.text());
        commentRepository.save(comment);
        task.getComments().add(comment);
        return task;
    }

}
