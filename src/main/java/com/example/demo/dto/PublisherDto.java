package com.example.demo.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class PublisherDto extends CatalogueDto {
    private Integer id;
    private String name;
    private String description;
    private String img;
}
