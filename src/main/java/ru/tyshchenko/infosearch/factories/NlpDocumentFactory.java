package ru.tyshchenko.infosearch.factories;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.util.Properties;

@Component
public class NlpDocumentFactory {
    public CoreDocument getCoreDocument(String text) {
        Properties props = getProperties();
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(text.toLowerCase());
        pipeline.annotate(doc);

        return doc;
    }

    @SneakyThrows
    public Properties getProperties() {
        Properties props = new Properties();
        props.load(Files.newBufferedReader(ResourceUtils.getFile("classpath:nlp.properties").toPath()));
        return props;
    }
}
