package org.example.domain.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.category.dto.request.CategoryRegisterRequest;
import org.example.domain.category.dto.response.CategoryResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    private final CategoryQuery categoryQuery;

    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse registerCategory(CategoryRegisterRequest request){

        categoryQuery.existsByName(request.name());

        Category category = categoryRepo.save(
                Category.create(request.name(), CategoryStatus.ACTIVE));

        return categoryMapper.toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategoryState(Long categoryId, CategoryStatus status){

        Category category = categoryQuery.findById(categoryId);

        category.changeState(status);

        return categoryMapper.toCategoryResponse(category);
    }
}
