package ru.tyshchenko.infosearch.factories;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class NlpDocumentFactory {
    public CoreDocument getCoreDocument(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, pos, lemma");
        props.setProperty("tokenize.options", "untokenizable=allDelete");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(text.toLowerCase());
        pipeline.annotate(doc);

        return doc;
    }
}
