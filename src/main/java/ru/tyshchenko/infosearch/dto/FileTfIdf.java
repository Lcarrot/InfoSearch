package ru.tyshchenko.infosearch.dto;

import lombok.*;

import java.util.Map;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileTfIdf {
    private String fileName;
    private Map<String, Double> tokenTfIdf;
    private Map<String, Double> lemmaTfIdf;
}
