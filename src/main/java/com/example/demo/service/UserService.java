package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;


    public UserDto save(UserDto userDto)  {
        return userConverter.fromUserToUserDto(userRepository.save(userConverter.fromUserDtoToUser(userDto)));
    }

    public List<UserDto> findAll() {

        return userRepository
                .findAll()
                .stream()
                .map(userConverter::fromUserToUserDto)
                .collect(Collectors.toList());
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

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] bytes = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
