package fun.lance.poetry.biz.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import fun.lance.poetry.biz.model.dto.SearchDTO;
import fun.lance.poetry.biz.model.vo.PoetryContentVO;
import fun.lance.poetry.biz.model.vo.PoetryVO;
import fun.lance.poetry.biz.model.vo.Recommend;
import fun.lance.poetry.common.CommonUtil;
import fun.lance.poetry.common.constant.SqlConst;
import fun.lance.poetry.common.enums.PoemChar;
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

    private final static int RECOMMEND_SIZE = 1;

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
            String querySql = SqlConst.POETRY_BASE +
                    " where p.content like " + hourEle + " order by rand() limit " + RECOMMEND_SIZE;
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(querySql);
            List<PoetryVO> poetryVOList = JSON.parseArray(JSON.toJSONString(maps), PoetryVO.class);
            // 为结果封装多语言（简体/繁体）
            poetryVOList.forEach(this::buildTwoFactors);
            recommend.setPoetryList(poetryVOList);
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
            String querySql = SqlConst.POETRY_BASE +
                    " where p.author_id = ? order by rand() limit " + RECOMMEND_SIZE;
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(querySql, author.getAuthorId());
            List<PoetryVO> poetryVOList = JSON.parseArray(JSON.toJSONString(maps), PoetryVO.class);
            // 为结果封装多语言（简体/繁体）
            poetryVOList.forEach(this::buildTwoFactors);
            recommend.setPoetryList(poetryVOList);
        } catch (Exception e) {
            log.error("使用诗人 {} 查询诗句错误", author.getAuthorName());
            log.error("", e);
        }
        return recommend;
    }

    public Page<PoetryVO> search(SearchDTO searchDTO) {
        return null;
    }

    /**
     * 为结果添加简体和繁体两种语言
     */
    public void buildTwoFactors(PoetryVO poetryVO) {
        if (StrUtil.isEmpty(poetryVO.getContent())) {
            return;
        }
        if (poetryVO.getOriginCharType() == null) {
            poetryVO.setOriginCharType(PoemChar.ZH_SIMP.value());
        }

        String originChar = poetryVO.getOriginCharType();
        PoetryContentVO simple = new PoetryContentVO();
        PoetryContentVO traditional = new PoetryContentVO();
        if (originChar.equals(PoemChar.ZH_SIMP.value())) {
            simple = new PoetryContentVO(poetryVO.getContent(), PoemChar.ZH_SIMP);
            traditional = new PoetryContentVO(ZhConverterUtil.toTraditional(poetryVO.getContent()), PoemChar.ZH_TRAD);
        } else if (originChar.equals(PoemChar.ZH_TRAD.value())) {
           simple = new PoetryContentVO(ZhConverterUtil.toSimple(poetryVO.getContent()), PoemChar.ZH_SIMP);
           traditional = new PoetryContentVO(poetryVO.getContent(), PoemChar.ZH_TRAD);
        }

        poetryVO.setContentWithChar(ListUtil.toList(simple, traditional));
    }
}
