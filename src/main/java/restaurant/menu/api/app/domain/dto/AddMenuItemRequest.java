package restaurant.menu.api.app.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddMenuItemRequest(
        @NotBlank
        @NotNull
        String name,

        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        @NotBlank
        String category
) {
}
