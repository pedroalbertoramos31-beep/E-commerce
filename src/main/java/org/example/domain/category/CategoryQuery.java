package org.example.domain.category;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.exception.error.CategoryException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryQuery {


    private final CategoryRepository categoryRepository;

    public Set<Category> findByIds(Set<Long> categoriesId) {

        Set<Category> categories = categoryRepository.findCategories(categoriesId);

        if (categories.size() != categoriesId.size()) {
            throw new CategoryException.NotFound();
        }

        return categories;
    }

    public Category findById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryException.NotFound::new);
    }

    public void existsByName(String name){
        if (categoryRepository.existsByName(name)){
            throw new CategoryException.DuplicateName(name);
        }
    }



}