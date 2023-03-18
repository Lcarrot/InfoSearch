package ru.tyshchenko.infosearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.infosearch.search.VectorFileSearch;

@SpringBootTest
public class Task5Test {

    @Autowired
    private VectorFileSearch vectorFileSearch;

    @Test
    void testSearch() {
        vectorFileSearch.getFilePathsByExpression("result & !(spread | derive)")
                .forEach(System.out::println);
    }
}
