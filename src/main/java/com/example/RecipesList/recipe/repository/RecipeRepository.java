package com.example.RecipesList.recipe.repository;

import com.example.RecipesList.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("select r from Recipe r where r.name ilike CONCAT('%', :search, '%')")
    List<Recipe> findAll(@Param("search") String search);
}
