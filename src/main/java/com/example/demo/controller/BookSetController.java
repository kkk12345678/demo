package com.example.demo.controller;

import com.example.demo.converter.TitleConverter;
import com.example.demo.dto.BookSetDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/booksets")
@AllArgsConstructor
@Log
public class BookSetController {
    private final BookSetService bookSetService;
    private final TitleService titleService;
    private final TitleConverter titleConverter;
    private final ImageService imageService;

    @GetMapping("/all")
    public List<BookSetDto> findAll() {
        log.info("Handling find all booksets request");
        return bookSetService.findAll();
    }

    @GetMapping("/{id}")
    public BookSetDto findOne(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling find bookset with id: " + id);
        BookSetDto bookSetDto = bookSetService.findById(id);
        if (isNull(bookSetDto)) {
            throw new NotFoundException("Bookset with id: " + id.toString() + " does not exist");
        }
        return bookSetDto;
    }

    @PostMapping("/save")
    public BookSetDto save(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Integer titleId,
            @RequestParam(required = false) Integer price15,
            @RequestParam Integer price30,
            @RequestParam(required = false) MultipartFile image
            ) throws ValidationException, NotFoundException {
        BookSetDto bookSetDto = new BookSetDto();
        bookSetDto.setName(name);
        bookSetDto.setDescription(description);
        bookSetDto.setTitle(titleConverter.fromTitleDtoToTitle(titleService.findById(titleId)));
        bookSetDto.setPrice15(price15);
        bookSetDto.setPrice30(price30);
        bookSetDto.setImg((isNull(image))? null : imageService.save(image));
        log.info("Handling save bookset: " + bookSetDto.getName());
        return bookSetService.save(bookSetDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling delete bookset with id: " + id);
        bookSetService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }
}
