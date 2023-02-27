package ru.tyshchenko.infosearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.infosearch.lemmatizer.WordLemmatizer;
import ru.tyshchenko.infosearch.tokenizer.TextTokenizer;

@SpringBootTest
public class Task2Test {
    @Autowired
    private TextTokenizer textTokenizer;
    @Autowired
    private WordLemmatizer wordLemmatizer;

    @Test
    void tokenizeText() {
        textTokenizer.tokenize();
    }

    @Test
    void lemmatizeWords() {
        wordLemmatizer.lemmatize();
    }
}
