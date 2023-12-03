package fun.lance.poetry.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.lance.poetry.enums.EraEnum;
import fun.lance.poetry.handler.PrepareHandler;
import fun.lance.poetry.mapper.AuthorMapper;
import fun.lance.poetry.mapper.PoemMapper;
import fun.lance.poetry.model.entity.Author;
import fun.lance.poetry.model.entity.Era;
import fun.lance.poetry.model.entity.Poem;
import fun.lance.poetry.service.IPoetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaocaoService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoemMapper poemMapper;

    @Override
    public String getName() {
        return "曹操诗集";
    }

    @Override
    public void readAndUpload(String path) {
        path += "caocao.json";
        String content = new FileReader(path).readString();

        Era era = prepareHandler.getOrCreateEra(EraEnum.SAN_GUO, null);
        Author author = prepareHandler.getOrCreateAuthor("曹操", era.getEraId(), null);

        JSONArray jsonArray = JSON.parseArray(content);
        for (int i = 0; i < jsonArray.size(); i++) {
            parseJson(jsonArray.getJSONObject(i), era, author);
        }
    }

    private void parseJson(JSONObject jsonObj, Era era, Author author) {
        String title = jsonObj.getString("title");
        JSONArray itemArr = jsonObj.getJSONArray("paragraphs");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itemArr.size(); i++) {
            sb.append(itemArr.getString(i)).append("\n");
        }
        log.info(sb.toString());

        Poem poem = new Poem();
        poem.setPoemName(title);
        poem.setContent(sb.toString());
        poem.setAuthorId(author.getAuthorId());
        poem.setEraId(era.getEraId());
        poemMapper.insert(poem);
    }
}
