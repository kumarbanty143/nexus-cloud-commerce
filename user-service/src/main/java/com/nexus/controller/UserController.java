package com.nexus.controller;

import com.nexus.dto.LoginRequestDto;
import com.nexus.dto.LoginResponseDto;
import com.nexus.dto.UserCreateRequestDto;
import com.nexus.dto.UserResponseDto;
import com.nexus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        UserResponseDto createdUser = userService.register(userCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loggedInUser = userService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@Valid @PathVariable Long id) {
        UserResponseDto user = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    

}
