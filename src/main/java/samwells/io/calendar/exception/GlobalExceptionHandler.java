package samwells.io.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import samwells.io.calendar.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDateTimeFormat.class)
    public ResponseEntity<ErrorDto> handleInvalidDateTimeFormat(InvalidDateTimeFormat ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorDto("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
