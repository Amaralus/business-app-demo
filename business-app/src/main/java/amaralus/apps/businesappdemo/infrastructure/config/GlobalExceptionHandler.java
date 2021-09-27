package amaralus.apps.businesappdemo.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus
    public ErrorResponse handleException(Exception e) throws Exception {
        if (e instanceof ResponseStatusException)
            throw e;

        log.error("Неизвестная ошибка!", e);
        return new ErrorResponse("Неизвестная ошибка сервера!", e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        return validationErrorResponse(e);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(validationErrorResponse(ex));
    }

    private ErrorResponse validationErrorResponse(Exception e) {
        log.warn("Ошибка валидации!", e);
        return new ErrorResponse("Ошибка валидаци запроса!", e);
    }
}
