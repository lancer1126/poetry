package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import fun.lance.poetry.common.enums.EraEnum;
import fun.lance.poetry.handler.PrepareHandler;
import fun.lance.poetry.extractor.mapper.PoemMapper;
import fun.lance.poetry.extractor.model.entity.Author;
import fun.lance.poetry.extractor.model.entity.Era;
import fun.lance.poetry.extractor.model.entity.Poem;
import fun.lance.poetry.extractor.service.IPoetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChuCiService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoemMapper poemMapper;

    @Override
    public String getName() {
        return "楚辞";
    }

    @Override
    public void readAndUpload(String path) {
        path += "chuci.json";
        String content = new FileReader(path).readString();

        Era era = prepareHandler.getOrCreateEra(EraEnum.CHUN_QIU, null);

        JSONArray jsonArray = JSON.parseArray(content);
        for (int i = 0; i < jsonArray.size(); i++) {
            parseJson(jsonArray.getJSONObject(i), era);
        }
    }

    private void parseJson(JSONObject jsonObj, Era era) {
        String title = jsonObj.getString("title");
        String section = jsonObj.getString("section");
        Author author = prepareHandler.getOrCreateAuthor(jsonObj.getString("author"), era.getEraId(), null);

        JSONArray itemArr = jsonObj.getJSONArray("content");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itemArr.size(); i++) {
            sb.append(itemArr.getString(i)).append("\n");
        }

        Poem poem = new Poem();
        poem.setPoemName(title);
        poem.setEraId(era.getEraId());
        poem.setAuthorId(author.getAuthorId());
        poem.setSection(section);
        poem.setContent(sb.toString());
        poemMapper.insert(poem);
    }
}
