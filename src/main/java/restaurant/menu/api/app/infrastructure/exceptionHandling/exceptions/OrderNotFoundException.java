package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
