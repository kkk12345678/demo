package com.example.demo.converter;

import com.example.demo.dto.BookSetDto;
import com.example.demo.model.BookSet;
import org.springframework.stereotype.Component;

@Component
public class BookSetConverter {
    public BookSetDto fromBookSetToBookSetDto(BookSet bookSet) {
        return BookSetDto.builder()
                .id(bookSet.getId())
                .name(bookSet.getName())
                .description(bookSet.getDescription())
                .img(bookSet.getImg())
                .title(bookSet.getTitle())
                .price15(bookSet.getPrice15())
                .price30(bookSet.getPrice30())
                .build();
    }

    public BookSet fromBookSetDtoToBookSet(BookSetDto bookSetDto) {
        BookSet bookSet = new BookSet();
        bookSet.setId(bookSetDto.getId());
        bookSet.setName(bookSetDto.getName());
        bookSet.setDescription(bookSetDto.getDescription());
        bookSet.setImg(bookSetDto.getImg());
        bookSet.setTitle(bookSetDto.getTitle());
        bookSet.setPrice15(bookSetDto.getPrice15());
        bookSet.setPrice30(bookSetDto.getPrice30());
        return bookSet;
    }
}
