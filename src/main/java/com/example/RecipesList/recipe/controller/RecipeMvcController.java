package com.example.RecipesList.recipe.controller;

import com.example.RecipesList.recipe.dto.RecipeDto;
import com.example.RecipesList.recipe.model.Recipe;
import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.model.UserRole;
import com.example.RecipesList.recipe.service.RecipeService;
import com.example.RecipesList.recipe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/recipe")
public class RecipeMvcController {
    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeMvcController(RecipeService recipeService,
                               UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.findAllRecipes()
                .stream().map(RecipeDto::new).toList());
        return "recipes-list";
    }

    @GetMapping(value = "/{id}/page")
    public String getRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findRecipe(id);
        model.addAttribute("recipeId", id);
        model.addAttribute("recipeDto", new RecipeDto(recipe));
        return "recipe-page";
    }

    @GetMapping(value = {"/create", "/{id}/edit"})
    public String editRecipe(@PathVariable(required = false) Long id,
                             Model model, Principal principal) {
        if(id==null||id<=0) {
            model.addAttribute("recipeDto", new RecipeDto());
            return "create-recipe";
        } else {
            Recipe recipe = recipeService.findRecipe(id);
            UserModel userModel = userService.findByLogin(principal.getName());

            if(!Objects.equals(recipe.getUserModel().getId(), userModel.getId()) && userModel.getRole() == UserRole.USER){
                return "redirect:/recipe";
            }
            model.addAttribute("recipeId", id);
            model.addAttribute("recipeDto", new RecipeDto(recipe));

            return "edit-recipe";
        }
    }

    @GetMapping(value = "/search/")
    public String searchRecipe(@RequestParam String request, Model model){
        List<RecipeDto> recipes = recipeService.findAllRecipes(request)
                .stream().map(RecipeDto::new).toList();
        model.addAttribute("recipes", recipes);
        return "recipes-list";
    }

    @PostMapping(value = {"/"})
    public String saveRecipe(@PathVariable(required = false) Long id,
                             @RequestParam("multipartFile") MultipartFile multipartFile,
                                @ModelAttribute @Valid RecipeDto recipeDto,
                                BindingResult bindingResult,
                                Model model, Principal principal) throws IOException {
        Long userId = userService.findByLogin(principal.getName()).getId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "edit-recipe";
        }
        if (id == null || id <= 0) {
            recipeDto.setImage("data:" + multipartFile.getContentType() + ";base64," + Base64.getEncoder().encodeToString(multipartFile.getBytes()));
            recipeService.addRecipe(userId,recipeDto);
        } else {
            recipeDto.setId(id);
            recipeDto.setImage("data:" + multipartFile.getContentType() + ";base64," + Base64.getEncoder().encodeToString(multipartFile.getBytes()));
            recipeService.updateRecipe(recipeDto);
        }
        return "redirect:/recipe";
    }

    @PostMapping(value = "/{id}")
    public String editRecipe(@PathVariable Long id,
                                @ModelAttribute @Valid RecipeDto recipeDto,
                                @RequestParam("multipartFile") MultipartFile multipartFile,
                                Model model, Principal principal) throws IOException {
        Recipe recipe = recipeService.findRecipe(id);
        UserModel userModel = userService.findByLogin(principal.getName());
        if(!Objects.equals(recipe.getUserModel().getId(), userModel.getId())&& userModel.getRole()==UserRole.USER){
            return "/recipe";
        }
        recipeDto.setId(id);
        recipeDto.setImage("data:" + multipartFile.getContentType() + ";base64," + Base64.getEncoder().encodeToString(multipartFile.getBytes()));
        recipeService.updateRecipe(recipeDto);
        return "redirect:/recipe/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable Long id, Principal principal) {
        Recipe recipe = recipeService.findRecipe(id);
        UserModel userModel = userService.findByLogin(principal.getName());
        if(!Objects.equals(recipe.getUserModel().getId(), userModel.getId())&& userModel.getRole()==UserRole.USER){
            return "redirect:/recipe";
        }
        recipeService.deleteRecipe(id);
        return "redirect:/recipe";
    }
}
