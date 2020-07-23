package io.github.cottonmc.libcd.api.util;

import net.minecraft.class_3542;

/**
 * The matching mode for nbt checking in an ingredient.
 */
public enum NbtMatchType implements class_3542 {
	/**
	 * Nbt is not checked; any nbt will return true.
	 */
	NONE("none"),
	/**
	 * Nbt is fuzzily checked; the input stack may have extra tags,
	 * but it must have all of the tags that the ingredient stack has,
	 * with the same values.
	 */
	FUZZY("fuzzy"),
	/**
	 * Nbt is strictly checked; only identical nbt will return true.
	 */
	EXACT("exact");

	String name;

	NbtMatchType(String name) {
		this.name = name;
	}

	@Override
	public String method_15434() {
		return name;
	}

	public static NbtMatchType forName(String name) {
		for (NbtMatchType value : NbtMatchType.values()) {
			if (name.equals(value.method_15434())) {
				return value;
			}
		}
		return NbtMatchType.NONE;
	}
}
