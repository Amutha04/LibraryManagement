package com.springboot.libraryManagement.service;

import com.springboot.libraryManagement.dto.request.AdminDto;
import com.springboot.libraryManagement.enums.Role;
import com.springboot.libraryManagement.model.User;
import com.springboot.libraryManagement.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void addAdmin(@Valid AdminDto adminDto) {
        User user = new User();
        user.setUsername(adminDto.username());
        user.setPassword(passwordEncoder.encode(adminDto.password()));
        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }
}