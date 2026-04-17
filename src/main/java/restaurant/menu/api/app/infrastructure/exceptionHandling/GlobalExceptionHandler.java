package restaurant.menu.api.app.infrastructure.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import restaurant.menu.api.app.infrastructure.exceptionHandling.exceptions.*;

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

    @ExceptionHandler(EmployeeExistsException.class)
    public ResponseEntity<ExceptionTemplate> handleEmployeeExists(EmployeeExistsException ex){
        return templateExceptionMessage(ex.getMessage(), HttpStatus.CONFLICT);
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

    @ExceptionHandler(CancellationOrderNotPossibleException.class)
    public ResponseEntity<ExceptionTemplate> handleCancellationOrderNotPossibleException(CancellationOrderNotPossibleException e){
        return templateExceptionMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderErrorException.class)
    public ResponseEntity<ExceptionTemplate> handleOrderErrorException(OrderErrorException e){
        return templateExceptionMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<ExceptionTemplate> handleMenuItemNotFoundException(MenuItemNotFoundException e){
        return templateExceptionMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MenuItemException.class)
    public ResponseEntity<ExceptionTemplate> handleMenuItemException(MenuItemException e){
        return templateExceptionMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "Atributo: '" + error.getField() + "' " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
