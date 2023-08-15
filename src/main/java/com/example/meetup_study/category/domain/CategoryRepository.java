package com.example.meetup_study.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.name = :categoryEnum")
    Optional<Category> findByName(@Param("categoryEnum") CategoryEnum categoryEnum);
}
