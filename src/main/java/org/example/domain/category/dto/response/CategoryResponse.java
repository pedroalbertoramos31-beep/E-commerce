package org.example.domain.category.dto.response;

import org.example.domain.category.CategoryStatus;

public record CategoryResponse(

        Long id,
        String name,
        CategoryStatus status

) {}
