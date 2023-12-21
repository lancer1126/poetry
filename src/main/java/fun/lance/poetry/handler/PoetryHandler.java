package fun.lance.poetry.handler;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import fun.lance.poetry.factory.PoetryBeanFactory;
import fun.lance.poetry.extractor.service.IPoetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PoetryHandler {

    @Value("${poetry.path}")
    private String poetryFilePath;
    @Value("${meilisearch.index-name}")
    private String meiliIndex;

    private final PoetryBeanFactory poetryBeanFactory;
    private final JdbcTemplate jdbcTemplate;
    private final Client meiliClient;

    public void readAllPoetry() {
        for (File file : FileUtil.ls(poetryFilePath)) {
            if (!file.isDirectory()) continue;

            String dirName = file.getName();
            IPoetryService poetryService = poetryBeanFactory.getService(dirName);
            if (poetryService != null) {
                log.info("读取文件夹 - {}", dirName);
                poetryService.readAndUpload(poetryFilePath + "//" + dirName + "//");
            }
        }
    }

    public void readByNames(List<String> dirNames) {
        for (String dirName : dirNames) {
            readByName(dirName);
        }
    }

    public void readByName(String dirName) {
        IPoetryService poetryService = poetryBeanFactory.getService(dirName);
        if (poetryService == null) {
            throw new RuntimeException(dirName + " 的实现类不存在");
        }
        log.info("----------文件夹 {} 开始读取----------", dirName);
        String dirPath = poetryFilePath + "//" + dirName + "//";
        poetryService.readAndUpload(dirPath);
        log.info("----------文件夹 {} 读取完成----------", dirName);
    }

    /**
     * 上传数据到meilisearch
     */
    public void uploadToMeili() {
        Index poetryIndex;
        try {
            poetryIndex = meiliClient.index(meiliIndex);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException("create meilisearch index error");
        }

        int limit = 1000;
        Map<String, Object> countMap = jdbcTemplate.queryForMap("select count(*) as cnt from poetry.poem");
        long dataCount = (long) countMap.get("cnt");
        long sequence = Long.parseLong(new BigDecimal(dataCount).divide(new BigDecimal(limit), 0, RoundingMode.HALF_UP).toPlainString());

        int offset, uploadCount = 0;
        for (int i = 0; i <= sequence; i++) {
            offset = i * limit;
            String querySql = "select p.poem_id,e.era_name,a.author_name,p.anthology,p.chapter,p.section,p.poem_name,p.content,p.char_type " +
                    " from poem p " +
                    "     inner join era e on e.era_id = p.era_id " +
                    "     inner join author a on a.author_id = p.author_id " +
                    " order by p.poem_id limit ?,?";
            List<Map<String, Object>> pageMap = jdbcTemplate.queryForList(querySql, offset, limit);
            String poemStr = JSON.toJSONString(pageMap);

            try {
                boolean uploadSuccess = toMeili(poetryIndex, poemStr);
                // 若是上传失败的话重试10次
                if (!uploadSuccess) {
                    for (int retry = 0; retry < 10; retry++) {
                        log.info("批次 {} 重新上传...", i);
                        Thread.sleep(10000);
                        boolean retryRes = toMeili(poetryIndex, poemStr);
                        if (retryRes) {
                            uploadSuccess = true;
                            break;
                        }
                    }
                }

                if (uploadSuccess) {
                    uploadCount += pageMap.size();
                    log.info("批次 {} 上传完成", i);
                } else {
                    log.error("批次 offset: {}, limit: {} 处理数据错误", offset, limit);
                }

                if (i % 50 == 0) {
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("批次 offset: {}, limit: {} 处理数据错误", offset, limit);
            }
        }
        log.info("上传meilisearch完成，共 {}", uploadCount);
    }

    private boolean toMeili(Index poetryIndex, String poemStr) {
        try {
            poetryIndex.addDocuments(poemStr, "poem_id");
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
