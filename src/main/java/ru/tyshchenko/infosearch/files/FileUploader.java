package ru.tyshchenko.infosearch.files;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileUploader {

    @Value("${inner.files.output}")
    private String path;

    @SneakyThrows
    public void uploadFile(String content) {
        Path dirPath = Path.of(path);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(UUID.randomUUID() + ".txt");
        Files.createFile(filePath);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath.toFile())))) {
            writer.write(content);
        }
    }
}
