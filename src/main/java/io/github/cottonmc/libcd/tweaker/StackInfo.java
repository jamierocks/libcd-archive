package io.github.cottonmc.libcd.tweaker;

import java.util.Map;
import java.util.Optional;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2960;

public class StackInfo {
	private class_1799 stack;
	public StackInfo(class_1799 stack) {
		this.stack = stack.method_7972();
	}

	boolean isEmpty() {
		return stack.method_7960();
	}

	String getItem() {
		return class_2378.field_11142.method_10221(stack.method_7909()).toString();
	}

	int getCount() {
		return stack.method_7947();
	}

	String getName() {
		return stack.method_7964().method_10851();
	}

	int getDamage() {
		return stack.method_7919();
	}

	int getEnchantmentLevel(String enchantId) {
		if (!stack.method_7942()) return 0;
		Optional<class_1887> opt = class_2378.field_11160.method_17966(new class_2960(enchantId));
		if (!opt.isPresent()) return 0;
		Map<class_1887, Integer> enchants = class_1890.method_8222(stack);
		return enchants.getOrDefault(opt.get(), 0);
	}

	String getTagValue(String key) {
		class_2487 tag = stack.method_7948();
		if (!tag.method_10545(key)) return "";
		else return tag.method_10580(key).method_10714();
	}

}
