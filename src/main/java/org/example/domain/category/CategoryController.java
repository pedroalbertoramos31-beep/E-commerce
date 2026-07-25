package org.example.domain.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.category.dto.request.CategoryRegisterRequest;
import org.example.domain.category.dto.response.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    CategoryService categoryService;


    /* ADMIN */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> registerCategory(@Valid @RequestBody CategoryRegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.registerCategory(request));
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategoryState(
            @PathVariable Long categoryId,
            @RequestParam CategoryStatus state){

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategoryState(categoryId, state));
    }


}
