/*
 * Student name: JEFPERRY ACHU CHI
 * Student number: 041170325
 * Lab Section: 331
 * Assignment: 3
 * Date: March 2025
 * Professor: Sandra
 */
package assn3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The main driver class for the Bread Recipe Manager application.
 * This class manages user interaction, displays menus, handles user input,
 * and coordinates operations with the {@link RecipeManager} class. It ensures
 * proper validation of inputs and provides a user-friendly interface for
 * managing bread orders and generating shopping lists.
 */
public class Assignment3 {
    private static final String YOUR_NAME = "Jefperry Achu Chi"; 
    private static RecipeManager recipeManager;
    private static Map<Recipe, Integer> shoppingList;

    /**
     * The entry point of the application.
     * 1. Loads recipes from the "recipelist.txt" file.
     * 2. Displays a welcome message.
     * 3. Enters an infinite loop for user input and menu processing.
     * 4. Handles exceptions to prevent crashes.
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            recipeManager = new RecipeManager();
            recipeManager.loadRecipes("recipelist.txt");
            System.out.println("Welcome to " + YOUR_NAME + "'s recipe manager.");

            Scanner scanner = new Scanner(System.in);
            shoppingList = new HashMap<>();

            while (true) {
                printMenu();
                int choice = readChoice(scanner);
                handleChoice(choice, scanner);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Could not find recipe file!");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void printMenu() {
        System.out.println("\nPlease select one of the following options:");
        System.out.println("1. Show available recipes.");
        System.out.println("2. Create Shopping List.");
        System.out.println("3. Print Shopping List.");
        System.out.println("4. Quit Program.");
        System.out.println("0. to reprint this menu.");
    }

    /**
     * Reads and validates the user's menu choice.
     * <p>
     * Continuously prompts until a valid choice (0-4) is provided.
     * 
     * @param scanner Scanner instance for input
     * @return Validated menu choice (0-4)
     */
    private static int readChoice(Scanner scanner) {
        while (true) {
            System.out.print("\nPlease enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice >= 0 && choice <= 4) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter 0-4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); 
            }
        }
    }

    /**
     * Processes the user's selected menu option.
     * 
     * @param choice   Selected menu option (0-4)
     * @param scanner  Scanner instance for input
     */
    private static void handleChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                showRecipes();
                break;
            case 2:
                createShoppingList(scanner);
                break;
            case 3:
                printShoppingList(scanner);
                break;
            case 4:
                System.out.println("Goodbye!");
                System.exit(0);
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays all available bread recipes to the user.
     */
    private static void showRecipes() {
        List<Recipe> recipes = recipeManager.getRecipes();
        System.out.println("\nAvailable recipes:");
        for (int i = 0; i < recipes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, recipes.get(i).getName());
        }
    }

    /**
     * Manages the process of creating a new shopping list.
     * 1. Clears existing orders.
     * 2. Continuously prompts for recipe numbers and quantities.
     * 3. Validates inputs and accumulates orders in {@link #shoppingList}.
     * 
     * @param scanner Scanner instance for input
     */
    private static void createShoppingList(Scanner scanner) {
        shoppingList.clear();
        List<Recipe> recipes = recipeManager.getRecipes();
        while (true) {
            System.out.print("\nEnter recipe number (1-" + recipes.size() + ") or -1 to finish: ");
            int recipeNum = readNumber(scanner, 1, recipes.size());
            if (recipeNum == -1) break;

            int quantity = readPositiveNumber(scanner, "How many loaves? ");
            Recipe selected = recipes.get(recipeNum - 1);
            shoppingList.put(selected, shoppingList.getOrDefault(selected, 0) + quantity);
        }
    }

    /**
     * Reads a number within a specified range or -1 to cancel.
     * 
     * @param scanner Scanner instance for input
     * @param min     Minimum valid value (inclusive)
     * @param max     Maximum valid value (inclusive)
     * @return Valid number or -1 if canceled
     */
    private static int readNumber(Scanner scanner, int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();
                if (num == -1) return -1;
                if (num >= min && num <= max) return num;
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
    }

    /**
     * Reads a positive integer (≥ 1) from the user.
     * 
     * @param scanner Scanner instance for input
     * @param prompt  Prompt message to display
     * @return Valid positive integer
     */
    private static int readPositiveNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();
                if (num >= 1) return num;
                System.out.println("Quantity must be at least 1.");
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    /**
     * Displays the current shopping list and offers a save option.
     * 
     * @param scanner Scanner instance for input
     */
    private static void printShoppingList(Scanner scanner) {
        if (shoppingList.isEmpty()) {
            System.out.println("Shopping list is empty.");
            return;
        }

        Map<String, Double> totals = recipeManager.calculateShoppingList(shoppingList);

        // Display selected recipes
        System.out.println("\nYour selected recipes:");
        for (Map.Entry<Recipe, Integer> entry : shoppingList.entrySet()) {
            System.out.printf("%d %s loaf/loaves.%n",
                    entry.getValue(), entry.getKey().getName());
        }

        // Display totals
        System.out.println("\nYou will need a total of:");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            String ingredient = entry.getKey();
            double quantity = entry.getValue();
            if ("eggs".equalsIgnoreCase(ingredient)) {
                System.out.printf("%.0f %s(s)%n", quantity, ingredient);
            } else {
                System.out.printf("%.1f grams of %s%n", quantity, ingredient);
            }
        }

        // Save option
        System.out.print("\nDo you want to save this list (Y/n)? ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.isEmpty() || response.equals("y")) {
            try {
                recipeManager.saveShoppingList(shoppingList, "shoppinglist.txt");
                System.out.println("Shopping list saved to shoppinglist.txt");
            } catch (IOException e) {
                System.err.println("Error saving shopping list: " + e.getMessage());
            }
        }
    }
}
