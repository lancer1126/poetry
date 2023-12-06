package fun.lance.poetry.handler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import fun.lance.poetry.enums.EraEnum;
import fun.lance.poetry.extractor.mapper.PoemMapper;
import fun.lance.poetry.extractor.model.dto.PoetryLink;
import fun.lance.poetry.extractor.model.entity.Author;
import fun.lance.poetry.extractor.model.entity.Era;
import fun.lance.poetry.extractor.model.entity.Poem;
import fun.lance.poetry.util.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PoetryExtractHandler {

    private final PrepareHandler prepareHandler;
    private final PoemMapper poemMapper;

    public int commonExtract(PoetryLink link) {
        if (CheckUtil.containsNull(link.getPoetryJson(), link.getTitleIndex(), link.getContentIndex())) {
            throw new RuntimeException("poetryJson is null");
        }

        JSONObject poetryJson = link.getPoetryJson();
        String title = poetryJson.getString(link.getTitleIndex());
        JSONArray itemArr = poetryJson.getJSONArray(link.getContentIndex());

        Era era = link.getEra();
        if (era == null) {
            if (link.getEraIndex() == null) {
                throw new RuntimeException("Era is null");
            }
            String eraName = poetryJson.getString(link.getEraIndex());
            era = prepareHandler.getOrCreateEra(matchEra(eraName), null);
        }

        Author author = link.getAuthor();
        if (author == null) {
            if (link.getAuthorIndex() == null) {
                throw new RuntimeException("Author is null");
            }
            String authorName = poetryJson.getString(link.getAuthorIndex());
            author = prepareHandler.getOrCreateAuthor(authorName, era.getEraId(), null);
        }

        String sectionStr = null;
        if (link.getSectionIndex() != null) {
            sectionStr = poetryJson.getString(link.getSectionIndex());
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itemArr.size(); i++) {
            sb.append(itemArr.getString(i)).append("\n");
        }

        Poem poem = new Poem();
        poem.setPoemName(title);
        poem.setAuthorId(author.getAuthorId());
        poem.setEraId(era.getEraId());
        poem.setAnthology(link.getAnthology());
        poem.setSection(sectionStr);
        poem.setContent(sb.toString());
        return poemMapper.insert(poem);
    }

    private EraEnum matchEra(String eraName) {
        for (EraEnum eraEnum : EraEnum.values()) {
            if (eraEnum.value().equalsIgnoreCase(eraName)) {
                return eraEnum;
            }
        }
        return EraEnum.WEI_ZHI;
    }

}
