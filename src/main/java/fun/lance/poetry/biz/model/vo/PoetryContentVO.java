package fun.lance.poetry.biz.model.vo;

import fun.lance.poetry.common.enums.PoemChar;
import lombok.Data;

@Data
public class PoetryContentVO {
    private String body;
    private String charType;

    public PoetryContentVO() {}

    public PoetryContentVO(String body, PoemChar poemChar) {
        this.body = body;
        this.charType = poemChar.value();
    }
}
