package fun.lance.poetry.extractor.model.dto;

import com.alibaba.fastjson2.JSONObject;
import fun.lance.poetry.extractor.model.entity.Author;
import fun.lance.poetry.extractor.model.entity.Era;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PoetryLink {
    private Era era;
    private Author author;
    private JSONObject poetryJson;
    private String anthology;
    private String eraIndex;
    private String authorIndex;
    private String titleIndex;
    private String sectionIndex;
    private String contentIndex;
}
