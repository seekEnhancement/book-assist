package woos.bookassist.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseRuntimeException {
    public static final int CODE_RESOURCE_NOT_FOUND = 400001;

    public ResourceNotFoundException(String query) {
        super(HttpStatus.NOT_FOUND, CODE_RESOURCE_NOT_FOUND, "query:[" + query + "] is not found.");
    }

    @Override
    public Integer getCode() {
        return CODE_RESOURCE_NOT_FOUND;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
