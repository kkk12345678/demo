package com.example.demo.dto;

import com.example.demo.model.Title;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class BookSetDto extends Dto {
    private Integer id;
    private String name;
    private String description;
    private String img;
    private Integer price15;
    private Integer price30;
    private Title title;
}
