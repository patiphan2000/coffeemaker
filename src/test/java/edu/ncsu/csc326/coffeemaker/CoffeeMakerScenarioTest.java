package edu.ncsu.csc326.coffeemaker;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;
import io.cucumber.java.en.*;

/**
 * Unit tests for CoffeeMaker class.
 *
 * @author Patiphan Srichai
 */
public class CoffeeMakerScenarioTest {

    /**
     * The object under test.
     */
    private CoffeeMaker coffeeMaker;
    private CoffeeMaker mockCoffeeMaker;
    private Inventory inventory;
    private RecipeBook recipeBook;

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

    @Given("The coffee maker is ready to serve")
    public void the_coffee_maker_is_ready_to_serve() throws RecipeException {
        inventory = new Inventory();
        recipeBook = mock(RecipeBook.class);
        coffeeMaker = new CoffeeMaker();
        mockCoffeeMaker = new CoffeeMaker(recipeBook, inventory);

        recipe1 = createRecipe("Coffee","0","3","1","1","50");
        recipe2 = createRecipe("Mocha","20","3","1","1","70");
        recipe3 = createRecipe("Latte","0","3","3","1","100");
        recipe4 = createRecipe("Hot Chocolate","4","0","1","1","65");

        // Stub of recipes (original).
        recipeList = new Recipe[] {recipe1, recipe2, recipe3, recipe4};
    }

    @When("The coffee maker have recipe with price {int}")
    public void the_coffee_maker_have_recipe_with_price(Integer price) throws RecipeException {
        Recipe bigRecipe = createRecipe("exact coffee", "1", "1", "1", "1", price.toString());
        recipeList = new Recipe[] {bigRecipe};
        when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
    }

    @Then("The customer choose recipe number {int} pay {int} and get {int} change")
    public void the_customer_choose_recipe_number_pay_and_get_change(Integer recipeNum, Integer pay, Integer change) {
        assertEquals(change.intValue(), mockCoffeeMaker.makeCoffee(recipeNum.intValue()-1, pay));
    }

    @When("The coffee maker have no recipe")
    public void the_coffee_maker_have_no_recipe() {
        recipeList = new Recipe[] {null, null, null};
        when(mockCoffeeMaker.getRecipes()).thenReturn(recipeList);
    }


}
