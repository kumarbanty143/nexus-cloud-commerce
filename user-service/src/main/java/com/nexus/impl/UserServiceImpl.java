package com.nexus.impl;

import com.nexus.dto.LoginRequestDto;
import com.nexus.dto.UserCreateRequestDto;
import com.nexus.dto.UserResponseDto;
import com.nexus.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.nexus.repository.UserRepository;
import com.nexus.service.UserService;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto register(UserCreateRequestDto dto) {
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        User savedUser = userRepository.save(user);
        return new UserResponseDto();
    }

    @Override
    public LoginRequestDto login(LoginRequestDto dto) {
        return null;
    }

    @Override
    public UserResponseDto getById(Long id) {
        return null;
    }
}
