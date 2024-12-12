package com.example.RecipesList;

import com.example.RecipesList.recipe.dto.RecipeDto;
import com.example.RecipesList.recipe.exception.RecipeNotFoundException;
import com.example.RecipesList.recipe.exception.UserNotFoundException;
import com.example.RecipesList.recipe.model.Recipe;
import com.example.RecipesList.recipe.model.UserModel;
import com.example.RecipesList.recipe.service.RecipeService;
import com.example.RecipesList.recipe.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecipesListApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private RecipeService recipeService;

	//тесты пользователя
	@Test
	void AddUser(){
		userService.deleteAllUsers();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		Assertions.assertEquals(userService.findAllUsers().size(), 1);
	}

	@Test
	void DeleteUser() {
		userService.deleteAllUsers();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		userService.deleteUser(userService.findByLogin("TestUser").getId());

		Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUser(userModel.getId()));
	}

	@Test
	void UpdateUser() {
		userService.deleteAllUsers();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		final UserModel newUserModel = userService.updateUser(userModel.getId(), "TestUserUpd", "TestPassUpd");

		Assertions.assertEquals(userService.findUser(newUserModel.getId()).getLogin(), "TestUserUpd");
	}

	@Test
	void FindUser() {
		userService.deleteAllUsers();
		final UserModel user1 = userService.addUser("TestUser", "TestPass", "TestPass");
		final UserModel user2 = userService.findUser(user1.getId());

		Assertions.assertEquals(user1.getLogin(), user2.getLogin());
	}

	@Test
	void FindAllUsers(){
		userService.deleteAllUsers();
		final UserModel user1 = userService.addUser("TestUser", "TestPass", "TestPass");
		final UserModel user2 = userService.addUser("TestUser_1", "TestPass_1", "TestPass_1");

		Assertions.assertEquals(userService.findAllUsers().size(), 2);
	}

	//тесты рецепта
	@Test
	void AddRecipe(){
		userService.deleteAllUsers();
		recipeService.deleteAllRecipes();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		final Recipe addRecipe = recipeService.addRecipe(userModel.getId(), "TestRecipe", "TestIngredients", "TestPreparing", "TestImage".getBytes());
		Assertions.assertEquals(recipeService.findAllRecipes().size(), 1);
	}

	@Test
	void DeleteRecipe() {
		userService.deleteAllUsers();
		recipeService.deleteAllRecipes();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		final Recipe addRecipe = recipeService.addRecipe(userModel.getId(), "TestRecipe", "TestIngredients", "TestPreparing", "TestImage".getBytes());
		recipeService.deleteRecipe(addRecipe.getId());

		Assertions.assertThrows(RecipeNotFoundException.class, () -> recipeService.findRecipe(addRecipe.getId()));
	}

	@Test
	void UpdateRecipe() {
		userService.deleteAllUsers();
		recipeService.deleteAllRecipes();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		final Recipe addRecipe = recipeService.addRecipe(userModel.getId(), "TestRecipe", "TestIngredients", "TestPreparing", "TestImage".getBytes());
		final Recipe resultUpd = recipeService.updateRecipe(addRecipe.getId(), "UpdName", "UpdIngred", "UpdPrep", "UpdImage".getBytes());
		Assertions.assertEquals(recipeService.findRecipe(resultUpd.getId()).getName(), "UpdName");
	}

	@Test
	void FindRecipe() {
		userService.deleteAllUsers();
		recipeService.deleteAllRecipes();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		final Recipe addRecipe = recipeService.addRecipe(userModel.getId(), "TestRecipe", "TestIngredients", "TestPreparing", "TestImage".getBytes());
		final Recipe forFindRecipe = recipeService.findRecipe(addRecipe.getId());
		Assertions.assertEquals(addRecipe.getId(), forFindRecipe.getId());
	}

	@Test
	void FindAllRecipes(){
		userService.deleteAllUsers();
		recipeService.deleteAllRecipes();
		final UserModel userModel = userService.addUser("TestUser", "TestPass", "TestPass");
		final Recipe addRecipe = recipeService.addRecipe(userModel.getId(), "TestRecipe", "TestIngredients", "TestPreparing", "TestImage".getBytes());

		final Recipe addRecipe1 = recipeService.addRecipe(userModel.getId(), "TestRecipe", "TestIngredients", "TestPreparing", "TestImage".getBytes());

		Assertions.assertEquals(recipeService.findAllRecipes().size(), 2);
	}
}
