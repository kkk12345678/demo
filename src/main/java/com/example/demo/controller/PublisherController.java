package com.example.demo.controller;

import com.example.demo.dto.PublisherDto;
import com.example.demo.exception.PublisherNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.PublisherService;
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
    public PublisherDto findOne(@PathVariable Integer id) {
        log.info("Handling find publisher with id: " + id);
        PublisherDto publisherDto = publisherService.findById(id);
        if (isNull(publisherDto)) {
            throw new PublisherNotFoundException(id);
        }
        return publisherDto;
    }

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public PublisherDto save(
            @RequestPart(name = "name") String name,
            @RequestPart(name = "description", required = false) String description,
            @RequestPart(name = "img", required = false) MultipartFile image
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
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws PublisherNotFoundException {
        log.info("Handling delete publisher with id: " + id);
        publisherService.delete(id);
        return ResponseEntity.ok().build();
    }
}
