package ru.tyshchenko.infosearch.search;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.dto.FileTfIdf;
import ru.tyshchenko.infosearch.dto.InvertedIndex;
import ru.tyshchenko.infosearch.dto.VectorFile;
import ru.tyshchenko.infosearch.factories.InvertedIndexFactory;
import ru.tyshchenko.infosearch.factories.TfIdfFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VectorFileSearch {

    private final SimpleFileSearch simpleFileSearch;
    private final InvertedIndexFactory invertedIndexFactory;
    private final TfIdfFactory tfIdfFactory;

    @Value("${inner.files.output}")
    private String path;

    public List<VectorFile> getFilePathsByExpression(String expression) {
        var tokenToInvIndex = invertedIndexFactory.readTokenToInvertedIndex();
        Set<String> fileNames = simpleFileSearch.findPathsByExpression(expression);
        String[] words = expression.replaceAll("[&|!()]+", "").split(" ");
        return fileNames.stream()
                .parallel()
                .map(tfIdfFactory::getFileIdf)
                .map(fileTfIdf -> createVectorFile(fileTfIdf, tokenToInvIndex, words))
                .sorted(Comparator.comparing(VectorFile::tfIdf).reversed())
                .collect(Collectors.toList());
    }

    private VectorFile createVectorFile(FileTfIdf fileTfIdf, Map<String, InvertedIndex> tokenToInvIndex,
                                        String[] words) {
        double resultValue = Arrays.stream(words)
                .map(word -> fileTfIdf.getTokenTfIdf().getOrDefault(word, 0.0) *
                        fileTfIdf.getLemmaTfIdf().getOrDefault(Optional.ofNullable(tokenToInvIndex.get(word))
                                .map(InvertedIndex::getLemma).orElse(null), 0.0))
                .reduce(0.0, (Double::sum));
        return new VectorFile(fileTfIdf.getFileName(), resultValue);
    }
}