package io.github.cottonmc.libcd.api.tweaker.recipe;

import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.util.DummyPlayer;
import io.github.cottonmc.libcd.api.util.StackInfo;
import io.github.cottonmc.libcd.api.util.WorldInfo;
import io.github.cottonmc.libcd.api.util.WrappedPlayer;
import io.github.cottonmc.libcd.api.util.crafting.CraftingUtils;
import net.minecraft.class_1657;
import net.minecraft.class_1715;
import net.minecraft.class_1799;
import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_1937;
import net.minecraft.class_2371;
import net.minecraft.class_2960;

public class CustomSpecialCraftingRecipe extends class_1852 {
	private ScriptBridge bridge;
	private CDLogger logger;

	public CustomSpecialCraftingRecipe(ScriptBridge bridge, class_2960 id) {
		super(id);
		this.bridge = bridge;
		this.logger = new CDLogger(bridge.getId().toString());
	}

	public CustomSpecialCraftingRecipe(class_2960 id) {
		super(id);
	}

	@Override
	public boolean matches(class_1715 inv, class_1937 world) {
		try {
			class_1657 player = CraftingUtils.findPlayer(inv);
			Object result = bridge.invokeFunction("matches", CraftingUtils.getInvStacks(inv), inv.method_17398(), inv.method_17397(), player != null? new WrappedPlayer(player) : DummyPlayer.INSTANCE, new WorldInfo(world));
			if (result instanceof Boolean) return (Boolean) result;
			else {
				logger.error("Could not check match for custom special crafting recipe %s, returning false: function 'matches' must returna  boolean, but returned %s instead", method_8114(), result.getClass().getName());
				return false;
			}
		} catch (Exception e) {
			logger.error("Could not check match for custom special crafting recipe %s, returning false: %s", method_8114(), e.getMessage());
		}
		return false;
	}

	@Override
	public class_1799 craft(class_1715 inv) {
		try {
			class_1657 player = CraftingUtils.findPlayer(inv);
			Object result = bridge.invokeFunction("preview", CraftingUtils.getInvStacks(inv), inv.method_17398(), inv.method_17397(), player != null? new WrappedPlayer(player) : DummyPlayer.INSTANCE);
			if (result == null) {
				logger.error("Could not get preview output for custom special crafting recipe %s, returning empty stack: function 'preview' must not return null", method_8114());
				return class_1799.field_8037;
			} else {
				return RecipeParser.processItemStack(result);
			}
		} catch (Exception e) {
			logger.error("Could not get preview output for custom special crafting recipe %s, returning empty: %s", method_8114(), e.getMessage());
			return class_1799.field_8037;
		}
	}

	@Override
	public boolean method_8113(int width, int height) {
		return true; //this doesn't matter, since it's a special crafting recipe
	}

	//TODO: make sure this is only called on server?
	@Override
	public class_2371<class_1799> getRemainingStacks(class_1715 inv) {
		class_2371<class_1799> remainingStacks = super.method_8111(inv);
		try {
			class_1657 player = CraftingUtils.findPlayer(inv);
			bridge.invokeFunction("craft", CraftingUtils.getInvStacks(inv), player != null? new WrappedPlayer(player) : DummyPlayer.INSTANCE, new StackInfo(craft(inv)));
		} catch (Exception e) {
			logger.error("Could not fully craft custom special crafting recipe %s, ignoring: %s", method_8114(), e.getMessage());
		}
		return remainingStacks;
	}

	//TODO: custom serializer to let users specify a script bridge?
	@Override
	public class_1865<?> method_8119() {
		return LibCD.CUSTOM_SPECIAL_SERIALIZER;
	}
}
