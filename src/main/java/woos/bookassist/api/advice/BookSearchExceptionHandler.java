package woos.bookassist.api.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import woos.bookassist.common.exception.ResourceNotFoundException;

@ControllerAdvice
public class BookSearchExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFound(
            RuntimeException ex, WebRequest request) {
        String requestParamQuery = request.getParameter("query");
        return handleExceptionInternal(ex, requestParamQuery,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
