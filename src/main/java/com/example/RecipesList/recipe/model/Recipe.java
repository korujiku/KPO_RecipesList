package com.example.RecipesList.recipe.model;

import com.example.RecipesList.recipe.dto.RecipeDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
public class Recipe {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotBlank( message = "Recipe's name can't be null or empty" )
    private String name;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "userModel_fk" )
    private UserModel userModel;

    @NotBlank( message = "Recipe's ingridients can't be null or empty" )
    private String ingridients;

    @NotBlank( message = "Recipe's preparing info can't be null or empty" )
    private String preparing;

    @Lob
    private byte[] image;

    public Recipe() {}

    public Recipe(String name, String ingridients, String preparing, byte[] image) {
        this.name = name;
        this.ingridients = ingridients;
        this.preparing = preparing;
        this.image = image;
    }

    public Recipe(RecipeDto recipeDto){
        this.name = recipeDto.getName();
        this.ingridients = recipeDto.getIngridients();
        this.preparing = recipeDto.getPreparing();
        this.image = recipeDto.getImage().getBytes();
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
        if (!userModel.getRecipes().contains(this)) {
            userModel.setRecipe(this);
        }
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngridients() {
        return ingridients;
    }

    public void setIngridients(String ingridients) {
        this.ingridients = ingridients;
    }

    public String getPreparing() {
        return preparing;
    }

    public void setPreparing(String preparing) {
        this.preparing = preparing;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    // ![Properties]

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                "name=" + name +
                "ingridients=" + ingridients +
                "preparing=" + preparing +
                '}';
    }
}
