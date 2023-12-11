package fun.lance.poetry.extractor.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Poem {
    @TableId
    private Long poemId;
    private Long authorId;
    private Long eraId;
    private String poemName;
    private String anthology;
    private String chapter;
    private String section;
    private String content;
}
