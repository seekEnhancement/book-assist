package woos.bookassist.common.exception;

import org.springframework.http.HttpStatus;

public class UserRegisterFailedException extends BaseRuntimeException {
    public static final int CODE_USER_REGISTER_FAILED = 600001;

    public UserRegisterFailedException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, CODE_USER_REGISTER_FAILED, errorMessage);
    }

    @Override
    public Integer getCode() {
        return CODE_USER_REGISTER_FAILED;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
