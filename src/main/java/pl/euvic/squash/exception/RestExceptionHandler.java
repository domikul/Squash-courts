package pl.euvic.squash.exception;

import org.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {

        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {NotAuthenticatedException.class})
    protected ResponseEntity<Object> handleNotAuthenticated(RuntimeException ex, WebRequest request) {

        ExceptionView exceptionView = new ExceptionView(HttpStatus.UNAUTHORIZED.value(), ex.getClass().getName(), ex.getMessage());

        return handleExceptionInternal(ex, exceptionView,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {WrongRequestException.class})
    protected ResponseEntity<Object> handleWrongRequest(RuntimeException ex, WebRequest request) {

        ExceptionView exceptionView = new ExceptionView(HttpStatus.BAD_REQUEST.value(), ex.getClass().getName(), ex.getMessage());

        return handleExceptionInternal(ex, exceptionView,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {

        ExceptionView exceptionView = new ExceptionView(HttpStatus.NOT_FOUND.value(), ex.getClass().getName(), ex.getMessage());

        return handleExceptionInternal(ex, exceptionView,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}