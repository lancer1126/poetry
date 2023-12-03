package fun.lance.poetry.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Era {
    @TableId
    private Long eraId;
    private String eraName;
    private String eraDescription;
}
