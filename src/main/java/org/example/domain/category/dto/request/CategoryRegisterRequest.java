package org.example.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRegisterRequest(

        @NotBlank(message = "Category name cannot be blank")
        @Size(min = 3, max = 100, message = "Category name has to be at least 3 characters long and max 100")
        String name
) {
}
