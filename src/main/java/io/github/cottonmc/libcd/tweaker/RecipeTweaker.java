package io.github.cottonmc.libcd.tweaker;

import com.google.common.collect.ImmutableMap;
import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.impl.IngredientAccessUtils;
import io.github.cottonmc.libcd.impl.RecipeMapAccessor;
import io.github.cottonmc.libcd.impl.ReloadListenersAccessor;
import io.github.cottonmc.libcd.util.NbtMatchType;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_1860;
import net.minecraft.class_1863;
import net.minecraft.class_1867;
import net.minecraft.class_1869;
import net.minecraft.class_2371;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_3859;
import net.minecraft.class_3861;
import net.minecraft.class_3862;
import net.minecraft.class_3920;
import net.minecraft.class_3956;
import net.minecraft.class_3975;
import net.minecraft.recipe.*;
import java.util.*;
import java.util.concurrent.Executor;

public class RecipeTweaker implements Tweaker {
	public static final RecipeTweaker INSTANCE = new RecipeTweaker();
	private class_1863 recipeManager;
	private int triedRecipeCount;
	private int recipeCount;
	private int removeCount;
	private Map<class_3956<?>, List<class_1860<?>>> toAdd = new HashMap<>();
	private Map<class_3956<?>, List<class_2960>> toRemove = new HashMap<>();

	/**
	 * Used during data pack loading to set up recipe adding.
	 * DO NOT CALL THIS YOURSELF, EVER. IT WILL LIKELY MESS THINGS UP.
	 */
	@Override
	public void prepareReload(class_3300 manager) {
		triedRecipeCount = -1;
		recipeCount = 0;
		removeCount = 0;
		toAdd.clear();
		toRemove.clear();
		if (manager instanceof ReloadListenersAccessor) {
			List<class_3302> listeners = ((ReloadListenersAccessor)manager).libcd_getListeners();
			for (class_3302 listener : listeners) {
				if (listener instanceof class_1863) {
					this.recipeManager = (class_1863)listener;
					return;
				}
			}
			LibCD.logger.error("No recipe manager was found! Tweaker cannot register recipes!");
			throw new IllegalStateException("No recipe manager was found! Tweaker cannot register recipes!");
		}
		LibCD.logger.error("No reload listeners accessor found! Tweaker cannot register recipes!");
		throw new IllegalStateException("No reload listeners accessor found! Tweaker cannot register recipes!");
	}

	/**
	 * Used during data pack applying to directly apply recipes.
	 * This is "safe" to call yourself, but will result in a *lot* of log spam.
	 * NOTE: for some reason, Mojang decided to make the recipe map entirely immutable!
	 *   I don't like this but I respect it, so this code will preserve the map's immutability,
	 *   even though it might be a better idea to leave it mutable.
	 */
	@Override
	public void applyReload(class_3300 manager, Executor executor) {
		Map<class_3956<?>, Map<class_2960, class_1860<?>>> recipeMap = new HashMap<>(((RecipeMapAccessor)recipeManager).libcd_getRecipeMap());
		Set<class_3956<?>> types = new HashSet<>(recipeMap.keySet());
		types.addAll(toAdd.keySet());
		for (class_3956<?> type : types) {
			Map<class_2960, class_1860<?>> map = new HashMap<>(recipeMap.getOrDefault(type, new HashMap<>()));
			for (class_1860<?> recipe : toAdd.getOrDefault(type, new ArrayList<>())) {
				class_2960 id = recipe.method_8114();
				if (map.containsKey(id)) {
					LibCD.logger.error("Failed to add recipe from tweaker - duplicate recipe ID: " + id);
				} else try {
					map.put(id, recipe);
					recipeCount++;
				} catch (Exception e) {
					LibCD.logger.error("Failed to add recipe from tweaker - " + e.getMessage());
				}
			}
			for (class_2960 recipeId : toRemove.getOrDefault(type, new ArrayList<>())) {
				if (map.containsKey(recipeId)) {
					map.remove(recipeId);
					removeCount++;
				} else LibCD.logger.error("Could not find recipe to remove: " + recipeId.toString());
			}
			recipeMap.put(type, ImmutableMap.copyOf(map));
		}
		((RecipeMapAccessor)recipeManager).libcd_setRecipeMap(ImmutableMap.copyOf(recipeMap));
	}

