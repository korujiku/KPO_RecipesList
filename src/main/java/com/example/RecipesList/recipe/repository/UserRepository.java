package com.example.RecipesList.recipe.repository;

import com.example.RecipesList.recipe.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findOneByLoginIgnoreCase(String login);
}
