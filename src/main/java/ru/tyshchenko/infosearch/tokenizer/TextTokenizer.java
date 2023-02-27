package ru.tyshchenko.infosearch.tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tyshchenko.infosearch.files.FileUploader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import ru.tyshchenko.infosearch.utils.ParserUtils;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class TextTokenizer {
    @Value("${inner.files.output}")
    private String path;
    private final FileUploader fileUploader;
    private static final Pattern ENGLISH_WORDS_REGEX = Pattern.compile("[a-zA-Z]{3,}");
    public TextTokenizer(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    public void tokenize() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize");
        props.setProperty("tokenize.options", "untokenizable=allDelete");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(getTextFromHtml().toLowerCase());
        pipeline.annotate(doc);

        fileUploader.uploadTokensInFile(doc.tokens().stream().map(CoreLabel::word)
                .filter(word -> ENGLISH_WORDS_REGEX.matcher(word).matches())
                .collect(Collectors.toSet()));
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
