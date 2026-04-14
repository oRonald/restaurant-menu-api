package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class CancellationOrderNotPossibleException extends RuntimeException {

    public CancellationOrderNotPossibleException(String message) {
        super(message);
    }
}
