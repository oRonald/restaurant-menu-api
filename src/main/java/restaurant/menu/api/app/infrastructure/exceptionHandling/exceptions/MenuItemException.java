package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class MenuItemException extends RuntimeException {

    public MenuItemException(String message) {
        super(message);
    }
}
