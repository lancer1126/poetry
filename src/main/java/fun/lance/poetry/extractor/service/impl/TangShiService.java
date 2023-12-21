package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import fun.lance.poetry.common.enums.EraEnum;
import fun.lance.poetry.extractor.mapper.AuthorMapper;
import fun.lance.poetry.extractor.mapper.EraMapper;
import fun.lance.poetry.extractor.mapper.PoemMapper;
import fun.lance.poetry.extractor.model.entity.Author;
import fun.lance.poetry.extractor.model.entity.Era;
import fun.lance.poetry.extractor.model.entity.Poem;
import fun.lance.poetry.extractor.service.IPoetryService;
import fun.lance.poetry.handler.PrepareHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TangShiService extends ServiceImpl<PoemMapper, Poem> implements IPoetryService {

    private final Map<String, Long> authorMap = new HashMap<>();

    private final PrepareHandler prepareHandler;
    private final AuthorMapper authorMapper;
    private final EraMapper eraMapper;

    @Override
    public String getName() {
        return "御定全唐诗";
    }

    @Override
    public void readAndUpload(String path) {
        long total = 0;
        Era era = prepareHandler.getOrCreateEra(EraEnum.SONG, null);
        for (File e : FileUtil.ls(path)) {
            if (e.getName().endsWith(".json")) {
                log.info("读取文件 {}", e.getName());
                int count = readOne(era, e);
                total += count;
                log.info("处理了 {} ", total);
                log.info("------------------------------");
            }
        }
    }

    private void fillAuthor(File file) {
        String content = new FileReader(file).readString();
        Era era_tang = eraMapper.selectOne(new QueryWrapper<Era>()
                .eq("era_name", EraEnum.TANG.value()));

        JSONArray authorArr = JSON.parseArray(content);
        for (int i = 0; i < authorArr.size(); i++) {
            JSONObject authorJson = authorArr.getJSONObject(i);

            Author author = authorMapper.selectOne(new QueryWrapper<Author>()
                    .eq("era_id", era_tang.getEraId())
                    .eq("author_name", authorJson.getString("name")));
            if (author == null) {
                log.error("未找到作者：{}", authorJson.getString("name"));
                continue;
            }

            author.setAuthorDescription(authorJson.getString("desc"));
            authorMapper.updateById(author);
        }
    }

    private int readOne(Era era, File file) {
        String content = new FileReader(file).readString();
        List<Poem> poemList = new ArrayList<>();

        JSONArray poemArr = JSON.parseArray(content);
        for (int i = 0; i < poemArr.size(); i++) {
            JSONObject poemJson = poemArr.getJSONObject(i);
            if (poemJson.isEmpty()) {
                continue;
            }

            String authorName = ZhConverterUtil.toSimple(poemJson.getString("author"));
            Long authorId = authorMap.get(authorName);
            if (authorId == null) {
                Author author = prepareHandler.getOrCreateAuthor(authorName, era.getEraId(), null);
                authorId = author.getAuthorId();
                authorMap.put(authorName, authorId);
            }

            StringBuilder sb = new StringBuilder();
            try {
                JSONArray paraArr = poemJson.getJSONArray("paragraphs");
                for (int paraIdx = 0; paraIdx < paraArr.size(); paraIdx++) {
                    sb.append(paraArr.getString(paraIdx)).append("\n");
                }

                Poem poem = new Poem();
                poem.setAuthorId(authorId);
                poem.setEraId(era.getEraId());
                poem.setAnthology(getName());
                poem.setPoemName(poemJson.getString("title"));
                poem.setContent(sb.toString());
                poemList.add(poem);
            } catch (Exception e) {
                log.error("", e);
                log.error("问题内容是 {}", poemJson);
            }
        }
        saveBatch(poemList);
        return poemList.size();
    }
}
