package io.github.cottonmc.libcd.api.util.crafting;

import net.minecraft.class_1282;

public class CraftingDamageSource extends class_1282 {
	public static final CraftingDamageSource INSTANCE = new CraftingDamageSource();

	private CraftingDamageSource() {
		super("libcd.crafting");
		this.method_5508();
		this.method_5509();
	}
}
