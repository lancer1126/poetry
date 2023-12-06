package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import fun.lance.poetry.enums.EraEnum;
import fun.lance.poetry.extractor.model.dto.PoetryLink;
import fun.lance.poetry.extractor.model.entity.Era;
import fun.lance.poetry.extractor.service.IPoetryService;
import fun.lance.poetry.handler.PoetryExtractHandler;
import fun.lance.poetry.handler.PrepareHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class MengXueService implements IPoetryService {

    private final PrepareHandler prepareHandler;
    private final PoetryExtractHandler poetryExtractHandler;

    @Override
    public String getName() {
        return "蒙学";
    }

    @Override
    public void readAndUpload(String path) {
        File[] files = FileUtil.ls(path);
        for (File file : files) {
            String jsonFile = path + file.getName();
            String content = new FileReader(jsonFile).readString();

            Era era = prepareHandler.getOrCreateEra(EraEnum.BEI_SONG, null);
            JSONArray jsonArray = JSON.parseArray(content);
            for (int i = 0; i < jsonArray.size(); i++) {
                PoetryLink link = new PoetryLink()
                        .setPoetryJson(jsonArray.getJSONObject(i))
                        .setEra(era)
                        .setAuthorIndex("author")
                        .setAnthology("蒙学")
                        .setTitleIndex("title")
                        .setContentIndex("paragraphs");
                poetryExtractHandler.commonExtract(link);
            }
        }
    }
}
