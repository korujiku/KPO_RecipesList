package com.example.RecipesList.recipe.exception;

public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException(Long id) {
        super(String.format("Recipe with id [%s] is not found", id));
    }
}
