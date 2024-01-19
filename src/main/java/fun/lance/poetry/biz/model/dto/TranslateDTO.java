package fun.lance.poetry.biz.model.dto;

import lombok.Data;

@Data
public class TranslateDTO {
    private Long poemId;
    private String targetLang;
}
