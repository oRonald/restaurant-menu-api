package restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions;

public class DishNotFoundException extends RuntimeException{

    public DishNotFoundException(String message) {
        super(message);
    }
}
