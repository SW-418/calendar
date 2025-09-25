package samwells.io.calendar.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long resourceId) {
        super(String.format("Requested resource with id %s was not found", resourceId));
    }
}
