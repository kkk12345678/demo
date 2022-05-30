package com.example.demo.util;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Component
public class AuthenticationUtil {
    private final UserService userService;
    private final String passwordValidationRegex;
    private final String passwordValidationText;

    public AuthenticationUtil(UserService userService, Environment env) {
        this.userService = userService;
        this.passwordValidationRegex = Objects.requireNonNull(env.getProperty("password.validation.regexp"));
        this.passwordValidationText = Objects.requireNonNull(env.getProperty("password.validation.text"));
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

    public void validateUserData(String firstName, String email, String password) throws ValidationException {
        if (firstName.isEmpty()) {
            throw new ValidationException("First name must be specified.");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ValidationException("Email is invalid.");
        }
        if (!password.matches(passwordValidationRegex)) {
            throw new ValidationException(passwordValidationText);
        }
    }

    public void auth(String email, String password) throws InvalidPasswordException, NotFoundException, NoSuchAlgorithmException {
        UserDto userDto = userService.findByEmail(email);
        if (!userDto.getPassword().equals(hashPassword(password))) {
            throw new InvalidPasswordException("Password is incorrect.");
        }
    }

    public void logout(String refreshToken) {
        try {
            UserDto userDto = userService.findByRefreshToken(refreshToken);
            userDto.setRefreshToken(null);
            userService.save(userDto);
        } catch (NotFoundException ignored) {}
    }

    public void refresh(String oldRefreshToken, String newRefreshToken) throws NotFoundException {
        UserDto userDto = userService.findByRefreshToken(oldRefreshToken);
        userDto.setRefreshToken(newRefreshToken);
        userService.save(userDto);
    }
}
