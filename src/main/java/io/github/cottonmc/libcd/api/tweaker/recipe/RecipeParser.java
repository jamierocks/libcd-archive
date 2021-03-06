package io.github.cottonmc.libcd.api.tweaker.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tag.TagHelper;
import io.github.cottonmc.libcd.api.tweaker.util.TweakerUtils;
import io.github.cottonmc.libcd.api.util.NbtMatchType;
import io.github.cottonmc.libcd.api.util.MutableStack;
import io.github.cottonmc.libcd.impl.IngredientAccessUtils;
import io.netty.buffer.Unpooled;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1856;
import net.minecraft.class_2371;
import net.minecraft.class_2378;
import net.minecraft.class_2509;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_3494;
import net.minecraft.class_5323;
import java.util.*;

/**
 * Helper class to make public versions private recipe methods
 */
public class RecipeParser {

	/**
	 * Get an Ingredient from a string item or tag id.
	 * @param input The id to use, with a # at the front if it's a tag or -> between two ids for a getter
	 * @return the Ingredient for the given id
	 */
	public static class_1856 processIngredient(Object input) throws CDSyntaxError {
		if (input instanceof class_1856) return (class_1856) input;
		else if (input instanceof MutableStack) {
			class_1799 stack = ((MutableStack) input).get();
			class_1856 ing = hackStackIngredients(stack);
			if (stack.method_7985()) {
				((IngredientAccessUtils) (Object) ing).libcd$setMatchType(NbtMatchType.EXACT);
			}
			return ing;
		}
		else if (input instanceof class_1799) {
			class_1799 stack = (class_1799) input;
			class_1856 ing = hackStackIngredients(stack);
			if (stack.method_7985()) {
				((IngredientAccessUtils) (Object) ing).libcd$setMatchType(NbtMatchType.EXACT);
			}
			return ing;
		} else if (input instanceof class_1799[]) {
			class_1799[] stacks = (class_1799[]) input;
			boolean needsTags = false;
			for (int i = 0; i < stacks.length; i++) {
				class_1799 stack = stacks[i];
				if (stack.method_7985()) {
					needsTags = true;
				}
			}
			class_1856 ing = hackStackIngredients(stacks);
			if (needsTags) {
				((IngredientAccessUtils) (Object) ing).libcd$setMatchType(NbtMatchType.EXACT);
			}
			return ing;
		} else if (input instanceof String) {
			String in = (String) input;
			int index = in.indexOf('{');
			String nbt = "";
			NbtMatchType type = NbtMatchType.NONE;
			List<class_1799> stacks = new ArrayList<>();
			if (index != -1) {
				int andIndex = in.indexOf('&');
				if (andIndex != -1) {
					type = NbtMatchType.forName(in.substring(andIndex+1));
					in = in.substring(0, andIndex);
				}
				nbt = in.substring(index);
				in = in.substring(0, index);
			}
			if (in.indexOf('#') == 0) {
				String tag = in.substring(1);
				class_3494<class_1792> itemTag = class_5323.method_29223().method_29220().method_15193(new class_2960(tag));
				if (itemTag == null) throw new CDSyntaxError("Failed to get item tag for input: " + in);
				for (class_1792 item : itemTag.method_15138()) {
					stacks.add(new class_1799(item));
				}
			} else if (in.contains("->")) {
				class_1799 stack = TweakerUtils.INSTANCE.getSpecialStack(in);
				if (stack.method_7960())
					throw new CDSyntaxError("Failed to get special stack for input: " + in);
				stacks.add(stack);
				type = NbtMatchType.EXACT;
			} else {
				class_1792 item = TweakerUtils.INSTANCE.getItem(in);
				if (item == class_1802.field_8162) throw new CDSyntaxError("Failed to get item for input: " + in);
				stacks.add(new class_1799(item));
			}
			if (!nbt.equals("")) {
				for (class_1799 stack : stacks) {
					if (!stack.method_7985() || stack.method_7969().isEmpty()) TweakerUtils.INSTANCE.addNbtToStack(stack, nbt);
				}
			}
			class_1856 ret = hackStackIngredients(stacks.toArray(new class_1799[]{}));
			((IngredientAccessUtils)(Object)ret).libcd$setMatchType(type);
			return ret;
		}
		else throw new CDSyntaxError("Illegal object passed to recipe parser of type " + input.getClass().getName());
	}

