package com.bsuir.aviatours.service.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.write(filePath, file.getBytes());

        return filename;
    }

    public void deleteImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        Path filePath = Paths.get(uploadPath, filename);
        Files.deleteIfExists(filePath);
    }

    public Resource loadImage(String filename) throws MalformedURLException {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        Path filePath = Paths.get(uploadPath).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read file: " + filename);
        }
        return resource;
    }
}
