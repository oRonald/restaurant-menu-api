package restaurant.menu.api.app.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginEmployee(
        @NotNull
        @NotBlank
        String username,

        @NotNull
        @NotBlank
        String password
) {
}
