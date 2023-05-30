package com.example.meetup_study.Category;

import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryEnum;

import java.util.Optional;

public interface CategoryService {

    void createCategory(CategoryEnum categoryEnum);

    Optional<Category> getCategory(CategoryEnum categoryEnum);

    void deleteCategory(CategoryEnum categoryEnum);


}
