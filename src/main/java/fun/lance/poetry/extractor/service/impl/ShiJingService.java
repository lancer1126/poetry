package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import fun.lance.poetry.common.enums.EraEnum;
import fun.lance.poetry.extractor.model.dto.PoetryLink;
import fun.lance.poetry.extractor.model.entity.Author;
import fun.lance.poetry.extractor.model.entity.Era;
import fun.lance.poetry.extractor.service.IPoetryService;
import fun.lance.poetry.handler.PoetryExtractHandler;
import fun.lance.poetry.handler.PrepareHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShiJingService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoetryExtractHandler poetryExtractHandler;

    @Override
    public String getName() {
        return "诗经";
    }

    @Override
    public void readAndUpload(String path) {
        path += "shijing.json";
        String content = new FileReader(path).readString();

        Era era = prepareHandler.getOrCreateEra(EraEnum.ZHOU, null);
        Author author = prepareHandler.getOrCreateAuthor("", era.getEraId(), null);

        JSONArray jsonArray = JSON.parseArray(content);
        for (int i = 0; i < jsonArray.size(); i++) {
            PoetryLink link = new PoetryLink()
                    .setPoetryJson(jsonArray.getJSONObject(i))
                    .setEra(era)
                    .setAuthor(author)
                    .setAnthology("诗经")
                    .setTitleIndex("title")
                    .setChapterIndex("chapter")
                    .setSectionIndex("section")
                    .setContentIndex("content");
            poetryExtractHandler.commonExtract(link);
        }
    }
}
