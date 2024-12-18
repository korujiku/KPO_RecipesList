package com.example.RecipesList.recipe.controller;

import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.model.UserSignupDto;
import com.example.RecipesList.recipe.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(UserSignupMvcController.SIGNUP_URL)
public class UserSignupMvcController {
    public static final String SIGNUP_URL = "/signup";

    private final UserService userService;

    public UserSignupMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserSignupDto());
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("userDto") @Valid UserSignupDto userSignupDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "signup";
        }
        try {
            final UserModel userModel = userService.addUser(
                    userSignupDto.getLogin(), userSignupDto.getPassword(), userSignupDto.getPasswordConfirm());
            return "redirect:/login?created=" + userModel.getLogin();
        } catch ( ValidationException e) {
            model.addAttribute("errors", e.getMessage());
            return "signup";
        }
    }
}
