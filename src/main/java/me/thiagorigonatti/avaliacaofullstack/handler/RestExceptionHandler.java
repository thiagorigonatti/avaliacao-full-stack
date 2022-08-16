package me.thiagorigonatti.avaliacaofullstack.handler;

import me.thiagorigonatti.avaliacaofullstack.exception.BadRequestException;
import me.thiagorigonatti.avaliacaofullstack.exception.BadRequestExceptionDetails;
import me.thiagorigonatti.avaliacaofullstack.exception.ValidationExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
        BadRequestExceptionDetails badRequestExceptionDetails = new BadRequestExceptionDetails();
        badRequestExceptionDetails.setTitle("Bad request exception");
        badRequestExceptionDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        badRequestExceptionDetails.setDetails(bre.getMessage());
        badRequestExceptionDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(badRequestExceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException manve) {

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField)
                .collect(Collectors.joining(", "));

        String fieldsMessages = fieldErrors.stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));


        ValidationExceptionDetails validationExceptionDetails = new ValidationExceptionDetails(fields, fieldsMessages);
        validationExceptionDetails.setTitle("Bad request exception invalid fields");
        validationExceptionDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        validationExceptionDetails.setDetails("Check the fields error");
        validationExceptionDetails.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(validationExceptionDetails, HttpStatus.BAD_REQUEST);
    }
}
