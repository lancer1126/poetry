package fun.lance.poetry.common.enums;

public enum RespEnum {
    SUCCESS(200, "success"),
    FAIL(400, "fail"),
    ;

    private final Integer code;
    private final String msg;

    RespEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "RespEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
