package uk.mirek.kpler.controllers;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import uk.mirek.kpler.dto.ErrorResponse;

import java.util.UUID;

@RestControllerAdvice
@Order(value = 1)
public class BaseExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @ExceptionHandler({HandlerMethodValidationException.class, ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), UUID.randomUUID().toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error("Exception", ex);
        return new ResponseEntity<>(new ErrorResponse("Encountered internal issue, please try again", UUID.randomUUID().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
