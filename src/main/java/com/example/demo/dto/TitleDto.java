package com.example.demo.dto;

import com.example.demo.model.Category;
import com.example.demo.model.Publisher;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class TitleDto extends Dto {
    private Integer id;
    private String name;
    private String description;
    private String img;
    private Category category;
    private Publisher publisher;
}
