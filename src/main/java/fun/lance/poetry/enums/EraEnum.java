package fun.lance.poetry.enums;

public enum EraEnum {
    SAN_GUO("三国");

    private final String name;

    EraEnum(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
