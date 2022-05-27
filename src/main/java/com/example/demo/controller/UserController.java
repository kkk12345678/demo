package com.example.demo.controller;

import com.example.demo.dto.TitleDto;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Log
public class UserController {
    private final UserService userService;

    //TODO
    @PostMapping("/signup")
    public UserDto save(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "address", defaultValue = "") String address
    ) throws NoSuchAlgorithmException {
        String hashedPassword = userService.hashPassword(password);
        UserDto userDto = new UserDto();
        userDto.setAddress(address);
        userDto.setEmail(email);
        userDto.setRole("USER");
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPassword(hashedPassword);
        userDto.setRefreshToken(null);
        log.info("Handling save user: " + userDto.getEmail());
        return userService.save(userDto);
    }

    @GetMapping("/users")
    public List<UserDto> findAll() {
        log.info("Handling find all users request");
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public UserDto findOne(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling find title with id: " + id);
        UserDto userDto = userService.findById(id);
        if (isNull(userDto)) {
            throw new NotFoundException("User with id: " + id.toString() + " does not exist");
        }
        return userDto;
    }
}
