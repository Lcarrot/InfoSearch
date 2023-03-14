package ru.tyshchenko.infosearch.dto;

import lombok.*;

import java.util.Map;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TfIdf {
    private String fileName;
    private Map<String, Long> tokenToCount;
    private Map<String, Long> lemmaToCount;
}
