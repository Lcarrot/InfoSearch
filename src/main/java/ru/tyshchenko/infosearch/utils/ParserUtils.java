package ru.tyshchenko.infosearch.utils;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


public class ParserUtils {

    @SneakyThrows
    public static Document getDocumentPage(String url) {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36")
                .get();
    }
    @SneakyThrows
    public static String getSourceText(Path filePath) {
        return Jsoup.parse(Files.readString(filePath)).text();
    }

    @SneakyThrows
    public static String getSourceTextFromFiles(Path dirPath) {
        try(Stream<Path> walk = Files.walk(dirPath)) {
            return walk.filter(Files::isRegularFile)
                    .map(ParserUtils::getSourceText)
                    .reduce("", (a, b) -> a + b);
        }
    }
}
