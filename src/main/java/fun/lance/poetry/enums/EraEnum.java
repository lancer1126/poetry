package fun.lance.poetry.enums;

public enum EraEnum {
    WEI_ZHI("未知"),
    ZHOU("周"),
    SAN_GUO("三国"),
    CHUN_QIU("春秋战国"),
    TANG("唐"),
    WU_DAI("五代十国"),
    SONG("宋"),
    YUAN("元"),
    QING("清");

    private final String name;

    EraEnum(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
