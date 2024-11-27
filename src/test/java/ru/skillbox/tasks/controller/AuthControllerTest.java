package ru.skillbox.tasks.controller;

import org.junit.jupiter.api.Test;
import ru.skillbox.tasks.TasksApplicationTests;
import ru.skillbox.tasks.domain.dto.JwtDto;
import ru.skillbox.tasks.domain.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthControllerTest extends TasksApplicationTests {

    @Test
    public void signInTest() {
        String url = "http://localhost:" + port + "/auth/sign-in";
        UserDto request = new UserDto("admin@mail.ru", "88888888");
        JwtDto response = restTemplate.postForObject(url, request, JwtDto.class);

        assertThat(response).isNotNull();
        assertThat(response.token()).isNotBlank();
    }

    @Test
    public void signUpTest() {
        String url = "http://localhost:" + port + "/auth/sign-up";
        UserDto request = new UserDto("user@mail.ru", "88888888");
        JwtDto response = restTemplate.postForObject(url, request, JwtDto.class);

        assertThat(response).isNotNull();
        assertThat(response.token()).isNotBlank();
    }

}
