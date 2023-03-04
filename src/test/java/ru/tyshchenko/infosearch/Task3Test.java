package ru.tyshchenko.infosearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.infosearch.factories.InvertedIndexFactory;
import ru.tyshchenko.infosearch.search.SimpleFileSearch;

@SpringBootTest
public class Task3Test {

    @Autowired
    private InvertedIndexFactory builder;
    @Autowired
    private SimpleFileSearch fileSearch;

    @Test
    void testInvertedIndexBuilder() {
        builder.buildIndex();
    }

    @Test
    void booleanSearch() {
        fileSearch.findPathsByExpression("result & !(spread | derive)")
                .forEach(path -> System.out.println("path: " + path));
    }
}
