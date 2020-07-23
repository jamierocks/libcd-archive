package io.github.cottonmc.libcd.api.tweaker.recipe;

import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.util.*;
import io.github.cottonmc.libcd.api.util.crafting.CraftingUtils;
import net.minecraft.class_1657;
import net.minecraft.class_1715;
import net.minecraft.class_1799;
import net.minecraft.class_1856;
import net.minecraft.class_1867;
import net.minecraft.class_1937;
import net.minecraft.class_2371;
import net.minecraft.class_2960;

public class CustomShapelessRecipe extends class_1867 {
	private ScriptBridge bridge;
	private CDLogger logger;

	public CustomShapelessRecipe(ScriptBridge bridge, class_2960 id, String group, class_2371<class_1856> ingredients, class_1799 output) {
		super(id, group, output, ingredients);
		this.bridge = bridge;
		this.logger = new CDLogger(bridge.getId().toString());
	}

	@Override
	public boolean method_17730(class_1715 inv, class_1937 world) {
		boolean matches = super.method_17730(inv, world);
		if (!matches) return false;
		try {
			class_1657 player = CraftingUtils.findPlayer(inv);
			Object result = bridge.invokeFunction("matches", CraftingUtils.getInvStacks(inv), inv.method_17398(), inv.method_17397(), player != null? new WrappedPlayer(player) : DummyPlayer.INSTANCE, new WorldInfo(world));
			if (result instanceof Boolean) return (Boolean) result;
			else {
				logger.error("Could not check match for custom shapeless recipe %s, returning standard match: function 'matches' must return boolean but returned %s", method_8114(), result.getClass().getName());
				return true;
			}
		} catch (Exception e) {
			logger.error("Could not check match for custom shapeless recipe %s, returning standard match: %s", method_8114(), e.getMessage());
		}
		return super.method_17730(inv, world);
	}

	@Override
	public class_1799 method_17729(class_1715 inv) {
		class_1799 stack = super.method_17729(inv);
		try {
			MutableStack mutableStack = new MutableStack(stack);
			class_1657 player = CraftingUtils.findPlayer(inv);
			Object result = bridge.invokeFunction("preview", CraftingUtils.getInvStacks(inv), inv.method_17398(), inv.method_17397(), player != null? new WrappedPlayer(player) : DummyPlayer.INSTANCE, mutableStack );
			return result == null? mutableStack.get() : RecipeParser.processItemStack(result);
		} catch (Exception e) {
			logger.error("Could not get preview output for custom shapeless recipe %s, returning standard output: %s", method_8114(), e.getMessage());
			return super.method_17729(inv);
		}
	}

	@Override
	public class_2371<class_1799> getRemainingStacks(class_1715 inv) {
		class_2371<class_1799> remainingStacks = super.method_8111(inv);
		try {
			class_1657 player = CraftingUtils.findPlayer(inv);
			bridge.invokeFunction("craft", CraftingUtils.getInvStacks(inv), player != null? new WrappedPlayer(player) : DummyPlayer.INSTANCE, new StackInfo(method_17729(inv)));
		} catch (Exception e) {
			logger.error("Could not fully craft custom shapeless recipe %s, ignoring: %s", method_8114(), e.getMessage());
		}
		return remainingStacks;
	}
}
