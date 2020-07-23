package io.github.cottonmc.libcd.tweaker;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.util.StackInfo;
import io.github.cottonmc.libcd.util.nbt.WrappedCompoundTag;
import net.minecraft.class_1299;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_2248;
import net.minecraft.class_2371;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2519;
import net.minecraft.class_2522;
import net.minecraft.class_2585;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_3489;
import net.minecraft.class_3494;
import net.minecraft.class_3611;

/**
 * Various utilities for writing tweakers, due to the obfuscation of minecraft code.
 */
public class TweakerUtils {
	public static final TweakerUtils INSTANCE = new TweakerUtils();
	/**
	 * Get a registered item inside a script.
	 * @param id The id to search for.
	 * @return The registered item, or Items.AIR if it doesn't exist.
	 */
	public class_1792 getItem(String id) {
		return class_2378.field_11142.method_10223(new class_2960(id));
	}

	/**
	 * Get the item from an item stack.
	 * @param stack The stack to check.
	 * @return The item of the stack.
	 */
	public class_1792 getStackItem(class_1799 stack) {
		return stack.method_7909();
	}

	/**
	 * Get a registered block inside a script.
	 * @param id The id to search for.
	 * @return The registered item, or Blocks.AIR if it doesn't exist.
	 */
	public class_2248 getBlock(String id) {
		return class_2378.field_11146.method_10223(new class_2960(id));
	}

	/**
	 * Get a registered fluid inside a script.
	 * @param id The id to search for.
	 * @return The registered fluid, or Fluids.EMPTY if it doesn't exist.
	 */
	public class_3611 getFluid(String id) {
		return class_2378.field_11154.method_10223(new class_2960(id));
	}

	/**
	 * Get a registered entity type inside a script.
	 * @param id The id to search for.
	 * @return The registered entity, or EntityType.PIG if it doesn't exist.
	 */
	public class_1299 getEntity(String id) {
		return class_2378.field_11145.method_10223(new class_2960(id));
	}

	/**
	 * Get a registered sound inside a script.
	 * @param id The id to search for.
	 * @return The registered sound, or SoundEvents.ENTITY_ITEM_PICKUP if it doesn't exist.
	 */
	public class_3414 getSound(String id) {
		return class_2378.field_11156.method_10223(new class_2960(id));
	}

	/**
	 * Check if a DefaultedList (like the ones inventories use) is empty.
	 * Necessary because DefaultedList stays within Collection<E> spec for once.
	 * @param items The DefaultedList to check.
	 * @return Whether all the item stacks in the list are empty or not.
	 */
	public boolean isItemListEmpty(class_2371<class_1799> items) {
		for (class_1799 stack : items) {
			if (!stack.method_7960()) return false;
		}
		return true;
	}

	/**
	 * Create an item stack from an item id.
	 * @param id The id of the item to get, along with any NBT.
	 * @return An item stack of the specified item, with an amount of 1.
	 */
	public class_1799 createItemStack(String id) {
		return createItemStack(id, 1);
	}

	/**
	 * Create an item stack from an item id.
	 * @param id The id of the item to get, along with any NBT.
	 * @param amount The amount of the item in the stack.
	 * @return An item stack of the specified item and amount.
	 */
	public class_1799 createItemStack(String id, int amount) {
		int index = id.indexOf('{');
		if (index == -1) {
			return new class_1799(getItem(id), amount);
		} else {
			class_1792 item = getItem(id.substring(0, index));
			class_1799 stack = new class_1799(item, amount);
			return addNbtToStack(stack, id.substring(index));
		}
	}

	/**
	 * Create an item stack from an item.
	 * @param item The item to have a stack of.
	 * @return An item stack of the specified item, with an amount of 1.
	 */
	public class_1799 createItemStack(class_1792 item) {
		return createItemStack(item, 1);
	}

	/**
	 * Create an item stack from an item.
	 * @param item The item to have a stack of.
	 * @param amount The amount of the item in the stack.
	 * @return An item stack of the specified item and amount.
	 */
	public class_1799 createItemStack(class_1792 item, int amount) {
		return new class_1799(item, amount);
	}

