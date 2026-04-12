package restaurant.menu.api.app.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull
        @Positive
        Integer tableNumber,

        @NotNull
        @Positive
        Long menuItem,

        @NotNull
        @Min(1)
        Integer quantity,

        @Positive
        BigDecimal tip
) {
}
