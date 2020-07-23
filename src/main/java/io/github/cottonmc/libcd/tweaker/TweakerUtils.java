package io.github.cottonmc.libcd.tweaker;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.libcd.LibCD;
import net.minecraft.class_1299;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_2248;
import net.minecraft.class_2371;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2522;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_3489;
import net.minecraft.class_3494;
import net.minecraft.class_3611;

/**
 * Various utilities for writing tweakers, due to the obfuscation of minecraft code.
 */
public class TweakerUtils {
	/**
	 * Get a registered item inside a script.
	 * @param id The id to search for.
	 * @return The registered item, or Items.AIR if it doesn't exist.
	 */
	public static class_1792 getItem(String id) {
		return class_2378.field_11142.method_10223(new class_2960(id));
	}

	/**
	 * Get the item from an item stack.
	 * @param stack The stack to check.
	 * @return The item of the stack.
	 */
	public static class_1792 getStackItem(class_1799 stack) {
		return stack.method_7909();
	}

	/**
	 * Get a registered block inside a script.
	 * @param id The id to search for.
	 * @return The registered item, or Blocks.AIR if it doesn't exist.
	 */
	public static class_2248 getBlock(String id) {
		return class_2378.field_11146.method_10223(new class_2960(id));
	}

	/**
	 * Get a registered fluid inside a script.
	 * @param id The id to search for.
	 * @return The registered fluid, or Fluids.EMPTY if it doesn't exist.
	 */
	public static class_3611 getFluid(String id) {
		return class_2378.field_11154.method_10223(new class_2960(id));
	}

	/**
	 * Get a registered entity type inside a script.
	 * @param id The id to search for.
	 * @return The registered entity, or EntityType.PIG if it doesn't exist.
	 */
	public static class_1299 getEntity(String id) {
		return class_2378.field_11145.method_10223(new class_2960(id));
	}

	/**
	 * Get a registered sound inside a script.
	 * @param id The id to search for.
	 * @return The registered sound, or SoundEvents.ENTITY_ITEM_PICKUP if it doesn't exist.
	 */
	public static class_3414 getSound(String id) {
		return class_2378.field_11156.method_10223(new class_2960(id));
	}

	/**
	 * Check if a DefaultedList (like the ones inventories use) is empty.
	 * Necessary because DefaultedList stays within Collection<E> spec for once.
	 * @param items The DefaultedList to check.
	 * @return Whether all the item stacks in the list are empty or not.
	 */
	public static boolean isItemListEmpty(class_2371<class_1799> items) {
		for (class_1799 stack : items) {
			if (!stack.method_7960()) return false;
		}
		return true;
	}

	/**
	 * Create an item stack from an item id.
	 * @param id The id of the item to get.
	 * @param amount The amount of the item in the stack.
	 * @return An item stack of the specified item and amount.
	 */
	public static class_1799 createItemStack(String id, int amount) {
		return new class_1799(getItem(id), amount);
	}

	/**
	 * Create an item stack from an item.
	 * @param item The item to have a stack of.
	 * @param amount The amount of the item in the stack.
	 * @return An item stack of the specified item and amount.
	 */
	public static class_1799 createItemStack(class_1792 item, int amount) {
		return new class_1799(item, amount);
	}

	/**
	 * Add NBT to an item stack.
	 * @param stack The stack to add NBT to.
	 * @param nbt The string version of NBT to add.
	 * @return The stack with added NBT.
	 */
	public static class_1799 addNbtToStack(class_1799 stack, String nbt) {
		class_2522 reader = new class_2522(new StringReader(nbt));
		try {
			class_2487 tag = reader.method_10727();
			stack.method_7980(tag);
		} catch (CommandSyntaxException e) {
			LibCD.logger.error("Error adding NBT to stack: " + e.getMessage());
		}
		return stack;
	}

	/**
	 * Get a potion of the specified type.
	 * Deprecated; use `getSpecialStack("minecraft:potion", id); instead
	 * @param id The id of the potion to get.
	 * @see <a href="https://minecraft.gamepedia.com/Potion#Data_values">Potion data values</a>
	 * @return an ItemStack of the desired potion, or an empty stack if the potion doesn't exist.
	 */
	@Deprecated
	public static class_1799 getPotion(String id) {
		class_1842 potion = class_1842.method_8048(id);
		if (potion == class_1847.field_8984) return class_1799.field_8037;
		return class_1844.method_8061(new class_1799(class_1802.field_8574), potion);
	}

	/**
	 * Get a specal stack like a potion from its formatted getter string.
	 * @param getter The formatted getter string ([getter:id]->[entry:id]) to use.
	 * @return the gotten stack, or an empty stack if the getter or id doesn't exist
	 */
	public static class_1799 getSpecialStack(String getter) {
		String[] split = RecipeParser.processGetter(getter);
		return getSpecialStack(split[0], split[1]);
	}

	/**
	 * Get a special stack like a potion from its getter and ID.
	 * @param getter The id of the TweakerStackGetter to use.
	 * @param entry The id of the entry to get from the TweakerStackGetter.
	 * @return The gotten stack, or an empty stack if the getter or id doesn't exist.
	 */
	public static class_1799 getSpecialStack(String getter, String entry) {
		class_2960 getterId = new class_2960(getter);
		class_2960 itemId = new class_2960(entry);
		if (!TweakerStackGetter.GETTERS.containsKey(getterId)) return class_1799.field_8037;
		TweakerStackGetter get = TweakerStackGetter.GETTERS.get(getterId);
		return get.getSpecialStack(itemId);
	}

	/**
	 * Get an array of string ids for items in a given tag.
	 * @param tagId The id of the tag to get items for.
	 * @return An array of items in the tag.
	 */
	public static String[] getItemsInTag(String tagId) {
		class_3494<class_1792> tag = class_3489.method_15106().method_15193(new class_2960(tagId));
		if (tag == null) return new String[0];
		Object[] items = tag.method_15138().toArray();
		String[] res = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			res[i] = class_2378.field_11142.method_10221((class_1792)items[i]).toString();
		}
		return res;
	}
}
