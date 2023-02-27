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
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WordLemmatizer {
    @Value("${inner.files.output}")
    private String path;
    private final FileUploader fileUploader;
    private final NlpDocumentFactory nlpDocumentFactory;

    public void lemmatize() {
        Map<String, Set<String>> map = new HashMap<>();
        Set<CoreLabel> labels = nlpDocumentFactory.getCoreDocument(
                        ParserUtils.getSourceTextFromFiles(Path.of(path, "pages")))
                .tokens().stream()
                .filter(token -> RegexUtils.ENGLISH_WORDS_REGEX.matcher(token.word()).matches())
                .collect(Collectors.toSet());

        for (CoreLabel token : labels) {
            map.computeIfAbsent(token.lemma(), k -> new HashSet<>()).add(token.word());
        }

        fileUploader.uploadLemmasInFile(map);
    }
}
