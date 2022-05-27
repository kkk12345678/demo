package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.ForeignKeyConstraintSqlException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    //TODO
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

    //TODO
    public boolean exists(UserDto userDto) {
        return false;
    }

    //TODO
    public List<UserDto> hasEmail(String email) {
        return null;
    }
    //TODO
    public void delete(Integer id) throws NotFoundException {

    }


    public UserDto findById(Integer id) throws NotFoundException {
        return userConverter.fromUserToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " does not exist")));
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashbytes);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