	@Override
	public String getApplyMessage() {
		return recipeCount + " " + (recipeCount == 1? "recipe" : "recipes" + (removeCount == 0? "" : " (" + removeCount + " removed)"));
	}

	/**
	 * Generate a recipe ID. Call this from Java tweaker classes.
	 * @param output The output stack of the recipe.
	 * @return A unique identifier for the recipe.
	 */
	public class_2960 getRecipeId(class_1799 output) {
		String resultName = class_2378.field_11142.method_10221(output.method_7909()).method_12832();
		triedRecipeCount++;
		return new class_2960(LibCD.MODID, "tweaked/"+resultName+"-"+triedRecipeCount);
	}

	/**
	 * Remove a recipe from the recipe manager.
	 * @param id The id of the recipe to remove.
	 */
	public void removeRecipe(String id) {
		class_2960 formatted = new class_2960(id);
		Optional<? extends class_1860<?>> opt = recipeManager.method_8130(formatted);
		if (opt.isPresent()) {
			class_1860<?> recipe = opt.get();
			class_3956<?> type = recipe.method_17716();
			if (!toRemove.containsKey(type)) toRemove.put(type, new ArrayList<>());
			List<class_2960> removal = toRemove.get(type);
			removal.add(formatted);
		}
	}

	/**
	 * Register a recipe to the recipe manager.
	 * @param recipe A constructed recipe.
	 */
	public void addRecipe(class_1860<?> recipe) {
		class_3956<?> type = recipe.method_17716();
		if (!toAdd.containsKey(type)) {
			toAdd.put(type, new ArrayList<>());
		}
		List<class_1860<?>> recipeList = toAdd.get(type);
		recipeList.add(recipe);
	}

	/**
	 * Get a recipe ingredient from an item stack. Call this from java tweaker classes.
	 * @param stack The item stack to make an ingredient for.
	 * @return The wrapped ingredient of the stack.
	 */
	public class_1856 ingredientForStack(class_1799 stack) {
		return RecipeParser.hackStackIngredients(stack);
	}

	/**
	 * Make an Ingredient object to pass to recipes from a string of inputs.
	 * @param nbtMatch The NBT matching type to use: "none", "fuzzy", or "exact".
	 * @param inputs The string forms of inputs to add to the Ingredient.
	 * @return An Ingredient object to pass to recipes.
	 * @throws TweakerSyntaxException If an input is malformed.
	 */
	public class_1856 makeIngredient(String nbtMatch, String...inputs) {
		List<class_1799> stacks = new ArrayList<>();
		NbtMatchType match = NbtMatchType.forName(nbtMatch);
		for (String input : inputs) {
			try {
				class_1799[] in = ((IngredientAccessUtils)(Object)RecipeParser.processIngredient(input)).libcd_getStackArray();
				stacks.addAll(Arrays.asList(in));
			} catch (TweakerSyntaxException e) {
				LibCD.logger.error("Could not add stack to ingredient: malformed stack string {}", input);
			}
		}
		class_1856 ret = RecipeParser.hackStackIngredients(stacks.toArray(new class_1799[]{}));
		((IngredientAccessUtils)(Object)ret).libcd_setMatchType(match);
		return ret;
	}

	public void addShaped(Object[][] inputs, class_1799 output) {
		addShaped(inputs, output, "");
	}

