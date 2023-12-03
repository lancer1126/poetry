package fun.lance.poetry.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Author {
    @TableId
    private Long authorId;
    private Long eraId;
    private String authorName;
    private String authorDescription;
}
