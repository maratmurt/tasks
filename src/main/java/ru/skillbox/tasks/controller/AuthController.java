package ru.skillbox.tasks.controller;

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
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtDto signUp(@RequestBody UserDto request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtDto signIn(@RequestBody UserDto request) {
        return authenticationService.signIn(request);
    }
}
