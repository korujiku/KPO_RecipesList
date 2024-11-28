package com.example.RecipesList.recipe.service;

import com.example.RecipesList.recipe.dto.RecipeDto;
import com.example.RecipesList.recipe.exception.RecipeNotFoundException;
import com.example.RecipesList.recipe.model.Recipe;
import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.repository.RecipeRepository;
import com.example.RecipesList.util.validation.ValidatorUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserService userService;
    private final ValidatorUtil validatorUtil;

    public RecipeService(RecipeRepository recipeRepository,
                         UserService userService,
                         ValidatorUtil validatorUtil) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.validatorUtil = validatorUtil;
    }

    @Transactional
    public Recipe addRecipe(Long userId, RecipeDto recipeDto) {
        final Recipe recipe = new Recipe(recipeDto);
        final UserModel userModel = userService.findUser(userId);
        recipe.setUserModel(userModel);
        validatorUtil.validate(recipe);
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe findRecipe(Long id) {
        final Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @Transactional
    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional
    public List<Recipe> findAllRecipes(String search) {
        return recipeRepository.findAll(search);
    }

    @Transactional
    public Recipe updateRecipe(RecipeDto recipeDto) {
        final Recipe currentRecipe = findRecipe(recipeDto.getId());
        currentRecipe.setName(recipeDto.getName());
        currentRecipe.setIngridients(recipeDto.getIngridients());
        currentRecipe.setPreparing(recipeDto.getPreparing());
        validatorUtil.validate(currentRecipe);
        return recipeRepository.save(currentRecipe);
    }

    @Transactional
    public Recipe deleteRecipe(Long id) {
        final Recipe currentRecipe = findRecipe(id);
        recipeRepository.delete(currentRecipe);
        return currentRecipe;
    }

    @Transactional
    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }
}
