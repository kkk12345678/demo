package com.example.demo.converter;

import com.example.demo.dto.TitleDto;
import com.example.demo.model.Title;
import org.springframework.stereotype.Component;

@Component
public class TitleConverter {
    public TitleDto fromTitleToTitleDto(Title title) {
        return TitleDto.builder()
                .id(title.getId())
                .name(title.getName())
                .description(title.getDescription())
                .img(title.getImg())
                .category(title.getCategory())
                .publisher(title.getPublisher())
                .build();
    }

    public Title fromTitleDtoToTitle(TitleDto titleDto) {
        Title title = new Title();
        title.setId(titleDto.getId());
        title.setName(titleDto.getName());
        title.setDescription(titleDto.getDescription());
        title.setImg(titleDto.getImg());
        title.setCategory(titleDto.getCategory());
        title.setPublisher(titleDto.getPublisher());
        return title;
    }
}
