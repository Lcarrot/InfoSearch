package ru.tyshchenko.infosearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.infosearch.crawler.HttpCrawler;
import ru.tyshchenko.infosearch.url.HabrUrlBuilder;
import ru.tyshchenko.infosearch.url.TandfonlineUrlBuilder;

@SpringBootTest
public class Task1Test {

    @Autowired
    private HttpCrawler httpCrawler;
    @Autowired
    private HabrUrlBuilder habrUrlBuilder;
    @Autowired
    private TandfonlineUrlBuilder tandfonlineUrlBuilder;

    @Test
    void downloadHabrPages() {
        httpCrawler.download(habrUrlBuilder, 10);
    }

    @Test
    void downloadSpringerPages() {
        httpCrawler.download(tandfonlineUrlBuilder, 10);
    }
}
