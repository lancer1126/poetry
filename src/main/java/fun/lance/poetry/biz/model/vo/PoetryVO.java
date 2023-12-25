package fun.lance.poetry.biz.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PoetryVO {
    private String eraName;
    private String authorName;
    private String anthology;
    private String chapter;
    private String section;
    private String poemName;
    private String content;
    private String originCharType;
    private List<PoetryContentVO> contentWithChar;
}
