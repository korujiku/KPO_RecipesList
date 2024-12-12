package com.example.RecipesList.recipe.controller;

import com.example.RecipesList.recipe.dto.RecipeDto;
import com.example.RecipesList.recipe.dto.UserDto;
import com.example.RecipesList.recipe.model.Recipe;
import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final UserService userService;

    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/profile")
    public String getUser(Principal principal,
                          Model model) {
        UserModel userModel = userService.findByLogin(principal.getName());
        List<RecipeDto> recipes = new ArrayList<>();

        for ( Recipe recipe : userModel.getRecipes()){
            recipes.add(new RecipeDto(recipe));
        }
        model.addAttribute("userDto", new UserDto(userModel));
        model.addAttribute("recipes", recipes);
        return "user-profile";
    }

    @GetMapping(value = "/profile/{id}/edit")
    public String editUser(@PathVariable Long id,
                           Model model) {
        UserModel userModel = userService.findUser(id);
        model.addAttribute("userDto", new UserDto(userModel));
        return "edit-profile";
    }

    @PostMapping(value = "/{id}")
    public String editUser(@PathVariable Long id,
                           @RequestParam String passwordConfirm,
                           @ModelAttribute @Valid UserDto userDto,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "edit-profile";
        }
        if (!userDto.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "New passwords do not match");
            return "edit-profile";
        }
        userService.updateUser(id, userDto.getLogin(), passwordConfirm);

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteUser(Principal principal) {
        UserModel userModel = userService.findByLogin(principal.getName());
        userService.deleteUser(userModel.getId());
        return "redirect:/login";
    }
}
