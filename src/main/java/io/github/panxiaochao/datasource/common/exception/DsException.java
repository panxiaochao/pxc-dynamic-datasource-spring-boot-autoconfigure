package io.github.panxiaochao.datasource.common.exception;

/**
 * {@code DsException}
 * <p> 多数据源自定义错误
 *
 * @author Lypxc
 * @since 2022/1/4
 */
public class DsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    public DsException() {
        super();
        this.code = -99;
    }

    public DsException(String message) {
        super(message);
        this.code = -99;
    }

    public DsException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public DsException(Throwable cause) {
        super(cause);
        this.code = -99;
    }

    public DsException(String message, Throwable cause) {
        super(message, cause);
        this.code = -99;
    }

    public DsException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
