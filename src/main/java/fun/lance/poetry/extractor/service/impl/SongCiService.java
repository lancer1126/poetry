package fun.lance.poetry.extractor.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongCiService extends ServiceImpl<PoemMapper, Poem> implements IPoetryService {

    private final Map<String, Long> authorMap = new HashMap<>();

    private final PrepareHandler prepareHandler;
    private final AuthorMapper authorMapper;
    private final EraMapper eraMapper;

    @Override
    public String getName() {
        return "宋词";
    }

    @Override
    public void readAndUpload(String path) {
        for (File e : FileUtil.ls(path)) {
            if (e.getName().startsWith("author.song")) {
                fillAuthor(e);
            }
        }
    }

    private void fillAuthor(File file) {
        String content = new FileReader(file).readString();
        Era era_tang = eraMapper.selectOne(new QueryWrapper<Era>()
                .eq("era_name", EraEnum.SONG.value()));

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

            author.setAuthorDescription(authorJson.getString("description"));
            authorMapper.updateById(author);
        }
    }

    private int readOne(Era era, File file) {
        String content = new FileReader(file).readString();
        List<Poem> poemList = new ArrayList<>();

        JSONArray poemArr = JSON.parseArray(content);
        for (int i = 0; i < poemArr.size(); i++) {
            JSONObject poemJson = poemArr.getJSONObject(i);
            String authorName = poemJson.getString("author");

            Long authorId = authorMap.get(authorName);
            if (authorId == null) {
                Author author = prepareHandler.getOrCreateAuthor(authorName, era.getEraId(), null);
                authorId = author.getAuthorId();
                authorMap.put(authorName, authorId);
            }

            StringBuilder sb = new StringBuilder();
            JSONArray paraArr = poemJson.getJSONArray("paragraphs");
            try {
                for (int paraIdx = 0; paraIdx < paraArr.size(); paraIdx++) {
                    sb.append(paraArr.getString(paraIdx)).append("\n");
                }
            } catch (Exception e) {
                log.error("", e);
                log.error("问题内容是 " + paraArr.toString());
            }

            Poem poem = new Poem();
            poem.setAuthorId(authorId);
            poem.setEraId(era.getEraId());
            poem.setAnthology("宋词");
            poem.setPoemName(poemJson.getString("rhythmic"));
            poem.setContent(sb.toString());
            poemList.add(poem);
        }
        saveBatch(poemList);
        return poemList.size();
    }
}
