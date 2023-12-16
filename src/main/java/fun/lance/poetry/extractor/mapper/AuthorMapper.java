package fun.lance.poetry.extractor.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.lance.poetry.extractor.model.entity.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthorMapper extends BaseMapper<Author> {

    @Select("select * from author order by rand() limit 1")
    Author getRandomAuthor();

}
