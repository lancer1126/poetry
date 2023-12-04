package fun.lance.poetry.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import fun.lance.poetry.enums.EraEnum;
import fun.lance.poetry.handler.PoetryExtractHandler;
import fun.lance.poetry.handler.PrepareHandler;
import fun.lance.poetry.model.dto.PoetryLink;
import fun.lance.poetry.model.entity.Author;
import fun.lance.poetry.model.entity.Era;
import fun.lance.poetry.service.IPoetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LunYuService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoetryExtractHandler poetryExtractHandler;

    @Override
    public String getName() {
        return "论语";
    }

    @Override
    public void readAndUpload(String path) {
        path += "lunyu.json";
        String content = new FileReader(path).readString();

        Era era = prepareHandler.getOrCreateEra(EraEnum.CHUN_QIU, null);
        Author author = prepareHandler.getOrCreateAuthor("待完善", era.getEraId(), null);

        JSONArray jsonArray = JSON.parseArray(content);
        for (int i = 0; i < jsonArray.size(); i++) {
            PoetryLink link = new PoetryLink()
                    .setPoetryJson(jsonArray.getJSONObject(i))
                    .setEra(era)
                    .setAuthor(author)
                    .setAnthology("论语")
                    .setTitleIndex("chapter")
                    .setContentIndex("paragraphs");
            poetryExtractHandler.commonExtract(link);
        }
    }
}
