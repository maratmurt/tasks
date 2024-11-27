package ru.skillbox.tasks.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.skillbox.tasks.TasksApplicationTests;
import ru.skillbox.tasks.domain.dto.CommentDto;
import ru.skillbox.tasks.domain.dto.TaskDto;
import ru.skillbox.tasks.domain.model.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskControllerTest extends TasksApplicationTests {

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("TRUNCATE TABLE task CASCADE");
    }

    @Test
    public void getAllTest() {
        jdbcTemplate.execute("INSERT INTO task " +
                "(title, description, status, priority, creator_id, assignee_id) " +
                "VALUES ('Test task', 'Description', 'PENDING', 'MEDIUM', 1, 1)");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);
        ResponseEntity<List<Task>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/task",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>(){}
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
        List<Task> tasks = response.getBody();
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Test task");
    }

    @Test
    public void getByIdTest() {
        jdbcTemplate.execute("INSERT INTO task " +
                "(id, title, description, status, priority, creator_id, assignee_id) " +
                "VALUES (1, 'Test task', 'Description', 'PENDING', 'MEDIUM', 1, 1)");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        ResponseEntity<Task> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/task/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Task.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test task");
    }

    @Test
    public void createTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        TaskDto taskDto = TaskDto.builder()
                .title("Test task")
                .description("Description")
                .status("PENDING")
                .priority("MEDIUM")
                .assigneeId(1L)
                .build();

        ResponseEntity<Task> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/task",
                HttpMethod.POST,
                new HttpEntity<>(taskDto, headers),
                Task.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test task");
    }

    @Test
    public void updateTest() {
        jdbcTemplate.execute("INSERT INTO task " +
                "(id, title, description, status, priority, creator_id, assignee_id) " +
                "VALUES (1, 'Test task', 'Description', 'PENDING', 'MEDIUM', 1, 1)");

        TaskDto taskDto = TaskDto.builder()
                .title("Updated task")
                .description("Description")
                .status("PENDING")
                .priority("MEDIUM")
                .assigneeId(1L)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        ResponseEntity<Task> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/task/1",
                HttpMethod.PUT,
                new HttpEntity<>(taskDto, headers),
                Task.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated task");
    }

    @Test
    public void deleteTest() {
        jdbcTemplate.execute("INSERT INTO task " +
                "(id, title, description, status, priority, creator_id, assignee_id) " +
                "VALUES (1, 'Test task', 'Description', 'PENDING', 'MEDIUM', 1, 1)");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/task/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM task",
                Long.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
        assertThat(ids.size()).isEqualTo(0);
    }

    @Test
    public void addCommentTest() {
        jdbcTemplate.execute("INSERT INTO task " +
                "(id, title, description, status, priority, creator_id, assignee_id) " +
                "VALUES (1, 'Test task', 'Description', 'PENDING', 'MEDIUM', 1, 1)");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        HttpEntity<CommentDto> request = new HttpEntity<>(
                new CommentDto("Test"),
                headers
        );

        ResponseEntity<Task> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/task/1/comment",
                HttpMethod.POST,
                request,
                Task.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        String taskCommentText = response.getBody().getComments().get(0).getText();
        assertThat(taskCommentText).isEqualTo("Test");
    }

}
