package ru.tyshchenko.infosearch.crawler;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
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
                    fileUploader.uploadFile(pair.getValue1(), name);
                    return new Pair<>(pair.getValue0(), name);
                }).toList();
        fileUploader.saveIndexFile(pairs);
    }
}
