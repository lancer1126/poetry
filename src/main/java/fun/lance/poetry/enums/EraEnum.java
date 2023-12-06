package fun.lance.poetry.enums;

public enum EraEnum {
    WEI_ZHI("未知"),
    SAN_GUO("三国"),
    CHUN_QIU("春秋战国"),
    TANG("唐"),
    BEI_SONG("北宋"),
    QING("清");

    private final String name;

    EraEnum(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
