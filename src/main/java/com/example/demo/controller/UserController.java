package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.JwtTokenService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Log
public class UserController {
    private static final String DEFAULT_ROLE = "SUPERADMIN";
    private final UserService userService;
    private final JwtTokenService jwtTokenService;



    //TODO different save user logic for different roles
    @PostMapping("/signup")
    public UserDto save(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "address", defaultValue = "") String address,
            HttpServletResponse response
    ) throws NoSuchAlgorithmException, ValidationException {
        userService.validateUserData(email, password);
        String hashedPassword = password.equals("") ? null : userService.hashPassword(password);
        UserDto userDto = new UserDto();
        userDto.setAddress(address);
        userDto.setEmail(email);
        userDto.setRole(DEFAULT_ROLE);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPassword(hashedPassword);
        Map<Integer, String> tokens = jwtTokenService.generateToken(firstName + " " + lastName, email, DEFAULT_ROLE);
        userDto.setRefreshToken(tokens.get(JwtTokenService.REFRESH_TOKEN));
        log.info("Handling save user: " + userDto.getEmail());
        Cookie cookie = new Cookie("refreshToken", tokens.get(JwtTokenService.REFRESH_TOKEN));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(2147483647);
        response.addCookie(cookie);
        response.addHeader("accessToken", tokens.get(JwtTokenService.ACCESS_TOKEN));
        return userService.save(userDto);
    }

    @GetMapping("/")
    public List<UserDto> findAll() {
        log.info("Handling find all users request");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findOne(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling find user with id: " + id);
        UserDto userDto = userService.findById(id);
        if (isNull(userDto)) {
            throw new NotFoundException("User with id: " + id.toString() + " does not exist");
        }
        return userDto;
    }

    //TODO login logic
    @PostMapping("/login")
    public UserDto login() {
        return null;
    }

    //TODO logout logic
    @PostMapping("/logout")
    public UserDto logout() {
        return null;
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling delete title with id: " + id);
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String validationExceptionHandler(ValidationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }
}
