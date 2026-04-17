package restaurant.menu.api.app.domain.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull
        @Positive
        Integer tableNumber,

        @NotNull
        @NotBlank
        String customerName,

        @NotNull
        @NotBlank
        String menuItem,

        @NotNull
        @Min(1)
        Integer quantity,

        @PositiveOrZero
        BigDecimal tip
) {
}
