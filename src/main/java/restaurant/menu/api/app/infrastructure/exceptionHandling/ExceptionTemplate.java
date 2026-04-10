package restaurant.menu.api.app.infrastructure.exceptionHandling;

import java.time.LocalDateTime;

public record ExceptionTemplate(
        String message,
        int status,
        String statusError,
        LocalDateTime timestamp
) {
}
