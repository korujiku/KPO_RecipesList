package com.example.RecipesList.recipe.dto;

import com.example.RecipesList.recipe.model.Recipe;

import java.nio.charset.StandardCharsets;

public class RecipeDto {
    private long id;
    private String name;
    private String ingridients;
    private String preparing;
    private String image;
    private Long userId;
    private String userModel;

    public RecipeDto() {}

    public RecipeDto(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.ingridients = recipe.getIngridients();
        this.preparing = recipe.getPreparing();
        this.image = new String(recipe.getImage(), StandardCharsets.UTF_8);
        this.userId = recipe.getUserModel().getId();
        this.userModel = recipe.getUserModel().getLogin();
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public long getId(){ return id; }

    public String getName(){ return name; }

    public String getIngridients(){ return ingridients; }

    public String getPreparing(){ return preparing; }

    public String getImage(){ return image; }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngridients(String ingridients) {
        this.ingridients = ingridients;
    }

    public void setPreparing(String preparing) {
        this.preparing = preparing;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUserModel(String user){
        this.userModel = user;
    }

    public String getUserModel(){
        return userModel;
    }
}
