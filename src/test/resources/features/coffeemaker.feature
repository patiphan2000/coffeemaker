Feature: Make a coffee
  A customer can buy a coffee and purchase with prices. The amount of ingredients in inventory should be correctly calculated.

  Background: purchased a coffee.
    Given The coffee maker is ready to serve
  Scenario: buy a coffee with exact price.
    When The coffee is purchased with price 100
    Then The change will be 0