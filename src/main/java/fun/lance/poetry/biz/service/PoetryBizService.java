package fun.lance.poetry.biz.service;

import com.alibaba.fastjson2.JSON;
import fun.lance.poetry.biz.model.vo.PoetryVO;
import fun.lance.poetry.biz.model.vo.Recommend;
import fun.lance.poetry.common.CommonUtil;
import fun.lance.poetry.extractor.mapper.AuthorMapper;
import fun.lance.poetry.extractor.model.entity.Author;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoetryBizService {

    private final JdbcTemplate jdbcTemplate;
    private final AuthorMapper authorMapper;

    public List<Recommend> getRecommends() {
        List<Recommend> recommends = new ArrayList<>();
        try (ExecutorService virtualExec = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<Recommend> hourFuture = virtualExec.submit(() -> getRecommendByHour(LocalTime.now().getHour()));
            Future<Recommend> authorFuture = virtualExec.submit(() -> getRecommendByAuthor(authorMapper.getRandomAuthor()));

            recommends.add(hourFuture.get());
            recommends.add(authorFuture.get());
        } catch (Exception e) {
            log.error("获取推荐诗句错误");
            log.error("", e);
        }
        return recommends;
    }

    /**
     * 通过农历日期为关键字获取相关诗句
     */
    public Recommend getRecommendByHour(int hourInt) {
        String hourChar = CommonUtil.hourIntToChar(hourInt);
        if (hourChar == null) {
            hourChar = "缘";
        }
        Recommend recommend = new Recommend();
        recommend.setKeyword(hourChar);

        try {
            String hourEle = "'%" + hourChar + "%'";
            String querySql = "select e.era_name as eraName, a.author_name as authorName, p.anthology, p.chapter, p.section, p.content " +
                    " from poem p " +
                    "         inner join era e on e.era_id = p.era_id " +
                    "         inner join author a on a.author_id = p.author_id " +
                    " where p.content like " + hourEle +
                    " order by rand() limit 5;";
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(querySql);
            recommend.setPoetryList(JSON.parseArray(JSON.toJSONString(maps), PoetryVO.class));
        } catch (Exception e) {
            log.error("使用 {} 查询诗句错误", recommend.getKeyword());
            log.error("", e);
        }
        return recommend;
    }

    /**
     * 通过作者获取诗句
     */
    public Recommend getRecommendByAuthor(Author author) {
        Recommend recommend = new Recommend();
        recommend.setKeyword(author.getAuthorName());

        try {
            String querySql = "select e.era_name as eraName, a.author_name as authorName, p.anthology, p.chapter, p.section, p.content " +
                    " from poem p " +
                    "         inner join era e on e.era_id = p.era_id " +
                    "         inner join author a on a.author_id = p.author_id " +
                    " where p.author_id = ? order by rand() limit 5;";
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(querySql, author.getAuthorId());
            recommend.setPoetryList(JSON.parseArray(JSON.toJSONString(maps), PoetryVO.class));
        } catch (Exception e) {
            log.error("使用诗人 {} 查询诗句错误", author.getAuthorName());
            log.error("", e);
        }
        return recommend;
    }
}
