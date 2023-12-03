package fun.lance.poetry;

import fun.lance.poetry.handler.PoetryHandler;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PoetryApplicationTests {

    @Resource
    private PoetryHandler poetryHandler;

    @Test
    void contextLoads() {
        poetryHandler.readByName("曹操诗集");
    }

}
