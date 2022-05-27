package com.example.demo.converter;

import com.example.demo.dto.BookSetDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.BookSet;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserDto fromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .address(user.getAddress())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .refreshToken(user.getRefreshToken())
                .role(user.getRole())
                .build();
    }

    public User fromUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setAddress(userDto.getAddress());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setRefreshToken(userDto.getRefreshToken());
        user.setRole(userDto.getRole());
        return user;
    }
}
