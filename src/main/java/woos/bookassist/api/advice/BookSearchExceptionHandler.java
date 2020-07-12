package woos.bookassist.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import woos.bookassist.common.exception.BaseRuntimeException;
import woos.bookassist.common.exception.ErrorResponse;
import woos.bookassist.common.exception.ResourceNotFoundException;

import java.util.function.Consumer;

@Slf4j
@RestControllerAdvice
public class BookSearchExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponse> handleResourceNotFound(BaseRuntimeException exception) {
        return makeResponseEntity(exception, x -> log.warn(x.getMessage(), x));
    }

    private ResponseEntity<ErrorResponse> makeResponseEntity(BaseRuntimeException exception,
                                                             Consumer<BaseRuntimeException> consumer) {
        consumer.accept(exception);
        return ResponseEntity.status(exception.getStatus()).body(makeErrorResponse(exception));
    }

    private ErrorResponse makeErrorResponse(BaseRuntimeException exception) {
        return new ErrorResponse(exception.getStatus().value(), exception.getCode(), exception.getMessage());
    }
}
