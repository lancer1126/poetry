package fun.lance.poetry.common.constant;

public class SqlConst {
    public static final String POETRY_BASE = "select " +
            " e.era_name as eraName, a.author_name as authorName, p.anthology, p.chapter, p.section, p.poem_name as poemName, p.content, p.char_type as originCharType " +
            " from poem p " +
            " inner join era e on e.era_id = p.era_id " +
            " inner join author a on a.author_id = p.author_id ";
}
