package samwells.io.calendar.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(Long resourceId) {
        super(String.format("User is not authorized to access resource with id %s", resourceId));
    }
}
