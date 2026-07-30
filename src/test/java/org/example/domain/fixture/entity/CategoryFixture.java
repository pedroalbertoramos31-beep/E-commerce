package org.example.domain.fixture.entity;

import org.example.domain.category.Category;
import org.example.domain.category.CategoryStatus;

public class CategoryFixture {

    public static final String DEFAULT_NAME = "Food";
    public static final CategoryStatus DEFAULT_STATUS = CategoryStatus.ACTIVE;

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder {

        private String name = DEFAULT_NAME;
        private CategoryStatus status = DEFAULT_STATUS;

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder status(CategoryStatus status) {
            this.status = status;
            return this;
        }

        public Category build() {
            return Category.create(name, status);
        }
    }

}
