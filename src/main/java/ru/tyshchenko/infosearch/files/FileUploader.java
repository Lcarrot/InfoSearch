package ru.tyshchenko.infosearch.files;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileUploader {

    @Value("${inner.files.output}")
    private String path;

    @SneakyThrows
    public Path getFilePath(String ... pathParts) {
        Path dirPath = Path.of(path);
        if (pathParts.length == 0) {
            return dirPath;
        }
        for (int i = 0; i < pathParts.length - 1; i++) {
            dirPath = Path.of(pathParts[i]);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        }
        Path filePath = dirPath.resolve(pathParts[pathParts.length - 1]);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        return filePath;
    }
}
