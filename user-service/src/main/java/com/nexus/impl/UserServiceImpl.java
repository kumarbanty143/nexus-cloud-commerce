package com.nexus.impl;

import com.nexus.dto.LoginRequestDto;
import com.nexus.dto.LoginResponseDto;
import com.nexus.dto.UserCreateRequestDto;
import com.nexus.dto.UserResponseDto;
import com.nexus.entity.User;
import com.nexus.exception.ResourceAlreadyExistException;
import com.nexus.exception.ResourceNotFoundException;
import com.nexus.mapper.AuthMapper;
import com.nexus.mapper.UserMapper;
import com.nexus.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.nexus.repository.UserRepository;
import com.nexus.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto register(UserCreateRequestDto dto) {
        if(userRepository.existsByEmail(dto.getEmail())){
            throw  new ResourceAlreadyExistException("Email Already Exist");
        }

        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Invalid Email address"));

        boolean matches = passwordEncoder.matches(user.getPassword(),dto.getPassword());

        if(!matches) {
            throw new ResourceNotFoundException("Invalid Password");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return authMapper.toLoginResponse(user, token);
    }

    @Override
    public UserResponseDto getById(Long id) {
       User user =  userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"+id));
       return userMapper.toDto(user);
    }
}
