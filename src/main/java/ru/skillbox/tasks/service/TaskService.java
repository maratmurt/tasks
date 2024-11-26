package ru.skillbox.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.tasks.domain.dto.CommentDto;
import ru.skillbox.tasks.domain.dto.TaskDto;
import ru.skillbox.tasks.domain.dto.TaskFilter;
import ru.skillbox.tasks.domain.model.*;
import ru.skillbox.tasks.exception.TaskUpdateSecurityException;
import ru.skillbox.tasks.repository.CommentRepository;
import ru.skillbox.tasks.repository.TaskRepository;
import ru.skillbox.tasks.repository.TaskSpecification;
import ru.skillbox.tasks.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public List<Task> getAll(Integer page, Integer size, TaskFilter filter) {
        Specification<Task> specification = null;
        if (filter != null) {
            specification = TaskSpecification.byCreatorId(filter.creatorId())
                    .and(TaskSpecification.byAssigneeId(filter.assigneeId()));
        }
        return taskRepository.findAll(specification, PageRequest.of(page, size)).toList();
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
        User assignee = userRepository.findById(taskDto.assigneeId()).orElseThrow();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(Status.valueOf(taskDto.status()));
        task.setPriority(Priority.valueOf(taskDto.priority()));
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task addComment(Long taskId, String username, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        User user = userRepository.findByEmail(username).orElseThrow();
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !task.getAssignee().equals(user)) {
            throw new TaskUpdateSecurityException("Комментарии доступны только администратору и исполнителю!");
        }
        Comment comment = Comment.builder()
                .task(task)
                .user(user)
                .text(commentDto.text())
                .build();
        commentRepository.save(comment);
        task.getComments().add(comment);
        return task;
    }

    public Task setStatus(Long taskId, String username, Status status) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        User user = userRepository.findByEmail(username).orElseThrow();
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !task.getAssignee().equals(user)) {
            throw new TaskUpdateSecurityException("Изменение статуса доступно только администратору и исполнителю!");
        }
        task.setStatus(status);
        return taskRepository.save(task);
    }
}
