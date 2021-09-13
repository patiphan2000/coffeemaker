/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modifications 
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to 
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for CoffeeMaker class.
 * 
 * @author Patiphan Srichai
 */
public class CoffeeMakerTest {
	
	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	private CoffeeMaker mockCoffeeMaker;
	private RecipeBook recipeBook;
	private Inventory inventory;
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	private Recipe[] recipeList;

	private Recipe createRecipe(String name, String chocolate, String coffee, String milk, String sugar, String price) throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setAmtChocolate(chocolate);
		recipe.setAmtCoffee(coffee);
		recipe.setAmtMilk(milk);
		recipe.setAmtSugar(sugar);
		recipe.setPrice(price);
		return recipe;
	}

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		coffeeMaker = new CoffeeMaker();

		inventory = new Inventory();
		recipeBook = mock(RecipeBook.class);
		mockCoffeeMaker = new CoffeeMaker(recipeBook, inventory);
		
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");

		// Stub of recipes (original).
		recipeList = new Recipe[] {recipe1, recipe2, recipe3, recipe4};

	}

	/**
	 * Test ID: 1
	 *
	 * Given 2 coffee recipe with the exact same name
	 * When we add a new recipe with the same name as existing recipe
	 * Then the new recipe will not be added and the first recipe will be unchanged.
	 */
	@Test
	public void testDuplicateRecipeName() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		Recipe dupRecipe = new Recipe();
		dupRecipe.setName("Coffee");
		coffeeMaker.addRecipe(dupRecipe);

		assertTrue(Arrays.asList(coffeeMaker.getRecipes()).contains(recipe1));
		assertEquals(recipe1, coffeeMaker.getRecipes()[0]);
		assertNotEquals(dupRecipe, coffeeMaker.getRecipes()[1]);
	}

	/**
	 * Test ID: 2
	 *
	 * Given 4 coffee recipe that can be added only 3 in the time
	 * When we add the 4th recipe the coffee maker will not accepted
	 * Then the 4th recipe will not be in coffee maker's recipes.
	 */
	@Test
	public void testFourRecipe() {
		coffeeMaker.addRecipe(recipe4);
		coffeeMaker.addRecipe(recipe3);
		coffeeMaker.addRecipe(recipe2);

		coffeeMaker.addRecipe(recipe1);  // this recipe should not be accepted
		assertFalse(Arrays.asList(coffeeMaker.getRecipes()).contains(recipe1));
	}

	/**
	 * Test ID: 3
	 *
	 * Given coffee recipe with decimal number prices
	 * When we add new recipe
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testDecimalPriceRecipe() throws RecipeException {
		Recipe decimalRecipe = new Recipe();
		decimalRecipe.setName("Coftea");
		decimalRecipe.setPrice("2.0");
	}

	/**
	 * Test ID: 4
	 *
	 * Given coffee recipe in the coffee maker
	 * When we delete that recipe
	 * Then the recipe will be remove from coffee maker's recipes
	 * 		and return the name of that recipe.
	 */
	@Test
	public void testDeleteRecipe() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(recipe1.getName(), coffeeMaker.deleteRecipe(0));
		assertFalse(Arrays.asList(coffeeMaker.getRecipes()).contains(recipe1));
	}

	/**
	 * Test ID: 5
	 *
	 * Given coffee maker with no recipe
	 * When we delete non-exist recipe
	 * Then it return empty string.
	 */
	@Test
	public void testDeleteGhostRecipe() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(recipe1.getName(), coffeeMaker.deleteRecipe(0));
		assertEquals("", coffeeMaker.deleteRecipe(0));
	}

	/**
	 * Test ID: 6
	 *
	 * Given the coffee maker with no recipe
	 * When we edit recipe
	 * Then prices and ingredient usage will be change.
	 *
	 * @throws RecipeException
	 */
	@Test
	public void testEditRecipe() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(5, coffeeMaker.makeCoffee(0, 55));
		Recipe changedRecipe = createRecipe("Halo Coffee", "1", "2", "2", "3", "55");
		coffeeMaker.editRecipe(0, changedRecipe);
		assertEquals(0, coffeeMaker.makeCoffee(0, 55));
	}

	/**
	 * Test ID: 7
	 *
	 * Given the coffee maker with no recipe
	 * When we try to edit recipe that not exist
	 * Then the recipe will still be empty.
	 *
	 * @throws RecipeException
	 */
	@Test
	public void testEditGhostRecipe() throws RecipeException {
		Recipe changedRecipe = createRecipe("Halo Coffee", "1", "2", "2", "3", "55");
		coffeeMaker.editRecipe(0, changedRecipe);
		assertNull(coffeeMaker.getRecipes()[0]);
	}

	/**
	 * Test ID: 8
	 *
	 * Given recipe in the coffee maker
	 * When we edit the recipe all of the information can be changed
	 * 		except name of the recipe
	 * Then name of the recipe will be the same.
	 *
	 * @throws RecipeException
	 */
	@Test
	public void	testEditRecipeName() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);
		Recipe changedRecipe = createRecipe("Halo Coffee", "1", "2", "2", "3", "55");
		coffeeMaker.editRecipe(0, changedRecipe);
		assertEquals("Coffee", coffeeMaker.getRecipes()[0].getName());
	}

	/**
	 * Test ID: 9
	 *
	 * Given a coffee maker with the default inventory
	 * When we add coffee to inventory with positive quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddPositiveCoffee() throws InventoryException {
		coffeeMaker.addInventory("1", "0", "0", "0");
	}

	/**
	 * Test ID: 10
	 *
	 * Given a coffee maker with the default inventory
	 * When we add coffee to inventory with decimal number quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddDecimalCoffee() throws InventoryException {
		coffeeMaker.addInventory("1.5", "0", "0", "0");
	}

	/**
	 * Test ID: 11
	 *
	 * Given a coffee maker with the default inventory
	 * When we add coffee to inventory with negative quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddNegativeCoffee() throws InventoryException {
		coffeeMaker.addInventory("-1", "0", "0", "0");
	}

	/**
	 * Test ID: 12
	 *
	 * Given a coffee maker with the default inventory
	 * When we add coffee to inventory with invalid form of quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInvalidCoffee() throws InventoryException {
		coffeeMaker.addInventory("bean", "0", "0", "0");
	}

	/**
	 * Test ID: 13
	 *
	 * Given a coffee maker with the default inventory
	 * When we add milk to inventory with positive quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddPositiveMilk() throws InventoryException {
		coffeeMaker.addInventory("0", "1", "0", "0");
	}

	/**
	 * Test ID: 14
	 *
	 * Given a coffee maker with the default inventory
	 * When we add milk to inventory with decimal number quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddDecimalMilk() throws InventoryException {
		coffeeMaker.addInventory("0", "1.5", "0", "0");
	}

	/**
	 * Test ID: 15
	 *
	 * Given a coffee maker with the default inventory
	 * When we add milk to inventory with negative quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddNegativeMilk() throws InventoryException {
		coffeeMaker.addInventory("0", "-1", "0", "0");
	}

	/**
	 * Test ID: 16
	 *
	 * Given a coffee maker with the default inventory
	 * When we add milk to inventory with invalid form of quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInvalidMilk() throws InventoryException {
		coffeeMaker.addInventory("0", "cow", "0", "0");
	}

	/**
	 * Test ID: 17
	 *
	 * Given a coffee maker with the default inventory
	 * When we add sugar to inventory with positive quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddPositiveSugar() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "1", "0");
	}

	/**
	 * Test ID: 18
	 *
	 * Given a coffee maker with the default inventory
	 * When we add sugar to inventory with decimal number quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddDecimalSugar() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "1.5", "0");
	}

	/**
	 * Test ID: 19
	 *
	 * Given a coffee maker with the default inventory
	 * When we add sugar to inventory with negative quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddNegativeSugar() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "-1", "0");
	}

	/**
	 * Test ID: 20
	 *
	 * Given a coffee maker with the default inventory
	 * When we add sugar to inventory with invalid form of quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInvalidSugar() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "corn", "0");
	}

	/**
	 * Test ID: 21
	 *
	 * Given a coffee maker with the default inventory
	 * When we add chocolate to inventory with positive quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddPositiveChocolate() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "0", "1");
	}

	/**
	 * Test ID: 22
	 *
	 * Given a coffee maker with the default inventory
	 * When we add chocolate to inventory with decimal number quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddDecimalChocolate() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "0", "1.5");
	}

	/**
	 * Test ID: 23
	 *
	 * Given a coffee maker with the default inventory
	 * When we add chocolate to inventory with negative quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddNegativeChocolate() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "0", "-1");
	}

	/**
	 * Test ID: 24
	 *
	 * Given a coffee maker with the default inventory
	 * When we add chocolate to inventory with invalid form of quantities
	 * Then we get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInvalidChocolate() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "0", "coco");
	}

	/**
	 * Test ID: 25
	 *
	 * Given a coffee maker with the default inventory
	 * When we add nothing to inventory(call addInventory with all parameter value 0)
	 * Then we do not get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException
	 */
	@Test
	public void testAddAirInventory() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "0", "0");
	}

	/**
	 * Test ID: 26
	 *
	 * Given a coffee maker with the default inventory
	 * When we check the inventory
	 * Then the report will show the initial value of each ingredients.
	 */
	@Test
	public void testCheckInitialInventory() {
		String amtBeforeAdd = "Coffee: 15\nMilk: 15\nSugar: 15\nChocolate: 15\n";
		assertEquals(amtBeforeAdd, coffeeMaker.checkInventory());
	}

	/**
	 * Test ID: 27
	 *
	 * Given a coffee maker with the default inventory
	 * When we add any ingredient to the inventory
	 * Then the report will be update to show current value of each ingredient.
	 *
	 * @throws InventoryException
	 */
	@Test
	public void testCheckAddInventory() throws InventoryException {
		String amtBeforeAdd = "Coffee: 15\nMilk: 15\nSugar: 15\nChocolate: 15\n";
		assertEquals(amtBeforeAdd, coffeeMaker.checkInventory());
		coffeeMaker.addInventory("10", "10", "10", "10");
		String amtAfterAdd = "Coffee: 25\nMilk: 25\nSugar: 25\nChocolate: 25\n";
		assertEquals(amtAfterAdd, coffeeMaker.checkInventory());
	}

	/**
	 * Test ID: 28
	 *
	 * Given a coffee maker with the default inventory
	 * When we make a coffee
	 * Then the report will be update to show current value
	 * 		of each ingredient reduced depend on the recipe.
	 */
	@Test
	public void testCheckInventoryAfterMakeCoffee() {
		String amtBeforeMakeCoffee = "Coffee: 15\nMilk: 15\nSugar: 15\nChocolate: 15\n";
		assertEquals(amtBeforeMakeCoffee, coffeeMaker.checkInventory());

		coffeeMaker.addRecipe(recipe1);
		String amtAfterMakeCoffee = "Coffee: 12\nMilk: 14\nSugar: 14\nChocolate: 15\n";
		coffeeMaker.makeCoffee(0, 50);
		assertEquals(amtAfterMakeCoffee, coffeeMaker.checkInventory());
	}

	/**
	 * Test ID: 29
	 *
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
//		coffeeMaker.addRecipe(recipe1);
		when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
		assertEquals(25, mockCoffeeMaker.makeCoffee(0, 75));
	}

	/**
	 * Test ID: 30
	 *
	 * Given coffee maker
	 * When there are no recipe in the machine and the order has been made
	 * Then coffee maker will return all the user's money.
	 */
	@Test
	public void testMakeCoffeeWithNoRecipe() {
		// empty RecipeBook
		recipeList = new Recipe[] {null, null, null, null};
		when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
		assertEquals(100, mockCoffeeMaker.makeCoffee(0, 100));
	}

	/**
	 * Test ID: 31
	 *
	 * Given coffee maker
	 * When user order coffee with decimal money
	 * Then the coffee will not been made and all the money will be returned.
	 */
	@Test(expected = NumberFormatException.class)
	public void testDecimalMoney() {
//		coffeeMaker.addRecipe(recipe1);
		when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
		assertEquals(Integer.parseInt("20.5"), mockCoffeeMaker.makeCoffee(0, Integer.parseInt("20.5")));
	}

	/**
	 * Test ID: 32
	 *
	 * Given coffee maker
	 * When user order coffee with not enough money
	 * Then the coffee will not been made and all the money will be returned.
	 */
	@Test
	public void testNotEnoughMoney() {
//		coffeeMaker.addRecipe(recipe1);
		when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
		assertEquals(20, mockCoffeeMaker.makeCoffee(0, 20));
	}

	/**
	 * Test ID: 33
	 *
	 * Given coffee maker
	 * When inventory is not enough to make a coffee
	 * Then the coffee will not been made and all the money will be returned.
	 */
	@Test
	public void testOutOfInventoryCoffee() throws RecipeException {
		Recipe bigRecipe = createRecipe("Big coffee", "15", "15", "15", "15", "20");
		recipeList = new Recipe[] {bigRecipe};
		when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
		mockCoffeeMaker.addRecipe(bigRecipe);
		assertEquals(80, mockCoffeeMaker.makeCoffee(0, 100));
		// after this inventory will be empty
		assertEquals(100, mockCoffeeMaker.makeCoffee(0, 100));
	}

	/**
	 * Test ID: 34
	 *
	 * Given coffee maker
	 * When add chocolate in the recipe with negative value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeNegativeChocolate() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtChocolate("-1");
	}

	/**
	 * Test ID: 35
	 *
	 * Given coffee maker
	 * When add chocolate in the recipe with invalid value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeInvalidChocolate() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtChocolate("chocolate");
	}

	/**
	 * Test ID: 36
	 *
	 * Given coffee maker
	 * When add coffee in the recipe with negative value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeNegativeCoffee() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtCoffee("-1");
	}

	/**
	 * Test ID: 37
	 *
	 * Given coffee maker
	 * When add coffee in the recipe with invalid value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeInvalidCoffee() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtCoffee("coffee");
	}

	/**
	 * Test ID: 38
	 *
	 * Given coffee maker
	 * When add milk in the recipe with negative value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeNegativeMilk() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtMilk("-1");
	}

	/**
	 * Test ID: 39
	 *
	 * Given coffee maker
	 * When add milk in the recipe with invalid value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeInvalidMilk() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtMilk("milk");
	}

	/**
	 * Test ID: 40
	 *
	 * Given coffee maker
	 * When add sugar in the recipe with negative value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeNegativeSugar() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtSugar("-1");
	}

	/**
	 * Test ID: 41
	 *
	 * Given coffee maker
	 * When add sugar in the recipe with invalid value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeInvalidSugar() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setAmtSugar("sugar");
	}

	/**
	 * Test ID: 42
	 *
	 * Given coffee maker
	 * When set the prices of the recipe with negative value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeNegativePrice() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setPrice("-1");
	}

	/**
	 * Test ID: 43
	 *
	 * Given coffee maker
	 * When set the prices of the recipe with invalid value
	 * Then we get recipe exception.
	 */
	@Test(expected = RecipeException.class)
	public void testRecipeInvalidPrice() throws RecipeException {
		Recipe recipe = new Recipe();
		recipe.setPrice("price");
	}

}
