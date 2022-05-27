package com.example.demo.dto;

import com.example.demo.model.BookSet;
import com.example.demo.model.User;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderDto {
    private Integer id;
    private User user;
    private BookSet bookSet;
    private Integer quantity;
}
