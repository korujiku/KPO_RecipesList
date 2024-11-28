package com.example.RecipesList.recipe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class UserModel {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Column( nullable = false, unique = true, length = 64 )
    @NotBlank( message = "login can't be null or empty" )
    @Size( min = 3, max = 64 )
    private String login;

    @Column( nullable = false, length = 64 )
    @NotBlank( message = "password can't be null or empty" )
    @Size( min = 5, max = 64 )
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userModel", cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<Recipe> recipes;

    private UserRole role;

    public UserModel(){}

    public UserModel(String login, String password){
        this.login = login;
        this.password = password;
        this.recipes = new ArrayList<>();
        this.role = UserRole.USER;
    }

    public UserModel(String login, String password, UserRole role){
        this.login = login;
        this.password = password;
        this.recipes = new ArrayList<>();
        this.role = role;
    }

    public UserModel(UserSignupDto userSignupDto){
        this.login = userSignupDto.getLogin();
        this.password = userSignupDto.getPassword();
        this.role = UserRole.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(id, userModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserModel {" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserRole getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipe(Recipe recipe) {
        if (recipe.getUserModel().equals(this)) {
            this.recipes.add(recipe);
        }
    }
}
