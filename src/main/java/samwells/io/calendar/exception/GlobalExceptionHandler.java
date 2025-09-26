package samwells.io.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import samwells.io.calendar.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { InvalidDateTimeFormat.class, InvalidTimezonePreferenceException.class, InvalidCursorException.class })
    public ResponseEntity<ErrorDto> handleInvalidDateTimeFormat(Exception ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleInvalidDateTimeFormat(UnauthorizedException ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorDto("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
