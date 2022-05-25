package com.example.demo.controller;

import com.example.demo.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/img")
@AllArgsConstructor
@Log
public class ImageController {
    private final ImageService imageService;

    @RequestMapping(
            value = "/{filename}",
            method = RequestMethod.GET,
            produces = "image/jpeg"
    )
    public @ResponseBody byte[] getImage(@PathVariable String filename) throws IOException {
        log.info("Handling get image: " + filename);
        return imageService.get(filename);
    }

}
