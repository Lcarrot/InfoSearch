package ru.tyshchenko.infosearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tyshchenko.infosearch.crawler.HttpCrawler;
import ru.tyshchenko.infosearch.dto.TextResponse;
import ru.tyshchenko.infosearch.dto.VectorFile;
import ru.tyshchenko.infosearch.search.VectorFileSearch;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    @Value("${inner.files.output}")
    private String path;

    @Value("${origin.domain}")
    private String domain;

    private final VectorFileSearch vectorFileSearch;
    private final HttpCrawler crawler;

    @GetMapping(value = "/search/{request}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public List<VectorFile> getFiles(@PathVariable String request) {
        return vectorFileSearch.getFilePathsByExpression(request);
    }

    @GetMapping(value = "/page/{pageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    @SneakyThrows
    public TextResponse getContent(@PathVariable String pageName) {
        Path filePath = Path.of(path, "pages", pageName + ".txt");
        return new TextResponse(ParserUtils.getSourceText(filePath),
                domain + crawler.getSourceUrl(pageName + ".txt"));
    }
}
