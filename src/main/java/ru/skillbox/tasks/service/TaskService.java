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
import ru.skillbox.tasks.exception.EntityNotFoundException;
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

    /**
     * Вывод списка задач с фильтрацией и пагинацией
     *
     * @return список задач
     */
    public List<Task> getAll(Integer page, Integer size, TaskFilter filter) {
        Specification<Task> specification = null;
        if (filter != null) {
            specification = TaskSpecification.byCreatorId(filter.creatorId())
                    .and(TaskSpecification.byAssigneeId(filter.assigneeId()));
        }
        return taskRepository.findAll(specification, PageRequest.of(page, size)).toList();
    }

    /**
     * Получение задачи по ID
     *
     * @return задача
     */
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    /**
     * Создание новой задачи
     *
     * @return новая задача
     */
    public Task create(TaskDto taskDto, String username) {
        User creator = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с email %s не найден", username)));
        User assignee = userRepository.findById(taskDto.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с ID %s не найден", taskDto.getAssigneeId())));
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(Status.valueOf(taskDto.getStatus()))
                .priority(Priority.valueOf(taskDto.getPriority()))
                .assignee(assignee)
                .creator(creator)
                .build();
        return taskRepository.save(task);
    }

    /**
     * Обновление задачи
     *
     * @return обновлённая задача
     */
    public Task update(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Задача с ID %s не найдена", taskDto.getAssigneeId())));
        User assignee = userRepository.findById(taskDto.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с ID %s не найден", taskDto.getAssigneeId())));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(Status.valueOf(taskDto.getStatus()));
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    /**
     * Удаление задачи по ID
     */
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Добавление комментария к задаче
     *
     * @return задача с новым комментарием
     */
    public Task addComment(Long taskId, String username, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Задача с ID %s не найдена", taskId)));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с email %s не найден", username)));
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !task.getAssignee().equals(user)) {
            throw new TaskUpdateSecurityException("Комментарии доступны только администратору и исполнителю!");
        }
        Comment comment = Comment.builder()
                .task(task)
                .user(user)
                .text(commentDto.getText())
                .build();
        commentRepository.save(comment);
        task.getComments().add(comment);
        return task;
    }

    /**
     * Обновление статуса задачи
     *
     * @return обновлённая задача
     */
    public Task setStatus(Long taskId, String username, Status status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Задача с ID %s не найдена", taskId)));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь с email %s не найден", username)));
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !task.getAssignee().equals(user)) {
            throw new TaskUpdateSecurityException("Изменение статуса доступно только администратору и исполнителю!");
        }
        task.setStatus(status);
        return taskRepository.save(task);
    }
}
