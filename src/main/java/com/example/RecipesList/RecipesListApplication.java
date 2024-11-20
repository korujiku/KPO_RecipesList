package com.example.RecipesList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RecipesListApplication {

	public static void main(String[] args) {

		SpringApplication.run(RecipesListApplication.class, args);
	}

}
