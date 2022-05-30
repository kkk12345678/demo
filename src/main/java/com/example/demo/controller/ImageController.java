package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import com.example.demo.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/img")
@AllArgsConstructor
@Log
public class ImageController {
    private final ImageUtil imageUtil;

    @RequestMapping(
            value = "/{filename}",
            method = RequestMethod.GET,
            produces = "image/jpeg"
    )
    public @ResponseBody byte[] getImage(@PathVariable String filename) throws IOException, NotFoundException {
        log.info("Handling get image: " + filename);
        return imageUtil.get(filename);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException e) {
        return e.getMessage();
    }
}
