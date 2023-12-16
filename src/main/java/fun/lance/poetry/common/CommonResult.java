package fun.lance.poetry.common;

import fun.lance.poetry.common.enums.RespEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResult<T> implements Serializable {

    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    public CommonResult() {}

    public CommonResult(Boolean success, RespEnum respEnum, String msg, T data) {
        this.success = success;
        this.code = respEnum.code();
        this.msg = msg;
        this.data = data;
    }

    public CommonResult(Boolean success, RespEnum respEnum, T data) {
        this.success = success;
        this.code = respEnum.code();
        this.msg = respEnum.msg();
        this.data = data;
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(true, RespEnum.SUCCESS, null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(true, RespEnum.SUCCESS, data);
    }

    public static <T> CommonResult<T> success(String msg, T data) {
        return new CommonResult<>(true, RespEnum.SUCCESS, msg, data);
    }

    public static <T> CommonResult<T> fail() {
        return new CommonResult<>(true, RespEnum.FAIL, null);
    }

    public static <T> CommonResult<T> fail(T data) {
        return new CommonResult<>(true, RespEnum.FAIL, data);
    }

    public static <T> CommonResult<T> fail(String msg, T data) {
        return new CommonResult<>(true, RespEnum.FAIL, msg, data);
    }
}
