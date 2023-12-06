package fun.lance.poetry.extractor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.lance.poetry.extractor.model.entity.Poem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PoemMapper extends BaseMapper<Poem> {
}
