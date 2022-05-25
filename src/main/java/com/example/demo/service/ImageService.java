package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


@Service
public class ImageService {
    @Autowired
    private final Environment env;
    private String imageDir;


   public ImageService(Environment env) {
       this.env = env;
       this.imageDir = Paths.get("").toAbsolutePath().normalize().toString() + this.env.getProperty("image.dir");
    }
    public String save(MultipartFile image) {
        final String filename = image.getOriginalFilename();
        String extension = getExtensionByStringHandling(filename);
        if (!extension.isEmpty()) {
            extension = "." + extension;
        }
        String newFilename = UUID.randomUUID().toString() + extension;
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

    public byte[] get(String filename) throws IOException{
        String path =  imageDir + filename;
        File file = new File(path);
        return Files.readAllBytes(file.toPath());
    }
    private String getExtensionByStringHandling(String filename) {
        Optional<String> optional = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        return (optional.isPresent() ? optional.get() : "");
    }
}
