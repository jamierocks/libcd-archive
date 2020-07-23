package io.github.cottonmc.libcd.api.util;

import io.github.cottonmc.libcd.api.util.nbt.NbtUtils;
import io.github.cottonmc.libcd.api.util.nbt.WrappedCompoundTag;
import net.minecraft.class_1799;
import net.minecraft.class_2378;
import net.minecraft.class_2561;
import net.minecraft.class_2585;
import net.minecraft.class_2960;

/**
 * A wrapped version of an item stack that can be modified from scripts.
 * Has all the methods of {@link StackInfo}.
 */
public class MutableStack extends StackInfo {

	public MutableStack(class_1799 stack) {
		super(stack);
		this.stack = stack;
	}

	/**
	 * Set the count of the item stack.
	 * @param count The count of items in the stack. Will be limited to the max stack size for this item.
	 */
	public MutableStack setCount(int count) {
		stack.method_7939(count);
		return this;
	}

	/**
	 * Set the custom name of the item stack.
	 * @param name The name for this stack.
	 */
	public MutableStack setName(String name) {
		stack.method_7977(new class_2585(name));
		return this;
	}

	public MutableStack setFormattedName(String name) {
		stack.method_7977(class_2561.class_2562.method_10877(name));
		return this;
	}

	/**
	 * Set the damage of the item stack.
	 * @param damage The amount of damage the item has taken.
	 */
	public MutableStack setDamage(int damage) {
		stack.method_7974(damage);
		return this;
	}

	/**
	 * Set the value of a tag in the stack's main NBT tag.
	 * @param key The name of the tag to set.
	 * @param value The value to set it to.
	 */
	public MutableStack setTagValue(String key, Object value) {
		stack.method_7948().method_10566(key, NbtUtils.getTagFor(value));
		return this;
	}

	/**
	 * Set the entire stack NBT tag.
	 * @param tag The tag to set it to.
	 */
	public MutableStack setTag(WrappedCompoundTag tag) {
		stack.method_7980(tag.getUnderlying());
		return this;
	}

	public MutableStack enchant(String enchantmentName, int level) {
		stack.method_7978(class_2378.field_11160.method_10223(new class_2960(enchantmentName)), level);
		return this;
	}

	public class_1799 get() {
		return stack;
	}
}
