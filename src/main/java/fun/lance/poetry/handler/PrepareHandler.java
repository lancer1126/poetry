package fun.lance.poetry.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import fun.lance.poetry.enums.EraEnum;
import fun.lance.poetry.mapper.AuthorMapper;
import fun.lance.poetry.mapper.EraMapper;
import fun.lance.poetry.model.entity.Author;
import fun.lance.poetry.model.entity.Era;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrepareHandler {

    private final EraMapper eraMapper;
    private final AuthorMapper authorMapper;

    public Era getOrCreateEra(@NonNull EraEnum eraEnum, String description) {
        Era era = eraMapper.selectOne(new QueryWrapper<Era>()
                .eq("era_name", eraEnum.value()));
        if (era != null) {
            return era;
        }

        era = new Era();
        era.setEraId(IdWorker.getId());
        era.setEraName(eraEnum.value());
        era.setEraDescription(description);
        eraMapper.insert(era);

        return era;
    }

    public Author getOrCreateAuthor(@NonNull String authorName, @NonNull Long eraId, String description) {
        Author author = authorMapper.selectOne(new QueryWrapper<Author>()
                .eq("author_name", authorName)
                .eq("era_id", eraId));
        if (author != null) {
            return author;
        }

        author = new Author();
        author.setAuthorId(IdWorker.getId());
        author.setAuthorName(authorName);
        author.setEraId(eraId);
        author.setAuthorDescription(description);
        authorMapper.insert(author);

        return author;
    }

}
