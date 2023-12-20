package fun.lance.poetry.common.enums;

public enum PoemChar {

    ZH_SIMP("zh-simplified"),
    ZH_TRAD("zh-traditional"),
    ;

    private final String poemChar;

    PoemChar(String poemChar) {
        this.poemChar = poemChar;
    }

    public String value() {
        return this.poemChar;
    }

}
