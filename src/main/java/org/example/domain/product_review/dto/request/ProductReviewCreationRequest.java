package org.example.domain.product_review.dto.request;

import jakarta.validation.constraints.*;

public record ProductReviewCreationRequest(

        @NotNull(message = "Rating is mandatory")
        @Min(value = 1, message = "Minimum rating is 1 star")
        @Max(value = 5, message = "Maximum rating is 5 star")
        Integer rating,

        @NotBlank(message = "The commentary can not be blank")
        @Size(max = 500, message = "The commentary can not be more than 500 characters")
        String comment
) {
}
