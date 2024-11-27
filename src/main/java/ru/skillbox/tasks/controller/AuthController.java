package ru.skillbox.tasks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.tasks.domain.dto.JwtDto;
import ru.skillbox.tasks.domain.dto.UserDto;
import ru.skillbox.tasks.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя.")
    @PostMapping("/sign-up")
    public JwtDto signUp(@RequestBody @Valid UserDto request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Аутентификация пользователя.")
    @PostMapping("/sign-in")
    public JwtDto signIn(@RequestBody @Valid UserDto request) {
        return authenticationService.signIn(request);
    }
}
