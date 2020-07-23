package io.github.cottonmc.libcd.api.tweaker.loot;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.github.cottonmc.libcd.api.util.Gsons;
import io.github.cottonmc.libcd.api.util.nbt.WrappedCompoundTag;
import net.minecraft.class_117;
import net.minecraft.class_141;
import net.minecraft.class_159;
import net.minecraft.class_3668;
import net.minecraft.class_40;
import net.minecraft.class_44;
import net.minecraft.class_4570;
import net.minecraft.class_47;
import net.minecraft.class_61;
import net.minecraft.loot.function.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to assemble loot functions from JSR-223 code.
 */
public class Functions {
	public static final Functions INSTANCE = new Functions();
	private JsonParser parser = new JsonParser();

	private Functions() {}

	/**
	 * Parse Stringified JSON into a special loot function. Useful for complex or third-party functions.
	 * @param json Stringified JSON of the function to add.
	 * @return The parsed function, ready to add to a table or entry.
	 */
	public class_117 parse(String json) {
		try {
			return Gsons.LOOT_TABLE.fromJson(parser.parse(json), class_117.class);
		} catch (JsonSyntaxException e) {
			LootTweaker.INSTANCE.getLogger().error("Could not parse loot function, returning null: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Set the exact count of items to drop.
	 * @param amount How many items should drop.
	 * @return An assembled function, ready to add to a table or entry.
	 */
	public class_117 countExact(int amount) {
		return class_141.method_621(class_44.method_289(amount)).method_515();
	}

	/**
	 * Set a range of counts of items to drop, with uniform distribution (equal probability for any result).
	 * @param min The minimum number of items to drop.
	 * @param max The maximum number of items to drop.
	 * @return An assembled function, ready to add to a table or entry.
	 */
	public class_117 countRange(int min, int max) {
		return class_141.method_621(class_61.method_377(min, max)).method_515();
	}

	/**
	 * Set a range of counts of items to drop, with binomial distribution (a bell curve of likeliness).
	 * @param n The maximum number of items to drop.
	 * @param p The most common number of items to drop, as a float percentage of the max.
	 * @return An assembled function, ready to add to a table or entry.
	 */
	public class_117 countBinomial(int n, float p) {
		return class_141.method_621(class_40.method_273(n, p)).method_515();
	}

	/**
	 * Give a player head the info it needs to properly display a player skin.
	 * @param from The entity target in this interaction to fill from: `this`, `killer`, `direct_killer`, or `killer_player`.
	 * @param conditions The conditions to meet before applying this function.
	 * @return An assembled function, ready to add to a table or entry.
	 */
	public class_117 fillPlayerHead(String from, class_4570... conditions) {
		List<class_4570> safeConditions = new ArrayList<>();
		for (class_4570 condition : conditions) {
			if (condition == null) {
				LootTweaker.INSTANCE.getLogger().error("Loot table `fillPlayerHead` function cannot take null condition, ignoring");
				continue;
			}
			safeConditions.add(condition);
		}
		class_47.class_50 target = class_47.class_50.method_314(from);
		return new class_3668(safeConditions.toArray(new class_4570[]{}), target);
	}

	//TODO: somehow able to pass conditions?
	/**
	 * Set the NBT for a stack in a loot table
	 * @param tag The wrapped form of the compound tag to set.
	 * @return An assembled function, ready to add to a table or entry.
	 */
	public class_117 setNbt(WrappedCompoundTag tag) {
		return class_159.method_677(tag.getUnderlying()).method_515();
	}
}
