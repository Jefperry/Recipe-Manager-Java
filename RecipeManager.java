/*
 * Student name: JEFPERRY ACHU CHI
 * Student number: 041170325
 * Lab Section: 331
 * Assignment: 3
 * Date: March 2025
 * Professor: Sandra
 */
package assn3;

import java.io.*;
import java.util.*;

/**
 * Manages bread recipes and shopping list operations.
 * This class is responsible for:
 * Loading recipes from a text file
 *  Calculating aggregated ingredient requirements
 *   Saving shopping lists to a file
 * It serves as the core logic layer between the {@link Assignment3} driver
 * class and the data storage mechanism.
 * Key features:
 *   Uses relative paths for file operations
 *  Handles recipe parsing from custom file formats
 * Performs ingredient quantity calculations
 * Validates and formats output for user display
 * 
 */
public class RecipeManager {
    private List<Recipe> recipes;

    /**
     * Creates a new RecipeManager with empty recipe storage.
     */
    public RecipeManager() {
        this.recipes = new ArrayList<>();
    }

    /**
     * Loads bread recipes from a text file using a relative path
     * The file must follow this format:	
     * Recipe [Name]
     * ingredient1 quantity1
     * ingredient2 quantity2
     * ... (5 ingredients per recipe)
     *
     * Recipes are separated by blank lines,Quantities are stored as doubles
     *
     * @param filename Name of the recipe file (e.g., "recipelist.txt")
     * @throws FileNotFoundException If the file cannot be found in the working directory
     */
    public void loadRecipes(String filename) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            Recipe currentRecipe = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("Recipe ")) {
                    String name = line.substring(7);
                    currentRecipe = new Recipe(name);
                    recipes.add(currentRecipe);
                } else {
                    String[] parts = line.split(" ");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid line: " + line);
                    }    
                    String ingredient = parts[0];
                    double quantity = Double.parseDouble(parts[1]);
                    currentRecipe.addIngredient(ingredient, quantity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading recipes", e);
        }
    }

    /**
     * Gets all loaded bread recipes.
     *
     * @return Unmodifiable list of {@link Recipe} objects
     */
    public List<Recipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    /**
     * Calculates total ingredients required for multiple bread orders.
     * Multiplies each recipe's ingredients by the order quantity and aggregates
     * across all recipes. Automatically excludes ingredients with 0 total quantity.
     * @param orders Map of recipes to order quantities
     * @return Map of ingredient names to total required quantities
     */
    public Map<String, Double> calculateShoppingList(Map<Recipe, Integer> orders) {
        Map<String, Double> totals = new HashMap<>();

        for (Map.Entry<Recipe, Integer> entry : orders.entrySet()) {
            Recipe recipe = entry.getKey();
            int count = entry.getValue();

            for (Map.Entry<String, Double> ing : recipe.getIngredients().entrySet()) {
                String name = ing.getKey();
                double perLoaf = ing.getValue();
                totals.put(name, totals.getOrDefault(name, 0.0) + (perLoaf * count));
            }
        }

        // Remove zero-quantity ingredients
        totals.entrySet().removeIf(e -> e.getValue() == 0.0);
        return totals;
    }

    /**
     *   Saves a formatted shopping list to a text file.
     *   The output file will contain:
     *   List of ordered recipes with quantities
     *   Total ingredients required
     *  Special formatting for eggs (count vs. weight)
     * @param orders  Map of recipes to order quantities
     * @param filename Output file name (e.g., "shoppinglist.txt")
     * @throws IOException If file writing fails
     */
    public void saveShoppingList(Map<Recipe, Integer> orders, String filename) throws IOException {
        Map<String, Double> totals = calculateShoppingList(orders);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write orders section
            writer.println("Your selected recipes:");
            for (Map.Entry<Recipe, Integer> entry : orders.entrySet()) {
                writer.printf("%d %s loaf/loaves.%n",
                        entry.getValue(), entry.getKey().getName());
            }

            // Write ingredients section
            writer.println("\nYou will need a total of:");
            for (Map.Entry<String, Double> entry : totals.entrySet()) {
                String ingredient = entry.getKey();
                double quantity = entry.getValue();

                if ("eggs".equalsIgnoreCase(ingredient)) {
                    writer.printf("%.0f %s(s)%n", quantity, ingredient);
                } else {
                    writer.printf("%.1f grams of %s%n", quantity, ingredient);
                }
            }
        }
    }
}
