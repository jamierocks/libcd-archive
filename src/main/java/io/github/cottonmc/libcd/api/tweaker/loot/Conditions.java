package io.github.cottonmc.libcd.api.tweaker.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.libcd.api.util.Gsons;
import io.github.cottonmc.libcd.mixin.ReferenceLootConditionAccessor;
import net.minecraft.class_182;
import net.minecraft.class_201;
import net.minecraft.class_2073;
import net.minecraft.class_219;
import net.minecraft.class_221;
import net.minecraft.class_223;
import net.minecraft.class_225;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2522;
import net.minecraft.class_2960;
import net.minecraft.class_3489;
import net.minecraft.class_5341;
import net.minecraft.loot.condition.*;
import javax.annotation.Nullable;

public class Conditions {
	public static final Conditions INSTANCE = new Conditions();

	private Conditions() {}

	/**
	 * Parse Stringified JSON into a special loot condition. Useful for complex or third-party conditions.
	 * @param json Stringified JSON of the condition to add.
	 * @return The parsed condition, ready to add to a pool or entry.
	 */
	public class_5341 parse(String json) {
		try {
			return Gsons.LOOT_TABLE.fromJson(json, class_5341.class);
		} catch (JsonSyntaxException e) {
			LootTweaker.INSTANCE.getLogger().error("Could not parse loot condition, returning null: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Assemble a group condition which will return true if any child condition is met.
	 * @param conditions The conditions to test.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 or(class_5341... conditions) {
		JsonObject json = new JsonObject();
		json.addProperty("condition", "minecraft:alternative");
		JsonArray children = new JsonArray();
		for (class_5341 condition : conditions) {
			if (condition == null) {
				LootTweaker.INSTANCE.getLogger().error("Loot table `or` condition cannot take null condition, skipping");
				continue;
			}
			String cond = Gsons.LOOT_TABLE.toJson(condition);
			children.add(Gsons.PARSER.parse(cond));
		}
		json.add("terms", children);
		return Gsons.LOOT_TABLE.fromJson(json, class_5341.class);
	}

	/**
	 * Create a loot condition that's an inversion of another condition.
	 * @param condition The condition to invert.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 not(class_5341 condition) {
		if (condition == null) {
			LootTweaker.INSTANCE.getLogger().error("Loot table `not` condition cannot take null condition, returning null");
			return null;
		}
		String json = Gsons.LOOT_TABLE.toJson(condition);
		JsonObject cond = new JsonObject();
		cond.addProperty("condition", "minecraft:inverted");
		cond.add("term", Gsons.PARSER.parse(json));
		return Gsons.LOOT_TABLE.fromJson(cond, class_5341.class);
	}

	/**
	 * Check whether an entity was killed by a player.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 killedByPlayer() {
		return class_221.method_939().build();
	}

	/**
	 * A random chance to drop.
	 * @param chance The chance, as a percentage.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 chance(float chance) {
		return class_219.method_932(chance).build();
	}

	/**
	 * A random chance to drop, affected by looting.
	 * @param chance The base chance, as a percentage.
	 * @param multiplier The multiplier for each level of looting on a weapon.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 chanceWithLooting(float chance, float multiplier) {
		return class_225.method_953(chance, multiplier).build();
	}

	/**
	 * Check whether a block survives a creeper explosion.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 survivesExplosion() {
		return class_201.method_871().build();
	}

	/**
	 * Check whether a given tool will cause a drop.
	 * @param item An item or tag ID to test. Tag ID must be prepended with `#`.
	 * @param nbt Any NBT required for the tool to have.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	//TODO: enchantment
	public class_5341 matchTool(String item, String nbt) {
		class_2073.class_2074 builder = class_2073.class_2074.method_8973();
		if (item.indexOf('#') == 0) {
			class_2960 id = new class_2960(item.substring(1));
			builder.method_8975(class_3489.method_15106().method_15193(id));
		} else {
			class_2960 id = new class_2960(item);
			builder.method_8977(class_2378.field_11142.method_10223(id));
		}
		if (!nbt.equals("")) {
			try {
				class_2487 tag = class_2522.method_10718(nbt);
				builder.method_20399(tag);
			} catch (CommandSyntaxException e) {

			}

		}
		return class_223.method_945(builder).build();
	}

	/**
	 * Use an enchantment on a tool to determine whether something should drop.
	 * @param enchantment The enchantment to test with.
	 * @param chances The float percentage chance that this will drop for each level of the enchantment.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 enchantBonus(String enchantment, float[] chances) {
		return class_182.method_800(class_2378.field_11160.method_10223(new class_2960(enchantment)), chances).build();
	}

	/**
	 * Use the weather to determine whether something should drop.
	 * @param raining The state of rain required to drop (use null to ignore).
	 * @param thundering The state of thunder required to drop (use null to ignore).
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 weather(@Nullable Boolean raining, @Nullable Boolean thundering) {
		JsonObject json = new JsonObject();
		json.addProperty("type", "minecraft:weather_check");
		if (raining != null) json.addProperty("raining", raining);
		if (thundering != null) json.addProperty("thundering", thundering);
		return Gsons.LOOT_TABLE.fromJson(json, class_5341.class);
	}

	/**
	 * Use a predicate JSON as a condition.
	 * @param id The ID of the predicate JSON to use.
	 * @return An assembled condition, ready to add to a pool or entry.
	 */
	public class_5341 predicate(String id) {
		return ReferenceLootConditionAccessor.callConstructor(new class_2960(id));
	}

}
