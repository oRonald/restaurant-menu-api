package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class MenuItemNotFoundException extends RuntimeException {

    public MenuItemNotFoundException(String message) {
        super(message);
    }
}
