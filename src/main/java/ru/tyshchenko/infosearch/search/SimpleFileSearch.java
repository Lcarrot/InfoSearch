package ru.tyshchenko.infosearch.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.dto.InvertedIndex;
import ru.tyshchenko.infosearch.expression.BooleanExpression;
import ru.tyshchenko.infosearch.files.FileUploader;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SimpleFileSearch {

    private final ObjectMapper objectMapper;
    private final FileUploader fileUploader;

    @Value("${inverted.index.file}")
    private String filePath;

    @SneakyThrows
    private BooleanExpression<String> getExpressionResolver() {
        List<InvertedIndex> indexList = objectMapper.readValue(
                fileUploader.getFilePath(filePath).toFile(), new TypeReference<>() {
                });
        Map<String, Set<String>> keyToValue = new HashMap<>();
        for (InvertedIndex index : indexList) {
            keyToValue.putAll(index.getTokenToDocId());
            keyToValue.computeIfAbsent(index.getLemma(), key -> index.getTokenToDocId().values()
                    .stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        }
        return new BooleanExpression<>(keyToValue);
    }

    @SneakyThrows
    public Set<String> findPathsByExpression(String expression) {
        return getExpressionResolver().resolve(expression);
    }
}
