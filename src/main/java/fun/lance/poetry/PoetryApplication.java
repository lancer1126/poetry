package fun.lance.poetry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "fun.lance.poetry.mapper")
public class PoetryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PoetryApplication.class, args);
    }
}