	public static class_1799 processItemStack(Object input) throws CDSyntaxError {
		if (input instanceof class_1799) return (class_1799) input;
		else if (input instanceof MutableStack) return ((MutableStack) input).get();
		else if (input instanceof String) {
			String in = (String) input;
			int atIndex = in.lastIndexOf('@');
			int nbtIndex = in.indexOf('{');
			int count = 1;
			if (atIndex != -1 && atIndex > in.lastIndexOf('}')) {
				count = Integer.parseInt(in.substring(atIndex + 1));
				in = in.substring(0, atIndex);
			}
			String nbt = "";
			if (nbtIndex != -1) {
				nbt = in.substring(nbtIndex);
				in = in.substring(0, nbtIndex);
			}
			class_1792 item;
			if (in.indexOf('#') == 0) {
				String tag = in.substring(1);
				class_3494<class_1792> itemTag = class_5323.method_29223().method_29220().method_15193(new class_2960(tag));
				if (itemTag == null) throw new CDSyntaxError("Failed to get item tag for output: " + in);
				item = TagHelper.ITEM.getDefaultEntry(itemTag);
			} else if (in.contains("->")) {
				class_1799 stack = TweakerUtils.INSTANCE.getSpecialStack(in);
				if (stack.method_7960())
					throw new CDSyntaxError("Failed to get special stack for output: " + in);
				if (stack.method_7946()) {
					stack.method_7939(count);
				}
				return stack;
			} else {
				item = TweakerUtils.INSTANCE.getItem(in);
			}

			class_1799 stack = new class_1799(item, count);
			if (!nbt.equals("")) {
				TweakerUtils.INSTANCE.addNbtToStack(stack, nbt);
			}

			return stack;
		}
		else throw new CDSyntaxError("Illegal object passed to recipe parser of type " + input.getClass().getName());
	}

	/**
	 * Split a factory string into the ids of the factory/id.
	 * @param base The base factory string to split.
	 * @return A two-item array of the two parts of the getter.
	 */
	public static String[] processStackFactory(String base) {
		String[] split = new String[2];
		int splitter = base.indexOf("->");
		split[0] = base.substring(0, splitter);
		split[1] = base.substring(splitter + 2);
		return split;
	}

	/**
	 * Process a grid of inputs CraftTweaker-style. Max of 3 width and height.
	 * @param inputs The array of string arrays to process inputs from
	 * @return The inputs converted into a single string array if the grid is valid
	 */
	public static Object[] processGrid(Object[][] inputs) throws CDSyntaxError {
		return processGrid(inputs, 3, 3);
	}

