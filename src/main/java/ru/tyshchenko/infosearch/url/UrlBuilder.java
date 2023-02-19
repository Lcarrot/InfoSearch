package ru.tyshchenko.infosearch.url;

import java.util.List;

public interface UrlBuilder {

    List<String> getPageUrls(int pageNumber);
    String getBuilderId();
    String getPageContent(String page);
}
