package com.nexus.service;

import com.nexus.dto.LoginRequestDto;
import com.nexus.dto.UserCreateRequestDto;
import com.nexus.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserCreateRequestDto dto);
    LoginRequestDto login(LoginRequestDto dto);
    UserResponseDto getById(Long id);
}
