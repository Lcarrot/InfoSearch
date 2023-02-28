package ru.tyshchenko.infosearch.nlp.tools;

import edu.stanford.nlp.ling.CoreLabel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.factories.NlpDocumentFactory;
import ru.tyshchenko.infosearch.files.FileUploader;
import ru.tyshchenko.infosearch.utils.ParserUtils;
import ru.tyshchenko.infosearch.utils.RegexUtils;

import java.nio.file.Path;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TextTokenizer {
    @Value("${inner.files.output}")
    private String path;
    private final FileUploader fileUploader;
    private final NlpDocumentFactory nlpDocumentFactory;

    public void tokenize() {
        fileUploader.uploadTokensInFile(nlpDocumentFactory.getCoreDocument(
                        ParserUtils.getSourceTextFromFiles(Path.of(path, "pages")))
                .tokens().stream()
                .map(CoreLabel::word)
                .filter(word -> RegexUtils.ENGLISH_WORDS_REGEX.matcher(word).matches())
                .collect(Collectors.toSet()));
    }
}
