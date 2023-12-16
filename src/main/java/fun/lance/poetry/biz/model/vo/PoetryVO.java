package fun.lance.poetry.biz.model.vo;

import lombok.Data;

@Data
public class PoetryVO {
    private String eraName;
    private String authorName;
    private String anthology;
    private String chapter;
    private String section;
    private String content;
}
