package com.example.RecipesList.recipe.controller;

import com.example.RecipesList.recipe.dto.UserDto;
import com.example.RecipesList.recipe.model.User;
import com.example.RecipesList.recipe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        User user = userService.findUser(id);
        return "user-profile";
    }

    @GetMapping(value = "/profile/{id}/edit")
    public String editUser(@PathVariable Long id,
                           Model model) {
        User user = userService.findUser(id);
        model.addAttribute("userDto", new UserDto(user));
        return "user-profile-edit";
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
        User user = userService.findByLogin(principal.getName());
        userService.deleteUser(user.getId());
        return "redirect:/login";
    }
}
