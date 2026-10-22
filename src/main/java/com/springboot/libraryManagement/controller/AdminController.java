package com.springboot.libraryManagement.controller;

import com.springboot.libraryManagement.dto.request.AdminDto;
import com.springboot.libraryManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/api/admin/add")
    public void addAdmin(@Valid @RequestBody AdminDto adminDto) {
        userService.addAdmin(adminDto);
    }
}
