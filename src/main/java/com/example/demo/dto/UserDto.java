package com.example.demo.dto;

import lombok.*;


@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String refreshToken;
    private String role;

    public UserDto(String firstName, String lastName, String email, String password, String address, String refreshToken, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.password = password;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
