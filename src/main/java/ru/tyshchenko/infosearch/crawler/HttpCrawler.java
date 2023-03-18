package ru.tyshchenko.infosearch.crawler;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.files.FileUploader;
import ru.tyshchenko.infosearch.url.UrlBuilder;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class HttpCrawler {

    private final FileUploader fileUploader;

    public HttpCrawler(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    public void download(UrlBuilder builder, int pageCount) {
        List<Pair<String, String>> pairs = IntStream.range(1, pageCount)
                .parallel()
                .mapToObj(builder::getPageUrls)
                .flatMap(Collection::stream)
                .map(url -> new Pair<>(url, builder.getPageContent(url)))
                .distinct()
                .map(pair -> {
                    String[] parts = pair.getValue0().split("/");
                    String name = parts[parts.length - 2] + parts[parts.length - 1] + ".txt";
                    uploadFile(pair.getValue1(), name);
                    return new Pair<>(pair.getValue0(), name);
                }).toList();
        saveIndexFile(pairs);
    }

    @SneakyThrows
    public void uploadFile(String content, String name) {
        Path filePath = fileUploader.getFilePath("pages", name);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath.toFile())))) {
            writer.write(content);
        }
    }

    @SneakyThrows
    private void saveIndexFile(List<Pair<String, String>> urlToName) {
        Path filePath = fileUploader.getFilePath("index.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Pair<String, String> pair : urlToName) {
                writer.write(pair.getValue1() + " : " + pair.getValue0() + "\n");
            }
        }
    }

@SneakyThrows
    public String getSourceUrl(String fileName) {
        return Files.readAllLines(fileUploader.getFilePath("index.txt")).stream()
                .filter(line -> line.contains(fileName))
                .map(line -> line.split(" : ")[1])
                .findAny().orElseThrow(IllegalArgumentException::new);
    }
}
