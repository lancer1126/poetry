package fun.lance.poetry.enums;

public enum EraEnum {
    WEI_ZHI("未知"),
    SAN_GUO("三国"),
    CHUN_QIU("春秋战国");

    private final String name;

    EraEnum(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
