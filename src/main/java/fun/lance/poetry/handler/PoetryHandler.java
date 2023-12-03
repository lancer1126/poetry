package fun.lance.poetry.handler;

import cn.hutool.core.io.FileUtil;
import fun.lance.poetry.factory.PoetryBeanFactory;
import fun.lance.poetry.service.IPoetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class PoetryHandler {

    @Value("${poetry.path}")
    private String poetryFilePath;

    private final PoetryBeanFactory poetryBeanFactory;

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

    public void readByName(String dirName) {
        IPoetryService poetryService = poetryBeanFactory.getService(dirName);
        if (poetryService == null) {
            throw new RuntimeException(dirName + " 的实现类不存在");
        }
        log.info("读取单个文件夹 - {}", dirName);
        String dirPath = poetryFilePath + "//" + dirName + "//";
        poetryService.readAndUpload(dirPath);
    }
}
