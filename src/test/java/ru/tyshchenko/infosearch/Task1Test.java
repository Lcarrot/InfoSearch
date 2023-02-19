package ru.tyshchenko.infosearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.infosearch.crawler.HttpCrawler;
import ru.tyshchenko.infosearch.url.HabrUrlBuilder;

@SpringBootTest
public class Task1Test {

    @Autowired
    private HttpCrawler httpCrawler;
    @Autowired
    private HabrUrlBuilder habrUrlBuilder;

    @Test
    void downloadPages() {
        httpCrawler.download(habrUrlBuilder, 10);
    }
}
