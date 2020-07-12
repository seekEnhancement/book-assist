package woos.bookassist.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 5436127787678438006L;
    private HttpStatus status;
    private Integer code;
    private String message;

    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(HttpStatus status, Integer code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public abstract Integer getCode();

    public abstract HttpStatus getStatus();
}
