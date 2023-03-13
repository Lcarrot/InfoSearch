package ru.tyshchenko.infosearch.factories;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.nlp.tools.TextTokenizer;
import ru.tyshchenko.infosearch.dto.TfIdf;
import ru.tyshchenko.infosearch.nlp.tools.WordLemmatizer;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class TfIdfFactory {
    @Value("${inner.files.output}")
    private String path;
    private final TextTokenizer textTokenizer;
    private final WordLemmatizer wordLemmatizer;

    @SneakyThrows
    public void calculateTfidf() {
        Map<Path, String> files = ParserUtils.getSourceTextFromFiles(Path.of(path, "pages"));
        Map<String, Set<String>> lemmaToTokens = wordLemmatizer.getTokensByLemmas();
        Map<String, String> tokenToLemma = lemmaToTokens.entrySet()
                .stream().map(entry -> entry.getValue().stream()
                        .collect(toMap(Function.identity(), value -> entry.getKey())).entrySet())
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<TfIdf> dtoList = files.entrySet().stream().parallel().map(file -> buildTfidf(file.getKey().getFileName().toString(), file.getValue(), tokenToLemma)).toList();
        Map<String, Integer> tokenToFileCount = new HashMap<>();
        Map<String, Integer> lemmasToFileCount = new HashMap<>();

        for (TfIdf dto : dtoList) {
            for (String token : dto.getTokenToCount().keySet()) {
                tokenToFileCount.put(token, tokenToFileCount.getOrDefault(token, 0) + 1);
            }
            for (String lemmas : dto.getLemmaToCount().keySet()) {
                lemmasToFileCount.put(lemmas, lemmasToFileCount.getOrDefault(lemmas, 0) + 1);
            }
        }

        Map<String, Double> tokenToIdf = calculateIdf(tokenToFileCount, files.size());
        Map<String, Double> lemmaToIdf = calculateIdf(lemmasToFileCount, files.size());

        for (TfIdf dto : dtoList) {
            Map<String, Double> tokenToTf = calculateTf(dto.getTokenToCount());
            Map<String, Double> lemmaToTf = calculateTf(dto.getLemmaToCount());
            uploadTfidfForInFile(Path.of((path + "tfidf/tokens/" + dto.getFileName())), tokenToTf, tokenToIdf);
            uploadTfidfForInFile(Path.of((path + "tfidf/lemmas/" + dto.getFileName())), lemmaToTf, lemmaToIdf);
        }
    }

    private Map<String, Double> calculateTf(Map<String, Long> wordToCount) {
        Set<Map.Entry<String, Long>> entrySet = wordToCount.entrySet();
        long size = entrySet.stream().mapToLong(Map.Entry::getValue).sum();
        Map<String, Double> wordToTf = new HashMap<>();
        for (Map.Entry<String, Long> count : entrySet) {
            wordToTf.put(count.getKey(), count.getValue() / (double) size);
        }
        return wordToTf;
    }

    private Map<String, Double> calculateIdf(Map<String, Integer> wordToFileCount, int filesCount) {
        Map<String, Double> tokenToIdf = new HashMap<>();
        for (Map.Entry<String, Integer> fileCount : wordToFileCount.entrySet()) {
            tokenToIdf.put(fileCount.getKey(), Math.log(filesCount / (double) fileCount.getValue()));
        }
        return tokenToIdf;
    }

    private TfIdf buildTfidf(String fileName, String content, Map<String, String> tokenToLemma) {
        List<String> words = Arrays.stream(content.split(" ")).map(String::toLowerCase).toList();
        Set<String> tokens = tokenToLemma.keySet();
        Map<String, Long> tokenCount = new HashMap<>();
        Map<String, Long> lemmasCount = new HashMap<>();
        for (String word : words) {
            if (tokens.contains(word)) {
                tokenCount.put(word, tokenCount.getOrDefault(word, 0L) + 1);
                String lemmas = tokenToLemma.get(word);
                lemmasCount.put(lemmas, tokenCount.getOrDefault(lemmas, 0L) + 1);
            }
        }
        return new TfIdf(fileName, tokenCount, lemmasCount);
    }

    @SneakyThrows
    private void uploadTfidfForInFile(Path filePath, Map<String, Double> wordToTf, Map<String, Double> wordToIdf) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Map.Entry<String, Double> entry : wordToTf.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + " " + wordToIdf.get(entry.getKey()) + " " + entry.getValue() * wordToIdf.get(entry.getKey()) + "\n");
            }
        }
    }
}
