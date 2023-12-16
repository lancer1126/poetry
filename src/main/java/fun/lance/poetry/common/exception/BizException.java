package fun.lance.poetry.common.exception;

import fun.lance.poetry.common.enums.RespEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private Integer code;

    public BizException(String msg) {
        super(msg);
    }

    public BizException(RespEnum respEnum) {
        super(respEnum.msg());
        this.code = respEnum.code();
    }

    public BizException(RespEnum respEnum, String msg) {
        super(msg);
        this.code = respEnum.code();
    }
}
