package com.example.meetup_study.Category;

import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.Category.domain.CategoryRepository;
import com.example.meetup_study.Category.exception.CategoryNotFoundException;
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
