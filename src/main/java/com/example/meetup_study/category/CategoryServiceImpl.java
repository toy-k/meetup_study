package com.example.meetup_study.category;

import com.example.meetup_study.category.domain.Category;
import com.example.meetup_study.category.domain.CategoryEnum;
import com.example.meetup_study.category.domain.CategoryRepository;
import com.example.meetup_study.category.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryEnum categoryEnum) {
        categoryRepository.save(new Category(categoryEnum));
    }

    @Override
    public Optional<Category> getCategory(CategoryEnum categoryEnum) {

        return categoryRepository.findByName(categoryEnum);
    }
    @Override
    public void deleteCategory(CategoryEnum categoryEnum) {
        Optional<Category> categoryOpt = categoryRepository.findByName(categoryEnum);
        if(categoryOpt.isPresent()){
            categoryRepository.delete(categoryOpt.get());
        }else{
            throw new CategoryNotFoundException();
        }
    }
}
