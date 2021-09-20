Feature: Make a coffee
  A customer can buy a coffee and purchase with prices. The amount of ingredients in inventory should be correctly calculated.

  Background: purchased a coffee.
    Given The coffee maker is ready to serve
  Scenario: buy a coffee with exact price.
    When The coffee maker have recipe with price 50
    Then The customer choose recipe number 1 pay 50 and get 0 change
  Scenario: buy a coffee with no recipe.
    When The coffee maker have no recipe
    Then The customer choose recipe number 1 pay 50 and get 50 change
  Scenario: buy a coffee with not enough money.
    When The coffee maker have recipe with price 50
    Then The customer choose recipe number 1 pay 25 and get 25 change