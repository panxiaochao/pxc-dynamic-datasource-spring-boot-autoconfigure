package io.github.panxiaochao.exception;

/**
 * {@code MultipleDataSourceException}
 * <p>多数据库自定义错误
 *
 * @author Lypxc
 * @since 2022/1/4
 */
public class MultipleDataSourceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MultipleDataSourceException() {
        super();
    }

    public MultipleDataSourceException(String message) {
        super(message);
    }

    public MultipleDataSourceException(Throwable cause) {
        super(cause);
    }

    public MultipleDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
