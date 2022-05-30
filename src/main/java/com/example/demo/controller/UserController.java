package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.AuthorizationException;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.ERole;
import com.example.demo.util.AuthenticationUtil;
import com.example.demo.util.JwtTokenUtil;
import com.example.demo.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    private final UserService userService;
    private final AuthenticationUtil authenticationUtil;
    private final JwtTokenUtil jwtTokenUtil;



    //TODO different save user logic for different roles
    @PostMapping("/signup")
    public UserDto save(
            @RequestParam String firstName,
            @RequestParam(value = "lastName", defaultValue = "") String lastName,
            @RequestParam String email,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "address", defaultValue = "") String address,
            @RequestParam(value = "role", defaultValue = "USER") String role,

            HttpServletResponse response
    ) throws NoSuchAlgorithmException, ValidationException, NotFoundException {
        authenticationUtil.validateUserData(firstName, email, password);
        try {
            UserDto userDto = userService.findByEmail(email);
            if (!isNull(userDto)) {
                throw new ValidationException("User with email:" + email + " already exists");
            }
        } catch (NotFoundException ignored) {}
        String hashedPassword = authenticationUtil.hashPassword(password);
        Map<Integer, String> tokens = jwtTokenUtil.generateToken(firstName + " " + lastName, email, role);
        String refreshToken = tokens.get(JwtTokenUtil.REFRESH_TOKEN);
        UserDto userDto = new UserDto(firstName, lastName, email, hashedPassword, address, refreshToken, role);
        setCookie(tokens, response);
        return userService.save(userDto);
    }

    @GetMapping("/all")
    public List<UserDto> findAll(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthorizationException {
        Claims claims = validateTokens(httpServletRequest, httpServletResponse);
        String role = (String) claims.get("role");
        if (!role.equals(String.valueOf(ERole.USER))) {
            return userService.findAll();
        }
        else throw new AuthorizationException("Only administrators have access");
    }

    @GetMapping("/{id}")
    public UserDto findOne(@PathVariable Integer id,
                           HttpServletResponse httpServletResponse,
                           HttpServletRequest httpServletRequest
    ) throws NotFoundException, AuthorizationException {
        Claims claims = validateTokens(httpServletRequest, httpServletResponse);
        String role = (String) claims.get("role");
        if (!role.equals(String.valueOf(ERole.USER))) {
            return userService.findById(id);
        }
        else {
            UserDto userDto = userService.findById(id);
            String email = (String) claims.get("email");
            if (userDto.getEmail().equals(email)) {
                return userDto;
            }
            throw new AuthorizationException("User has access to own account.");
        }
    }

    @PostMapping("/login")
    public UserDto login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ) throws NotFoundException, InvalidPasswordException, NoSuchAlgorithmException {

        authenticationUtil.auth(email, password);
        UserDto userDto = userService.findByEmail(email);
        Map<Integer, String> tokens = jwtTokenUtil.generateToken(
                userDto.getFirstName() + " " + userDto.getLastName(),
                userDto.getEmail(),
                userDto.getRole());
        userDto.setRefreshToken(tokens.get(JwtTokenUtil.REFRESH_TOKEN));
        userService.save(userDto);
        setCookie(tokens, response);
        return userDto;
    }


    @PostMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue();
                    cookie.setMaxAge(0);
                    httpServletResponse.addCookie(cookie);
                    authenticationUtil.logout(refreshToken);
                }
            }
        }
    }




    @DeleteMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest
    ) throws NotFoundException, AuthorizationException {
        Claims claims = validateTokens(httpServletRequest, httpServletResponse);
        String role = (String) claims.get("role");
        if (role.equals(String.valueOf(ERole.SUPERADMIN))) {
            userService.delete(id);
            return "User with id :" + id + " was successfully deleted.";
        }
        throw new AuthorizationException("Only superadmin can delete users.");
    }

    private void setCookie(Map<Integer, String> tokens, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("refreshToken", tokens.get(JwtTokenUtil.REFRESH_TOKEN));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(2147483647);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.addHeader("Authorization", "Bearer " + tokens.get(JwtTokenUtil.ACCESS_TOKEN));
    }

    private Claims validateTokens(HttpServletRequest req, HttpServletResponse res) throws AuthorizationException {
        try {
            String accessToken = req.getHeader("Authorization").split(" ")[1];
            return jwtTokenUtil.validateAccessToken(accessToken);
        } catch (Exception e) {
            Cookie[] cookies = req.getCookies();
            if (isNull(cookies)) {
                throw new AuthorizationException("Unauthorized user.");
            }
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue();
                    try {
                        Claims claims = jwtTokenUtil.validateRefreshToken(refreshToken);
                        String role = (String) claims.get("role");
                        if (!role.equals(String.valueOf(ERole.USER))) {
                            String name = (String) claims.get("name");
                            String email = (String) claims.get("email");
                            Map<Integer, String> tokens = jwtTokenUtil.generateToken(name, email, role);
                            authenticationUtil.refresh(refreshToken, tokens.get(JwtTokenUtil.REFRESH_TOKEN));
                            setCookie(tokens, res);
                            return claims;
                        }
                        else {
                            throw new AuthorizationException("Only administrators have access.");
                        }
                    } catch (Exception e1) {
                        throw new AuthorizationException(e1.getMessage());
                    }
                }
            }
            throw new AuthorizationException(e.getMessage());
        }
    }
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String validationExceptionHandler(ValidationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler({InvalidPasswordException.class, AuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String unAuthorizedHandler(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String noSuchAlgorithmHandler(Exception e) {
        return e.getMessage();
    }
}
