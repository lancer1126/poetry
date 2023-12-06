package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import fun.lance.poetry.enums.EraEnum;
import fun.lance.poetry.extractor.model.dto.PoetryLink;
import fun.lance.poetry.extractor.model.entity.Author;
import fun.lance.poetry.extractor.model.entity.Era;
import fun.lance.poetry.extractor.service.IPoetryService;
import fun.lance.poetry.handler.PoetryExtractHandler;
import fun.lance.poetry.handler.PrepareHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaLanService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoetryExtractHandler poetryExtractHandler;

    @Override
    public String getName() {
        return "纳兰性德";
    }

    @Override
    public void readAndUpload(String path) {
        path += "纳兰性德诗集.json";
        String content = new FileReader(path).readString();

        Era era = prepareHandler.getOrCreateEra(EraEnum.QING, null);
        Author author = prepareHandler.getOrCreateAuthor("纳兰性德", era.getEraId(), null);

        JSONArray jsonArray = JSON.parseArray(content);
        for (int i = 0; i < jsonArray.size(); i++) {
            PoetryLink link = new PoetryLink()
                    .setPoetryJson(jsonArray.getJSONObject(i))
                    .setEra(era)
                    .setAuthor(author)
                    .setAnthology("纳兰性德诗集")
                    .setTitleIndex("title")
                    .setContentIndex("para");
            poetryExtractHandler.commonExtract(link);
        }
    }
}
