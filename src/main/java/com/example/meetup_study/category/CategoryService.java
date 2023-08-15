package com.example.meetup_study.category;

import com.example.meetup_study.category.domain.Category;
import com.example.meetup_study.category.domain.CategoryEnum;

import java.util.Optional;

public interface CategoryService {

    void createCategory(CategoryEnum categoryEnum);

    Optional<Category> getCategory(CategoryEnum categoryEnum);

    void deleteCategory(CategoryEnum categoryEnum);


}
