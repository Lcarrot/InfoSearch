package ru.tyshchenko.infosearch.files;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FileUploader {

    @Value("${inner.files.output}")
    private String path;

    @SneakyThrows
    public void uploadFile(String content, String name) {
        Path dirPath = Path.of(path, "pages");
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(name);
        Files.createFile(filePath);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath.toFile())))) {
            writer.write(content);
        }
    }

    @SneakyThrows
    public void saveIndexFile(List<Pair<String, String>> urlToName) {
        Path dirPath = Path.of(path);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve("index.txt");
        Files.createFile(filePath);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath.toFile())))) {
            for (Pair<String, String> pair: urlToName) {
                writer.write(pair.getValue1() + " : " + pair.getValue0() + "\n");
            }
        }
    }
}
