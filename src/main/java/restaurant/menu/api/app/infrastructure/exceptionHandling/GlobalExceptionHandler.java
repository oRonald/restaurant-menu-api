package restaurant.menu.api.app.infrastructure.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.DishNotFoundException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.ExistingTableOrderException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.OrderNotFoundException;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.RoleNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionTemplate> templateExceptionMessage(String message, HttpStatus status){
        return ResponseEntity.status(status).body(new ExceptionTemplate(
                message,
                status.value(),
                status.name(),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionTemplate> handleRoleNotFound(RoleNotFoundException ex){
        return templateExceptionMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExistingTableOrderException.class)
    public ResponseEntity<ExceptionTemplate> handleExistingTableOrderException(ExistingTableOrderException ex){
        return templateExceptionMessage(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<ExceptionTemplate> handleDishNotFound(DishNotFoundException e){
        return templateExceptionMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ExceptionTemplate> handleOrderNotFound(OrderNotFoundException e){
        return templateExceptionMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
