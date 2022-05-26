package com.example.demo.controller;

import com.example.demo.dto.PublisherDto;
import com.example.demo.exception.ForeignKeyConstraintSqlException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.PublisherService;
import com.example.demo.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/publishers")
@AllArgsConstructor
@Log
public class PublisherController {
    private final PublisherService publisherService;
    private final ImageService imageService;

    @GetMapping("/all")
    public List<PublisherDto> findAll() {
        log.info("Handling find all publishers request");
        return publisherService.findAll();
    }

    @GetMapping("/{id}")
    public PublisherDto findOne(@PathVariable Integer id) throws NotFoundException {
        log.info("Handling find publisher with id: " + id);
        PublisherDto publisherDto = publisherService.findById(id);
        if (isNull(publisherDto)) {
            throw new NotFoundException("Publisher with id: " + id.toString() + " does not exist");
        }
        return publisherDto;
    }

    @PostMapping("/save")
    public PublisherDto save(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image
    ) throws ValidationException {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setName(name);
        publisherDto.setDescription(description);
        if (isNull(image)) {
            publisherDto.setImg(null);
        }
        else {
            publisherDto.setImg(imageService.save(image));
        }
        log.info("Handling save publisher: " + publisherDto.getName());
        return publisherService.save(publisherDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws NotFoundException, ForeignKeyConstraintSqlException {
        log.info("Handling delete publisher with id: " + id);
        publisherService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ForeignKeyConstraintSqlException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String foreignKeyConstraintSqlExceptionHandler(ForeignKeyConstraintSqlException ex) {
        return ex.getMessage();
    }
}
