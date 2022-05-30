package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserDto save(UserDto userDto) {
        return userConverter.fromUserToUserDto(userRepository.save(userConverter.fromUserDtoToUser(userDto)));
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userConverter::fromUserToUserDto).collect(Collectors.toList());
    }

    public void delete(Integer id) throws NotFoundException {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Title with id: " + id + " does not exist"));
        userRepository.deleteById(id);
    }

    public UserDto findById(Integer id) throws NotFoundException {
        return userConverter.fromUserToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " does not exist")));
    }

    public UserDto findByEmail(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email);
        if (isNull(user)) {
            throw new NotFoundException("User with email: " + email + " does not exist");
        }
        return userConverter.fromUserToUserDto(user);
    }

    public UserDto findByRefreshToken(String token) throws NotFoundException {
        User user = userRepository.findByRefreshToken(token);
        if (isNull(user)) {
            throw new NotFoundException("User not found");
        }
        return userConverter.fromUserToUserDto(user);
    }

    public long count() {
        return userRepository.count();
    }
}
