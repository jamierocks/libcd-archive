package io.github.cottonmc.libcd.tweaker;

import com.google.common.collect.Sets;
import io.github.cottonmc.libcd.LibCD;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1856;
import net.minecraft.class_2371;
import net.minecraft.class_2960;
import net.minecraft.class_3489;
import net.minecraft.class_3494;

/**
 * Helper class to make public versions private recipe methods
 */
public class RecipeParser {

	/**
	 * Get an Ingredient from a string item or tag id.
	 * @param input The id to use, with a # at the front if it's a tag or -> between two ids for a getter
	 * @return the Ingredient for the given id
	 */
	public static class_1856 processIngredient(String input) throws TweakerSyntaxException {
		if (input.indexOf('#') == 0) {
			String tag = input.substring(1);
			class_3494<class_1792> itemTag = class_3489.method_15106().method_15193(new class_2960(tag));
			if (itemTag == null) throw new TweakerSyntaxException("Failed to get item tag for input: " + input);
			return class_1856.method_8106(itemTag);
		} else if (input.indexOf('&') == 0) {
			LibCD.logger.warn("Use of deprecated potion-getting method in '" + input + "', this will be removed soon!");
			class_1799 stack = TweakerUtils.getPotion(input.substring(1));
			if (stack.method_7960()) throw new TweakerSyntaxException("Failed to get potion for input: " + input);
			return class_1856.method_8101(stack);
		} else if (input.contains("->")) {
			class_1799 stack = TweakerUtils.getSpecialStack(input);
			if (stack.method_7960()) throw new TweakerSyntaxException("Failed to get special stack for input: " + input);
			return class_1856.method_8101(stack);
		} else {
			class_1792 item = TweakerUtils.getItem(input);
			if (item == class_1802.field_8162) throw new TweakerSyntaxException("Failed to get item for input: " + input);
			return class_1856.method_8091(item);
		}
	}

	/**
	 * Split a getter string into the ids of the getter/id.
	 * @param base The base getter string to split.
	 * @return A two-item array of the two parts of the getter.
	 */
	static String[] processGetter(String base) {
		String[] split = new String[2];
		int splitter = base.indexOf("->");
		split[0] = base.substring(0, splitter);
		split[1] = base.substring(splitter + 2);
		return split;
	}

	/**
	 * Process a grid of inputs CraftTweaker-style.
	 * @param inputs The array of string arrays to process inputs from
	 * @return The inputs converted into a single string array if the grid is valid
	 */
	public static String[] processGrid(String[][] inputs) throws TweakerSyntaxException {
		if (inputs.length > 3) throw new TweakerSyntaxException("Invalid pattern: too many columns, 3 is maximum");
		if (inputs.length == 0) throw new TweakerSyntaxException("Invalid pattern: empty pattern is not allowed");
		int width = inputs[0].length;
		String[] output = new String[inputs.length * inputs[0].length];
		for (int i = 0; i < inputs.length; i++) {
			String[] row = inputs[i];
			if (row.length > 3) throw new TweakerSyntaxException("Invalid pattern: too many columns, 3 is maximum");
			if (row.length != width) throw new TweakerSyntaxException("Invalid pattern: each row must be the same width");
			for (int j = 0; j < width; j++) {
				output[j + inputs.length * i] = inputs[i][j];
			}
		}
		return output;
	}

	/**
	 * validate and parse a recipe pattern.
	 * @param pattern up to three strings of up to three characters each for the pattern
	 * @return processed pattern
	 */
	public static String[] processPattern(String... pattern) throws TweakerSyntaxException {
		if (pattern.length > 3) {
			throw new TweakerSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		} else if (pattern.length == 0) {
			throw new TweakerSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for (int i = 0; i < pattern.length; i++) {
				String row = pattern[i];
				if (row.length() > 3) {
					throw new TweakerSyntaxException("Invalid pattern: too many columns, 3 is maximum");
				}

				if (i > 0 && pattern[0].length() != row.length()) {
					throw new TweakerSyntaxException("Invalid pattern: each row must be the same width");
				}

				pattern[i] = row;
			}
			int nextIndex = 2147483647;
			int highIndex = 0;
			int checked = 0;
			int sinceLastEmpty = 0;

			for (int i = 0; i < pattern.length; ++i) {
				String input = pattern[i];
				nextIndex = Math.min(nextIndex, findNextIngredient(input));
				int lastIndex = findNextIngredientReverse(input);
				highIndex = Math.max(highIndex, lastIndex);
				if (lastIndex < 0) {
					if (checked == i) {
						++checked;
					}

					++sinceLastEmpty;
				} else {
					sinceLastEmpty = 0;
				}
			}

			if (pattern.length == sinceLastEmpty) {
				return new String[0];
			} else {
				String[] combined = new String[pattern.length - sinceLastEmpty - checked];

				for (int i = 0; i < combined.length; ++i) {
					combined[i] = pattern[i + checked].substring(nextIndex, highIndex + 1);
				}

				return combined;
			}
		}
	}

	/**
	 * Process dictionaries into a Recipe-readable form.
	 * @param dictionary a map of keys to values for a recipe to parse.
	 * @return A map of string keys to ingredient values that a Recipe can read.
	 */
	public static Map<String, class_1856> processDictionary(Map<String, String> dictionary) throws TweakerSyntaxException {
		Map<String, class_1856> map = new HashMap<>();
		for (Map.Entry<String, String> entry : dictionary.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new TweakerSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new TweakerSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}
			map.put(entry.getKey(), processIngredient(entry.getValue()));
		}
		map.put(" ", class_1856.field_9017);
		return map;
	}

	/**
	 * Compile a pattern and dictionary into a full ingredient list.
	 * @param pattern A patern parsed by processPattern.
	 * @param dictionary A dictionary parsed by processDictionary.
	 * @param x How many columns there are.
	 * @param y How many rows there are.
	 * @return A defaulted list of ingredients.
	 */
	public static class_2371<class_1856> getIngredients(String[] pattern, Map<String, class_1856> dictionary, int x, int y) throws TweakerSyntaxException {
		class_2371<class_1856> ingredients = class_2371.method_10213(x * y, class_1856.field_9017);
		Set<String> keys = Sets.newHashSet(dictionary.keySet());
		keys.remove(" ");

		for(int i = 0; i < pattern.length; i++) {
			for(int j = 0; j < pattern[i].length(); j++) {
				String key = pattern[i].substring(j, j + 1);
				class_1856 ingredient = dictionary.get(key);
				if (ingredient == null) {
					throw new TweakerSyntaxException("Pattern references symbol '" + key + "' but it's not defined in the key");
				}

				keys.remove(key);
				ingredients.set(j + x * i, ingredient);
			}
		}

		if (!keys.isEmpty()) {
			throw new TweakerSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
		} else {
			return ingredients;
		}
	}

	/**
	 * Skip forwards through a recipe row to find an ingredient key
	 * @param input a recipe row to parse
	 * @return index for the next ingredient character
	 */
	private static int findNextIngredient(String input) {
		int i;
		for (i = 0; i < input.length() && input.charAt(i) == ' '; i++) { }
		return i;
	}

	/**
	 * Skip backwards through a recipe row to find an ingredient key
	 * @param input a recipe row to parse
	 * @return index for the next ingredient character
	 */
	private static int findNextIngredientReverse(String input) {
		int i;
		for (i = input.length() - 1; i >= 0 && input.charAt(i) == ' '; i--) { }
		return i;
	}
}