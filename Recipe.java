/*
 * Student name: JEFPERRY ACHU CHI
 * Student number: 041170325
 * Lab Section: 331
 * Assignment: 3
 * Date: March 2025
 * Professor: Sandra
 */
package assn3;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a bread recipe with its required ingredients and quantities
 * This class acts as a data container for bread recipes. Each recipe has:
 * 
 *  A unique name (e.g., "Brioche")
 *  A map of ingredients to their required quantities per loaf
 * 
 * Quantities are stored as {@code double} values to accommodate both:
 *   Weight-based measurements (e.g., grams of flour)
 *   Count-based measurements (e.g., number of eggs)
 * This class provides methods to:
 * Add ingredients to the recipe
 * Retrieve ingredient quantities
 *  Get the recipe's name and full ingredient list
 * 
 */
public class Recipe {
    private String name;
    private Map<String, Double> ingredients;

    /**
     * Creates a new Recipe with the specified name
     * Initializes an empty ingredient map. Ingredients must be added
     * using {@link #addIngredient(String, double)}.
     *
     * @param name The name of the bread recipe (e.g., "Challah")
     */
    public Recipe(String name) {
        this.name = name;
        this.ingredients = new HashMap<>();
    }

    /**
     * Gets the name of the recipe.
     *
     * @return The recipe's name (e.g., "Pita")
     */
    public String getName() {
        return name;
    }

    /**
     * Gets all ingredients required for this recipe
     * The returned map is a reference to the internal storage. Changes
     * to the map will affect the recipe's data.
     *
     * @return A map of ingredient names to quantities per loaf
     */
    public Map<String, Double> getIngredients() {
        return ingredients;
    }

    /**
     * Adds an ingredient to the recipe with a specified quantity.
     * <p>
     * If the ingredient already exists, its quantity will be overwritten.
     * Use this method to update quantities for existing ingredients.
     *
     * @param ingredient The name of the ingredient (e.g., "yeast")
     * @param quantity   The required amount per loaf (e.g., 0.5 for 0.5g)
     */
    public void addIngredient(String ingredient, double quantity) {
        ingredients.put(ingredient, quantity);
    }

    /**
     * Retrieves the quantity of a specific ingredient required per loaf.
     * <p>
     * If the ingredient is not part of the recipe, returns 0.0.
     *
     * @param ingredient The ingredient to query (e.g., "sugar")
     * @return The required quantity, or 0.0 if not present
     */
    public double getIngredientQuantity(String ingredient) {
        return ingredients.getOrDefault(ingredient, 0.0);
    }
}