	/**
	 * Add a shaped recipe from a 2D array of inputs, like a standard CraftTweaker recipe.
	 * @param inputs the 2D array (array of arrays) of inputs to use.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addShaped(Object[][] inputs, class_1799 output, String group) {
		try {
			Object[] processed = RecipeParser.processGrid(inputs);
			int width = inputs[0].length;
			int height = inputs.length;
			addShaped(processed, output, width, height, group);
		} catch (Exception e) {
			LibCD.logger.error("Error parsing shaped recipe - " + e.getMessage());
		}
	}

	public void addShaped(String[] inputs, class_1799 output, int width, int height) {
		addShaped(inputs, output, width, height, "");
	}

	/**
	 * Register a shaped crafting recipe from a 1D array of inputs.
	 * @param inputs The input item or tag ids required in order: left to right, top to bottom.
	 * @param output The output of the recipe.
	 * @param width How many rows the recipe needs.
	 * @param height How many columns the recipe needs.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addShaped(Object[] inputs, class_1799 output, int width, int height, String group){
		class_2960 recipeId = getRecipeId(output);
		try {
			class_2371<class_1856> ingredients = class_2371.method_10211();
			for (int i = 0; i < Math.min(inputs.length, width * height); i++) {
				Object id = inputs[i];
				if (id.equals("")) continue;
				ingredients.add(i, RecipeParser.processIngredient(id));
			}
			addRecipe(new class_1869(recipeId, group, width, height, ingredients, output));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing shaped recipe - " + e.getMessage());
		}
	}

	public void addShaped(String[] pattern, Map<String, Object> dictionary, class_1799 output) {
		addShaped(pattern, dictionary, output, "");
	}

	/**
	 * Register a shaped crafting recipe from a pattern and dictionary.
	 * @param pattern A crafting pattern like one you'd find in a vanilla recipe JSON.
	 * @param dictionary A map of single characters to item or tag ids.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addShaped(String[] pattern, Map<String, Object> dictionary, class_1799 output, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			pattern = RecipeParser.processPattern(pattern);
			Map<String, class_1856> map = RecipeParser.processDictionary(dictionary);
			int x = pattern[0].length();
			int y = pattern.length;
			class_2371<class_1856> ingredients = RecipeParser.getIngredients(pattern, map, x, y);
			addRecipe(new class_1869(recipeId, group, x, y, ingredients, output));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing shaped recipe - " + e.getMessage());
		}
	}

	public void addShapeless(Object[] inputs, class_1799 output) {
		addShapeless(inputs, output, "");
	}

	/**
	 * Register a shapeless crafting recipe from an array of inputs.
	 * @param inputs A list of input item or tag ids required for the recipe.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addShapeless(Object[] inputs, class_1799 output, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			class_2371<class_1856> ingredients = class_2371.method_10211();
			for (int i = 0; i < Math.min(inputs.length, 9); i++) {
				Object id = inputs[i];
				if (id.equals("")) continue;
				ingredients.add(i, RecipeParser.processIngredient(id));
			}
			addRecipe(new class_1867(recipeId, group, output, ingredients));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing shapeless recipe - " + e.getMessage());
		}
	}

	public void addSmelting(Object input, class_1799 output, int ticks, float xp) {
		addSmelting(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to smelt in a standard furnace.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 200
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addSmelting(Object input, class_1799 output, int cookTime, float xp, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			class_1856 ingredient = RecipeParser.processIngredient(input);
			addRecipe(new class_3861(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing smelting recipe - " + e.getMessage());
		}
	}

	public void addBlasting(Object input, class_1799 output, int ticks, float xp) {
		addBlasting(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to smelt in a blast furnace.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 100
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addBlasting(Object input, class_1799 output, int cookTime, float xp, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			class_1856 ingredient = RecipeParser.processIngredient(input);
			addRecipe(new class_3859(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing blasting recipe - " + e.getMessage());
		}
	}

	public void addSmoking(Object input, class_1799 output, int ticks, float xp) {
		addSmoking(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to cook in a smoker.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 100
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addSmoking(Object input, class_1799 output, int cookTime, float xp, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			class_1856 ingredient = RecipeParser.processIngredient(input);
			addRecipe(new class_3862(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing smokig recipe - " + e.getMessage());
		}
	}

	public void addCampfire(Object input, class_1799 output, int ticks, float xp) {
		addCampfire(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to cook on a campfire.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 600
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addCampfire(Object input, class_1799 output, int cookTime, float xp, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			class_1856 ingredient = RecipeParser.processIngredient(input);
			addRecipe(new class_3920(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing campfire recipe - " + e.getMessage());
		}
	}

	public void addStonecutting(Object input, class_1799 output) {
		addStonecutting(input, output, "");
	}

	/**
	 * Register a recipe to cut in the stonecutter.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public void addStonecutting(Object input, class_1799 output, String group) {
		class_2960 recipeId = getRecipeId(output);
		try {
			class_1856 ingredient = RecipeParser.processIngredient(input);
			addRecipe(new class_3975(recipeId, group, ingredient, output));
		} catch (Exception e) {
			LibCD.logger.error("Error parsing stonecutter recipe - " + e.getMessage());
		}
	}

}
