package ru.tyshchenko.infosearch.crawler;

import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.files.FileUploader;
import ru.tyshchenko.infosearch.url.UrlBuilder;

import java.util.Collection;
import java.util.stream.IntStream;

@Component
public class HttpCrawler {

    private final FileUploader fileUploader;

    public HttpCrawler(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    public void download(UrlBuilder builder, int pageCount) {
        IntStream.range(1, pageCount)
                .parallel()
                .mapToObj(builder::getPageUrls)
                .flatMap(Collection::stream)
                .map(builder::getPageContent)
                .forEach(fileUploader::uploadFile);
    }
}
