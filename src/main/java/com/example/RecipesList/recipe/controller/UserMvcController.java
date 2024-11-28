package com.example.RecipesList.recipe.controller;

import com.example.RecipesList.recipe.dto.UserDto;
import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final UserService userService;

    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/profile/{id}")
    public String getUser(@PathVariable Long id,
                          Model model) {
        UserModel userModel = userService.findUser(id);
        return "userModel-profile";
    }

    @GetMapping(value = "/profile/{id}/edit")
    public String editUser(@PathVariable Long id,
                           Model model) {
        UserModel userModel = userService.findUser(id);
        model.addAttribute("userDto", new UserDto(userModel));
        return "userModel-profile-edit";
    }

    @PostMapping(value = "/{id}")
    public String editUser(@PathVariable Long id,
                           @ModelAttribute @Valid UserDto userDto,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "user-profile-edit";
        }
        userService.updateUser(id, userDto.getLogin(), userDto.getPassword());

        return "redirect:/user/profile/" + id;
    }

    @PostMapping("/profile/delete")
    public String deleteUser(Principal principal) {
        UserModel userModel = userService.findByLogin(principal.getName());
        userService.deleteUser(userModel.getId());
        return "redirect:/login";
    }
}
