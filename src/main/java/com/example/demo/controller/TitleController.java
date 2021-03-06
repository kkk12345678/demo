package com.example.demo.controller;

import com.example.demo.converter.CategoryConverter;
import com.example.demo.converter.PublisherConverter;
import com.example.demo.dto.TitleDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.CategoryService;
import com.example.demo.util.ImageUtil;
import com.example.demo.service.PublisherService;
import com.example.demo.service.TitleService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/titles")
@AllArgsConstructor
@Log
public class TitleController {
    private final TitleService titleService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final CategoryConverter categoryConverter;
    private final PublisherConverter publisherConverter;
    private final ImageUtil imageUtil;

    @GetMapping("/all")
    public List<TitleDto> findAll() {
        log.info("Handling find all titles request");
        return titleService.findAll();
    }

    @GetMapping("/{id}")
    public TitleDto findOne(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling find title with id: " + id);
        TitleDto titleDto = titleService.findById(id);
        if (isNull(titleDto)) {
            throw new NotFoundException("Title with id: " + id.toString() + " does not exist");
        }
        return titleDto;
    }

    @PostMapping("/save")
    public TitleDto save(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Integer categoryId,
            @RequestParam Integer publisherId,
            @RequestParam(required = false) MultipartFile image
            ) throws ValidationException, NotFoundException {
        TitleDto titleDto = new TitleDto();
        titleDto.setName(name);
        titleDto.setDescription(description);
        titleDto.setCategory(categoryConverter.fromCategoryDtoToCategory(categoryService.findById(categoryId)));
        titleDto.setPublisher(publisherConverter.fromPublisherDtoToPublisher(publisherService.findById(publisherId)));
        titleDto.setImg(imageUtil.save(image));
        log.info("Handling save title: " + titleDto.getName());
        return titleService.save(titleDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling delete title with id: " + id);
        titleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException e) {
        return e.getMessage();
    }
}
