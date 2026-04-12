package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class ExistingTableOrderException extends RuntimeException {

    public ExistingTableOrderException(String message) {
        super(message);
    }
}
