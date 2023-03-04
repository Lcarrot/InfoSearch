package ru.tyshchenko.infosearch.nlp.tools;

import edu.stanford.nlp.ling.CoreLabel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.factories.NlpDocumentFactory;
import ru.tyshchenko.infosearch.files.FileUploader;
import ru.tyshchenko.infosearch.utils.ParserUtils;
import ru.tyshchenko.infosearch.utils.RegexUtils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WordLemmatizer {

    private static final String FILE_NAME = "lemmas.txt";
    @Value("${inner.files.output}")
    private String path;
    private final FileUploader fileUploader;
    private final NlpDocumentFactory nlpDocumentFactory;

    public void lemmatize() {
        Map<String, Set<String>> map = new HashMap<>();
        Set<CoreLabel> labels = nlpDocumentFactory.getCoreDocument(
                        ParserUtils.getSourceTextFromFilesAsString(Path.of(path, "pages")))
                .tokens().stream()
                .filter(token -> RegexUtils.ENGLISH_WORDS_REGEX.matcher(token.word()).matches())
                .collect(Collectors.toSet());

        for (CoreLabel token : labels) {
            map.computeIfAbsent(token.lemma(), k -> new HashSet<>()).add(token.word());
        }
        uploadLemmasInFile(map);
    }

    @SneakyThrows
    private void uploadLemmasInFile(Map<String, Set<String>> lemmas) {
        Path filePath = fileUploader.getFilePath(FILE_NAME);
        var result = new StringBuilder();
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Map.Entry<String, Set<String>> entry : lemmas.entrySet()) {
                String lemma = entry.getKey();
                Set<String> tokens = entry.getValue();
                tokens.forEach(token -> result.append(token).append(" "));
                writer.write(lemma + " : " + result + "\n");
                result.setLength(0);
            }
        }
    }

    @SneakyThrows
    public Map<String, Set<String>> getTokensByLemmas() {
        return Files.readAllLines(Path.of(path, FILE_NAME)).stream().map(line -> line.split(" : "))
                .collect(Collectors.toMap(line -> line[0].trim(),
                        line -> Arrays.stream(line[1].split(" "))
                                .map(String::trim).collect(Collectors.toSet())));
    }
}
