package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class EmployeeExistsException extends RuntimeException {

    public EmployeeExistsException(String message) {
        super(message);
    }
}
