package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
public class CommonService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoetryExtractHandler poetryExtractHandler;

    @Override
    public String getName() {
        return "四书五经";
    }

    @Override
    public void readAndUpload(String path) {
        path += "zhongyong.json";
        String content = new FileReader(path).readString();

        Era era = prepareHandler.getOrCreateEra(EraEnum.CHUN_QIU, null);
        Author author = prepareHandler.getOrCreateAuthor("子思", era.getEraId(), null);


        JSONObject jsonObject = JSON.parseObject(content);
        PoetryLink link = new PoetryLink()
                .setPoetryJson(jsonObject)
                .setEra(era)
                .setAuthor(author)
                .setAnthology(getName())
                .setTitleIndex("chapter")
                .setContentIndex("paragraphs");
        poetryExtractHandler.commonExtract(link);
    }
}
