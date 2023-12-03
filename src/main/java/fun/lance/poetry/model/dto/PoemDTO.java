package fun.lance.poetry.model.dto;

import lombok.Data;

@Data
public class PoemDTO {
    private Long poemId;
    private String poemName;
    private String authorName;
    private String eraName;
    private String content;
}
