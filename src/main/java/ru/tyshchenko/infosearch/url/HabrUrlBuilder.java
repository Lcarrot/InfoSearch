package ru.tyshchenko.infosearch.url;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.util.List;
import java.util.Objects;

@Component("habr")
public class HabrUrlBuilder implements UrlBuilder {

    private static final String DOMAIN = "https://habr.com";
    private static final String MAIN_PAGE = "/ru/all/page";

    @Override
    @SneakyThrows
    public List<String> getPageUrls(int pageNumber) {
        return ParserUtils.getDocumentPage(DOMAIN + MAIN_PAGE + pageNumber + "/")
                .body()
                .getElementsByClass("tm-article-snippet__title-link").eachAttr("href");
    }

    public String getPageContent(String page) {
        return Objects.requireNonNull(ParserUtils.getDocumentPage(DOMAIN + page)
                .body()
                .getElementById("post-content-body")).html();
    }

    public String getBuilderId() {
        return "habr";
    }
}
