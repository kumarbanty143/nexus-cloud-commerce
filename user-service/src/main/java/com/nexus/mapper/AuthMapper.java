package com.nexus.mapper;

import com.nexus.dto.LoginResponseDto;
import com.nexus.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public LoginResponseDto toLoginResponse(User user, String token) {
        return new LoginResponseDto(token, "Bearer", user.getId(), user.getEmail(), user.getName(), user.getPhone());
    }
}
