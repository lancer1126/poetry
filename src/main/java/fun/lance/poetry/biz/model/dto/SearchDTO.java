package fun.lance.poetry.biz.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchDTO {
    private String word;
    private Integer index;
    private Integer size;
}
