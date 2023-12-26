package fun.lance.poetry.biz.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageItem<T> {
    private long total;
    private long offset;
    private long size;
    private List<T> records;
}
