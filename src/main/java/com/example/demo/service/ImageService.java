package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@Service
public class ImageService {

    private final String imageDir;

    public ImageService(Environment env) {
        this.imageDir = Paths.get("").toAbsolutePath().normalize() + env.getProperty("image.dir");
    }
    public String save(MultipartFile image) {
        final String filename = image.getOriginalFilename();
        String extension = getExtensionByStringHandling(filename);
        if (!extension.isEmpty()) {
            extension = "." + extension;
        }
        String newFilename = UUID.randomUUID() + extension;
        String path =  imageDir + newFilename;
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(image.getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFilename;
    }

    public byte[] get(String filename) throws IOException, NotFoundException {
        String path =  imageDir + filename;
        File file = new File(path);
        if (!file.exists()) {
            throw new NotFoundException("File " + filename + " does not exist");
        }
        return Files.readAllBytes(file.toPath());
    }

    private String getExtensionByStringHandling(String filename) {
        Optional<String> optional = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        return (optional.orElse(""));
    }
}
