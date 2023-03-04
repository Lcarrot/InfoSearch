package ru.tyshchenko.infosearch.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvertedIndex {
    private String lemma;
    private Map<String, Set<String>> tokenToDocId = new HashMap<>();
}
