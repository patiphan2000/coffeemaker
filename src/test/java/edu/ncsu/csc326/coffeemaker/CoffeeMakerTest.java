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

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import java.lang.reflect.Array;
import java.util.Arrays;

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
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

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
	}
	
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than 
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	/**
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

	@Test
	public void	testEditRecipe() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);
		Recipe changedRecipe = new Recipe();
		changedRecipe.setName("Halo Coffee");
		changedRecipe.setAmtChocolate("1");
		changedRecipe.setAmtCoffee("2");
		changedRecipe.setAmtMilk("2");
		changedRecipe.setAmtSugar("3");
		changedRecipe.setPrice("55");
		coffeeMaker.editRecipe(0, changedRecipe);
		assertEquals("Coffee", coffeeMaker.getRecipes()[0].getName());
	}

	@Test
	public void testEditGhostRecipe() throws RecipeException {
		Recipe changedRecipe = new Recipe();
		changedRecipe.setName("Halo Coffee");
		changedRecipe.setAmtChocolate("1");
		changedRecipe.setAmtCoffee("2");
		changedRecipe.setAmtMilk("2");
		changedRecipe.setAmtSugar("3");
		changedRecipe.setPrice("55");
		coffeeMaker.editRecipe(0, changedRecipe);
		assertNull(coffeeMaker.getRecipes()[0]);
	}

	@Test
	public void testAddAirInventory() throws InventoryException {
		coffeeMaker.addInventory("0", "0", "0", "0");
	}

	@Test(expected = InventoryException.class)
	public void testAddDecimalInventory() throws InventoryException {
		coffeeMaker.addInventory("4.2", "1.1", "2.3", "3.0");
	}

	@Test
	public void testInventoryReport() throws InventoryException {
		String amtBeforeAdd = "Coffee: 15\nMilk: 15\nSugar: 15\nChocolate: 15\n";
		assertEquals(amtBeforeAdd, coffeeMaker.checkInventory());
		coffeeMaker.addInventory("10", "10", "10", "10");
		String amtAfterAdd = "Coffee: 25\nMilk: 25\nSugar: 25\nChocolate: 25\n";
		assertEquals(amtAfterAdd, coffeeMaker.checkInventory());
	}

}