	/**
	 * Add NBT to an item stack.
	 * @param stack The stack to add NBT to.
	 * @param nbt The string version of NBT to add.
	 * @return The stack with added NBT.
	 */
	public class_1799 addNbtToStack(class_1799 stack, String nbt) {
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
	 * Add NBT to an item stack.
	 * @param stack The stack to add NBT to.
	 * @param tag The wrapped compound tag to add.
	 * @return The stack with added NBT.
	 */
	public class_1799 addNbtToStack(class_1799 stack, WrappedCompoundTag tag) {
		stack.method_7980(tag.getUnderlying());
		return stack;
	}

	/**
	 * @param stack The stack to get info for.
	 * @return A wrapper class with read-only info about the tag.
	 */
	public StackInfo getStackInfo(class_1799 stack) {
		return new StackInfo(stack);
	}

	/**
	 * @param stack The stack to get the tag for.
	 * @return A wrapper around the mutable tag for the stack.
	 */
	public WrappedCompoundTag getStackTag(class_1799 stack) {
		return new WrappedCompoundTag(stack.method_7948());
	}

	/**
	 * Add an enchantment to an ItemStack. Will ignore whether the enchantment fits on the stack.
	 * @param stack The stack to enchant.
	 * @param enchantment The ID of the enchantment to add.
	 * @param level The level of the enchantment to add.
	 * @return The stack with the new enchantment.
	 */
	public class_1799 enchant(class_1799 stack, String enchantment, int level) {
		class_1887 ench = class_2378.field_11160.method_10223(new class_2960(enchantment));
		stack.method_7978(ench, level);
		return stack;
	}

	/**
	 * Add lore messages to an ItemStack.
	 * @param stack The stack to add lroe to.
	 * @param lore The lines to add to lore. Use § to change the color of the messages.
	 * @return The stack with the new lore.
	 */
	public class_1799 addLore(class_1799 stack, String[] lore) {
		class_2487 display = stack.method_7911("display");
		class_2499 list = display.method_10554("Lore", 8);
		for (int i = 0; i < lore.length; i++) {
			String line = lore[i];
			list.method_10533(i, new class_2519("{\"text\":\"" + line + "\"}"));
		}
		display.method_10566("Lore", list);
		stack.method_7959("display", display);
		return stack;
	}

	/**
	 * Set the damage on an ItemStack. Counts up from 0 to the item's max damage.
	 * @param stack The stack to set damage on.
	 * @param amount How much damage to apply, or -1 to make unbreakable.
	 * @return The stack with the new damage.
	 */
	public class_1799 setDamage(class_1799 stack, int amount) {
		if (amount == -1) stack.method_7948().method_10556("Unbreakable", true);
		else stack.method_7974(amount);
		return stack;
	}

	/**
	 * Set the custom name on an ItemStack.
	 * @param stack The stack to set the name on.
	 * @param name The name to set to. Use § to change the color of the name.
	 * @return The stack with the new name.
	 */
	public class_1799 setName(class_1799 stack, String name) {
		stack.method_7977(new class_2585(name));
		return stack;
	}

	/**
	 * Get a specal stack like a potion from its formatted getter string.
	 * @param getter The formatted getter string ([getter:id]->[entry:id]) to use.
	 * @return the gotten stack, or an empty stack if the getter or id doesn't exist
	 */
	public class_1799 getSpecialStack(String getter) {
		String[] split = RecipeParser.processGetter(getter);
		return getSpecialStack(split[0], split[1]);
	}

	/**
	 * Get a special stack like a potion from its getter and ID.
	 * @param getter The id of the TweakerStackGetter to use.
	 * @param entry The id of the entry to get from the TweakerStackGetter.
	 * @return The gotten stack, or an empty stack if the getter or id doesn't exist.
	 */
	public class_1799 getSpecialStack(String getter, String entry) {
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
	public String[] getItemsInTag(String tagId) {
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