	/**
	 * Process a grid of inputs CraftTweaker-style.
	 * @param inputs The array of string arrays to process inputs from
	 * @param maxWidth The maximum number of columns allowed
	 * @param maxHeight The maximum number of rows allowed
	 * @return The inputs converted into a single string array if the grid is valid
	 */
	public static Object[] processGrid(Object[][] inputs, int maxWidth, int maxHeight) throws CDSyntaxError {
		if (inputs.length > maxHeight) throw new CDSyntaxError("Invalid pattern: too many rows, " + maxHeight + " is maximum");
		if (inputs.length == 0) throw new CDSyntaxError("Invalid pattern: empty pattern is not allowed");
		int width = inputs[0].length;
		List<Object> output = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++) {
			Object[] row = inputs[i];
			if (row.length > maxWidth) throw new CDSyntaxError("Invalid pattern: too many columns, " + maxWidth + " is maximum");
			if (row.length != width) throw new CDSyntaxError("Invalid pattern: each row must be the same width");
			for (int j = 0; j < width; j++) {
				output.add(inputs[i][j]);
			}
		}
		return output.toArray();
	}

	/**
	 * validate and parse a recipe pattern. Max of 3 width and height.
	 * @param pattern up to three strings of up to three characters each for the pattern
	 * @return processed pattern
	 */
	public static String[] processPattern(String... pattern) throws CDSyntaxError {
		return processPattern(3, 3, pattern);
	}

	/**
	 * Validate and parse a recipe pattern.
	 * @param maxWidth The maximum number of columns allowed
	 * @param maxHeight The maximum number of rows allowed
	 * @param pattern Up to <height> strings of up to <width> characters each for the pattern
	 * @return processed pattern
	 */
	public static String[] processPattern(int maxWidth, int maxHeight, String... pattern) throws CDSyntaxError {
		if (pattern.length > 3) {
			throw new CDSyntaxError("Invalid pattern: too many rows, " + maxHeight + " is maximum");
		} else if (pattern.length == 0) {
			throw new CDSyntaxError("Invalid pattern: empty pattern not allowed");
		} else {
			for (int i = 0; i < pattern.length; i++) {
				String row = pattern[i];
				if (row.length() > 3) {
					throw new CDSyntaxError("Invalid pattern: too many columns, " + maxWidth + " is maximum");
				}

				if (i > 0 && pattern[0].length() != row.length()) {
					throw new CDSyntaxError("Invalid pattern: each row must be the same width");
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
	public static Map<String, class_1856> processDictionary(Map<String, Object> dictionary) throws CDSyntaxError {
		Map<String, class_1856> map = new HashMap<>();
		for (Map.Entry<String, Object> entry : dictionary.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new CDSyntaxError("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new CDSyntaxError("Invalid key entry: ' ' is a reserved symbol.");
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
	public static class_2371<class_1856> getIngredients(String[] pattern, Map<String, class_1856> dictionary, int x, int y) throws CDSyntaxError {
		class_2371<class_1856> ingredients = class_2371.method_10213(x * y, class_1856.field_9017);
		Set<String> keys = Sets.newHashSet(dictionary.keySet());
		keys.remove(" ");

		for(int i = 0; i < pattern.length; i++) {
			for(int j = 0; j < pattern[i].length(); j++) {
				String key = pattern[i].substring(j, j + 1);
				class_1856 ingredient = dictionary.get(key);
				if (ingredient == null) {
					throw new CDSyntaxError("Pattern references symbol '" + key + "' but it's not defined in the key");
				}

				keys.remove(key);
				ingredients.set(j + x * i, ingredient);
			}
		}

		if (!keys.isEmpty()) {
			throw new CDSyntaxError("Key defines symbols that aren't used in pattern: " + keys);
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

	/**
	 * Thanks, ProGuard! The `Ingredient.ofStacks()` method is currently only in the client environment,
	 * so I have to write this ugly, terrible hack to make it work!
	 * Serializes the input stacks into a PacketByteBuf,
	 * then tricks the Ingredient class into deserializing them.
	 * However, if NBT Crafting is here, I can just do it with that!
	 * @param stacks The input stacks to support.
	 * @return The ingredient object for the input stacks.
	 */
	public static class_1856 hackStackIngredients(class_1799...stacks) {
		if (FabricLoader.getInstance().isModLoaded("nbtcrafting")) {
			if (stacks.length > 1) {
				JsonArray array = new JsonArray();
				for (class_1799 stack : stacks) {
					array.add(serializeStack(stack));
				}
				return class_1856.method_8102(array);
			} else {
				return class_1856.method_8102(serializeStack(stacks[0]));
			}
//			return IngredientAssembler.constructFromStacks(stacks);
		} else {
			class_2540 buf = new class_2540(Unpooled.buffer());
			buf.method_10804(stacks.length);
			for (class_1799 stack : stacks) {
				buf.method_10793(stack);
			}
			return class_1856.method_8086(buf);
		}
	}

	/*
	 * Helpful if the NBT Crafting `constructFromStacks` doesn't work. It doesn't currently, so here it is.
	 */
	private static JsonObject serializeStack(class_1799 stack) {
		JsonObject ret = new JsonObject();
		ret.addProperty("item", class_2378.field_11142.method_10221(stack.method_7909()).toString());
		ret.addProperty("count", stack.method_7947());
		if (stack.method_7985()) {
			JsonObject data = Dynamic.convert(class_2509.field_11560, JsonOps.INSTANCE, stack.method_7969()).getAsJsonObject();
			ret.add("data", data);
		}
		return ret;
	}
}
