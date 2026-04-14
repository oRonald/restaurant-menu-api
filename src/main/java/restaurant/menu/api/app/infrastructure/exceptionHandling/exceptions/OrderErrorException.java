package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class OrderErrorException extends RuntimeException {

    public OrderErrorException(String message) {
        super(message);
    }
}
