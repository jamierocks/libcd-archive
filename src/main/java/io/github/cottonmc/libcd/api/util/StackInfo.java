package io.github.cottonmc.libcd.api.util;

import io.github.cottonmc.libcd.api.util.nbt.NbtUtils;
import io.github.cottonmc.libcd.api.util.nbt.WrappedCompoundTag;
import java.util.Map;
import java.util.Optional;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2960;

/**
 * A class with read-only info about a stack, accessible outside of obf. The stack and its NBT cannot be modified from a StackInfo.
 */
public class StackInfo {
	private class_1799 stack;
	public StackInfo(class_1799 stack) {
		this.stack = stack.method_7972();
	}

	/**
	 * @return Whether the stack is empty.
	 */
	boolean isEmpty() {
		return stack.method_7960();
	}

	/**
	 * @return The ID of the stack's item.
	 */
	String getItem() {
		return class_2378.field_11142.method_10221(stack.method_7909()).toString();
	}

	/**
	 * @return The count of items in the stack.
	 */
	int getCount() {
		return stack.method_7947();
	}

	/**
	 * @return The stack's name.
	 */
	String getName() {
		return stack.method_7964().method_10851();
	}

	/**
	 * @return How much damage the item has taken.
	 */
	int getDamage() {
		return stack.method_7919();
	}

	/**
	 * @param enchantId The enchantment to check for.
	 * @return The level of that enchantment, or 0 if it's not there.
	 */
	int getEnchantmentLevel(String enchantId) {
		if (!stack.method_7942()) return 0;
		Optional<class_1887> opt = class_2378.field_11160.method_17966(new class_2960(enchantId));
		if (!opt.isPresent()) return 0;
		Map<class_1887, Integer> enchants = class_1890.method_8222(stack);
		return enchants.getOrDefault(opt.get(), 0);
	}

	/**
	 * @param key The key to check the value of.
	 * @return The object at that key.
	 */
	Object getTagValue(String key) {
		class_2487 tag = stack.method_7948();
		return NbtUtils.getObjectFor(tag.method_10580(key));
	}

	/**
	 * @return a non-modifiable view of the object's NBT, wrapped for usability.
	 */
	WrappedCompoundTag getTag() {
		return new WrappedCompoundTag(stack.method_7948());
	}

}
