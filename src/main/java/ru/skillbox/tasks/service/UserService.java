package ru.skillbox.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.skillbox.tasks.domain.model.User;
import ru.skillbox.tasks.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
