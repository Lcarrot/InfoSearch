package ru.tyshchenko.infosearch.lemmatizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.files.FileUploader;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class WordLemmatizer {
    @Value("${inner.files.output}")
    private String path;
    private final FileUploader fileUploader;
    private static final Pattern ENGLISH_WORDS_REGEX = Pattern.compile("[a-zA-Z]{3,}");
    public WordLemmatizer(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    public void lemmatize() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, pos, lemma");
        props.setProperty("tokenize.options", "untokenizable=allDelete");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(getTextFromHtml().toLowerCase());
        pipeline.annotate(doc);

        Map<String, Set<String>> map = new HashMap<>();
        for (CoreLabel token : doc.tokens().stream()
                .filter(token -> ENGLISH_WORDS_REGEX.matcher(token.word()).matches())
                .collect(Collectors.toSet())) {
            if(map.containsKey(token.lemma())) {
                map.get(token.lemma()).add(token.word());
            } else {
                map.put(token.lemma(), new HashSet<String>(Arrays.asList(token.word())) {
                });
            }
        }

        fileUploader.uploadLemmasInFile(map);
    }
    @SneakyThrows
    private String getTextFromHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Path> files = Files.walk(Paths.get(path + "pages"))
                .filter(Files::isRegularFile)
                .toList();
        for (Path file : files) {
            stringBuilder.append(ParserUtils.getSourceText(file));
        }
        return stringBuilder.toString();
    }
}
