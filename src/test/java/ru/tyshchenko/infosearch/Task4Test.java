package ru.tyshchenko.infosearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.infosearch.factories.InvertedIndexFactory;
import ru.tyshchenko.infosearch.factories.TfIdfFactory;
import ru.tyshchenko.infosearch.search.SimpleFileSearch;

@SpringBootTest
public class Task4Test {

    @Autowired
    private TfIdfFactory builder;

    @Test
    void calculateTfidfForTokens() {
        builder.calculateTfidf();
    }
}
