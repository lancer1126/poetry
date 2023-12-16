package fun.lance.poetry.biz.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Recommend {
    private String keyword;
    private List<PoetryVO> poetryList;
}
