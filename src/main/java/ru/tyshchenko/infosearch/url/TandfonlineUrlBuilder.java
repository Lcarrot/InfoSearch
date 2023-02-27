package ru.tyshchenko.infosearch.url;

import lombok.SneakyThrows;
import org.jsoup.nodes.Document;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.util.List;
import java.util.Objects;

@Component("tandfonline")
public class TandfonlineUrlBuilder implements UrlBuilder {
    private static final String DOMAIN = "https://www.tandfonline.com";
    private static final String MAIN_PAGE = "/action/doSearch";
    private static final String PARAMS = "?AllField=nlp&openAccess=18&pageSize=20&startPage=";

    @Override
    @SneakyThrows
    @Retryable
    public List<String> getPageUrls(int pageNumber) {
        return ParserUtils.getDocumentPage(DOMAIN + MAIN_PAGE + PARAMS + pageNumber)
                .body()
                .getElementsByClass("ref nowrap").eachAttr("href");
    }

    @Retryable
    public String getPageContent(String page) {
        Document document = ParserUtils.getDocumentPage(DOMAIN + page);
        return Objects.requireNonNull(document
                .body()
                .getElementsByClass("hlFld-Fulltext")).html();
    }

    public String getBuilderId() {
        return "tandfonline";
    }
}
