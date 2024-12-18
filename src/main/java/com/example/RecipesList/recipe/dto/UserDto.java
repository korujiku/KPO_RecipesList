package com.example.RecipesList.recipe.dto;

import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.model.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private long id;
    private String login;
    private String password;
    private List<RecipeDto> recipes;
    private UserRole role;

    public UserDto() {}

    public UserDto(UserModel userModel) {
        this.id = userModel.getId();
        this.login = userModel.getLogin();
        this.password = userModel.getPassword();
        this.recipes = new ArrayList<>();
        this.role = userModel.getRole();
        if ( userModel.getRecipes() != null) {
            recipes = userModel.getRecipes().stream()
                    .map(RecipeDto::new).toList();
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRecipes(List<RecipeDto> recipes) {
        this.recipes = recipes;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<RecipeDto> getRecipes() {
        return recipes;
    }

    public UserRole getRole() {
        return role;
    }
}
