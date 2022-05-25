package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@Log
public class CategoryController {
    private final CategoryService categoryService;
    private final ImageService imageService;

    @GetMapping("/all")
    public List<CategoryDto> findAll() {
        log.info("Handling find all categories request");
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDto findOne(@PathVariable Integer id) {
        log.info("Handling find category with id: " + id);
        CategoryDto categoryDto = categoryService.findById(id);
        if (isNull(categoryDto)) {
            throw new CategoryNotFoundException(id);
        }
        return categoryDto;
    }

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CategoryDto save(
            @RequestPart(name = "name", required = true) String name,
            @RequestPart(name = "description", required = false) String description,
            @RequestPart(name = "img", required = false) MultipartFile image
    ) throws ValidationException {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        if (isNull(image)) {
            categoryDto.setImg(null);
        }
        else {
            categoryDto.setImg(imageService.save(image));
        }
        log.info("Handling save category: " + categoryDto.getName());
        return categoryService.save(categoryDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws CategoryNotFoundException {
        log.info("Handling delete category with id: " + id);
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

}
