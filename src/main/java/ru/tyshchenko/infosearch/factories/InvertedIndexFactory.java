package ru.tyshchenko.infosearch.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.dto.InvertedIndex;
import ru.tyshchenko.infosearch.files.FileUploader;
import ru.tyshchenko.infosearch.nlp.tools.WordLemmatizer;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.nio.file.Path;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InvertedIndexFactory {

    @Value("${inner.files.output}")
    private String path;
    @Value("${inverted.index.file}")
    private String filePath;

    private final WordLemmatizer lemmatizer;
    private final FileUploader fileUploader;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void buildIndex() {
        Map<String, InvertedIndex> tokenToIndex = readTokenToInvertedIndex();
        Map<Path, String> files = ParserUtils.getSourceTextFromFiles(Path.of(path, "pages"));
        files.forEach((path, content) -> {
            String fileName = path.getFileName().toString();
            Arrays.stream(content.split(" "))
                    .map(String::toLowerCase)
                    .filter(tokenToIndex::containsKey)
                    .forEach(token -> tokenToIndex.get(token).getTokenToDocId().get(token).add(fileName));
        });
        objectMapper.writeValue(fileUploader.getFilePath(filePath).toFile(), tokenToIndex.values());
    }

    public Map<String, InvertedIndex> readTokenToInvertedIndex() {
        Map<String, Set<String>> lemmaToTokens = lemmatizer.getTokensByLemmas();
        Map<String, InvertedIndex> tokenToIndex = new HashMap<>();
        for (Map.Entry<String, Set<String>> lemmaToToken : lemmaToTokens.entrySet()) {
            var index = new InvertedIndex();
            index.setLemma(lemmaToToken.getKey());
            lemmaToToken.getValue().forEach(token -> {
                index.getTokenToDocId().put(token, new HashSet<>());
                tokenToIndex.put(token, index);
            });
        }
        return tokenToIndex;
    }
}
