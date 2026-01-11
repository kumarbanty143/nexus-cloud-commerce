package service;

import dto.LoginRequestDto;
import dto.UserCreateRequestDto;
import dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserCreateRequestDto dto);
    LoginRequestDto login(LoginRequestDto dto);
    UserResponseDto getById(Long id);
}
