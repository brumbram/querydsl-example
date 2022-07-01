package com.opentable.sampleapplication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle Invalid fields data exception
     * @param invalidArgument
     * @return a {@code ErrorResponse}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException invalidArgument
    ) {
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Input Validation error.");

        for (FieldError fieldError : invalidArgument.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle unsupported field for sorting
     * @param unsupportedInput
     * @return a {@code ErrorResponse}
     */
    @ExceptionHandler(UnsupportedInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUnsupportedInput(
            UnsupportedInputException unsupportedInput
    ) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), unsupportedInput.getMessage()));
    }

    /**
     * Handle unprocessable json data exception
     * @param msgNotReadable
     * @return a {@code ErrorResponse}
     */
    @ExceptionHandler(value= HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleUnprocessableMsgException(HttpMessageNotReadableException msgNotReadable) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Unprocessable input data"));
    }
}
