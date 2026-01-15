package com.nexus.service;

import com.nexus.dto.LoginRequestDto;
import com.nexus.dto.LoginResponseDto;
import com.nexus.dto.UserCreateRequestDto;
import com.nexus.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserCreateRequestDto dto);
    LoginResponseDto login(LoginRequestDto dto);
    UserResponseDto getById(Long id);
}
