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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TextTokenizer {
    @Value("${inner.files.output}")
    private String path;
    private final FileUploader fileUploader;
    private final NlpDocumentFactory nlpDocumentFactory;

    public void tokenize() {
        uploadTokensInFile(nlpDocumentFactory.getCoreDocument(
                        ParserUtils.getSourceTextFromFilesAsString(Path.of(path, "pages")))
                .tokens().stream()
                .map(CoreLabel::word)
                .filter(word -> RegexUtils.ENGLISH_WORDS_REGEX.matcher(word).matches())
                .collect(Collectors.toSet()));
    }

    @SneakyThrows
    public void uploadTokensInFile(Set<String> tokens) {
        Path filePath = fileUploader.getFilePath("tokens.txt");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath.toFile())))) {
            for (String token: tokens) {
                writer.write(token + "\n");
            }
        }
    }
}
