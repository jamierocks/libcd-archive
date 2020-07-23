package io.github.cottonmc.libcd;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.cottonmc.libcd.api.advancement.AdvancementRewardsManager;
import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.LibCDInitializer;
import io.github.cottonmc.libcd.api.condition.ConditionManager;
import io.github.cottonmc.libcd.api.condition.ConditionalData;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import io.github.cottonmc.libcd.api.tweaker.loot.Conditions;
import io.github.cottonmc.libcd.api.tweaker.loot.Entries;
import io.github.cottonmc.libcd.api.tweaker.loot.Functions;
import io.github.cottonmc.libcd.api.tweaker.loot.LootTweaker;
import io.github.cottonmc.libcd.api.tweaker.util.Nbt;
import io.github.cottonmc.libcd.api.tweaker.util.TweakerUtils;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeTweaker;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_2246;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3481;
import net.minecraft.class_3489;
import java.util.List;

public class CDContent implements LibCDInitializer {
	@Override
	public void initTweakers(TweakerManager manager) {
		manager.addTweaker("libcd.recipe.RecipeTweaker", RecipeTweaker.INSTANCE);
		manager.addTweaker("libcd.loot.LootTweaker", LootTweaker.INSTANCE);
		manager.addAssistant("libcd.util.TweakerUtils", TweakerUtils.INSTANCE);
		manager.addAssistant("libcd.util.Nbt", Nbt.INSTANCE);
		manager.addAssistant("libcd.loot.Conditions", Conditions.INSTANCE);
		manager.addAssistant("libcd.loot.Functions", Functions.INSTANCE);
		manager.addAssistant("libcd.loot.Entries", Entries.INSTANCE);
		manager.addStackFactory(new class_2960("minecraft", "potion"), (id) -> {
			class_1842 potion = class_1842.method_8048(id.toString());
			if (potion == class_1847.field_8984) return class_1799.field_8037;
			return class_1844.method_8061(new class_1799(class_1802.field_8574), potion);
		});
	}

	@Override
	public void initConditions(ConditionManager manager) {
		manager.registerCondition(new class_2960(CDCommons.MODID, "mod_loaded"), value -> {
			if (value instanceof String) return FabricLoader.getInstance().isModLoaded((String) value);
			if (value instanceof List) {
				for (JsonElement el : (List<JsonElement>)value) {
					if (!(el instanceof JsonPrimitive)) return false;
					Object obj = ((JsonPrimitive)el).getValue();
					if (obj instanceof String) {
						if (!FabricLoader.getInstance().isModLoaded((String)obj)) return false;
					}  else return false;
				}
				return true;
			}
			throw new CDSyntaxError("mod_loaded must accept either a String or an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "item_exists"), value -> {
			if (value instanceof String) return class_2378.field_11142.method_10223(new class_2960((String)value)) != class_1802.field_8162;
			if (value instanceof List) {
				for (JsonElement el : (List<JsonElement>)value) {
					if (!(el instanceof JsonPrimitive)) throw new CDSyntaxError("item_exists array must only contain Strings!");
					Object obj = ((JsonPrimitive)el).getValue();
					if (obj instanceof String) {
						if (class_2378.field_11142.method_10223(new class_2960((String)obj)) == class_1802.field_8162) return false;
					}  else throw new CDSyntaxError("item_exists array must only contain Strings!");
				}
				return true;
			}
			throw new CDSyntaxError("item_exists must accept either a String or an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "item_tag_exists"), value -> {
			if (value instanceof String) return class_3489.method_15106().method_15189().contains(new class_2960((String)value));
			if (value instanceof List) {
				for (JsonElement el : (List<JsonElement>)value) {
					if (!(el instanceof JsonPrimitive)) throw new CDSyntaxError("item_tag_exists array must only contain Strings!");
					Object obj = ((JsonPrimitive)el).getValue();
					if (obj instanceof String) {
						class_2960 id = new class_2960((String) obj);
						if (!class_3489.method_15106().method_15189().contains(id)) return false;
						if (class_3489.method_15106().method_15193(id).method_15138().isEmpty()) return false;
					}  else throw new CDSyntaxError("item_tag_exists array must only contain Strings!");
				}
				return true;
			}
			throw new CDSyntaxError("item_tag_exists must accept either a String or an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "block_exists"), value -> {
			if (value instanceof String) return class_2378.field_11146.method_10223(new class_2960((String)value)) != class_2246.field_10124;
			if (value instanceof List) {
				for (JsonElement el : (List<JsonElement>)value) {
					if (!(el instanceof JsonPrimitive)) throw new CDSyntaxError("block_exists array must only contain Strings!");
					Object obj = ((JsonPrimitive)el).getValue();
					if (obj instanceof String) {
						if (class_2378.field_11146.method_10223(new class_2960((String)obj)) == class_2246.field_10124) return false;
					}  else throw new CDSyntaxError("block_exists array must only contain Strings!");
				}
				return true;
			}
			throw new CDSyntaxError("block_exists must accept either a String or an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "block_tag_exists"), value -> {
			if (value instanceof String) return class_3481.method_15073().method_15189().contains(new class_2960((String)value));
			if (value instanceof List) {
				for (JsonElement el : (List<JsonElement>)value) {
					if (!(el instanceof JsonPrimitive)) throw new CDSyntaxError("block_tag_exists array must only contain Strings!");
					Object obj = ((JsonPrimitive)el).getValue();
					if (obj instanceof String) {
						class_2960 id = new class_2960((String) obj);
						if (!class_3481.method_15073().method_15189().contains(id)) return false;
						if (class_3489.method_15106().method_15193(id).method_15138().isEmpty()) return false;
					}  else throw new CDSyntaxError("block_tag_exists array must only contain Strings!");
				}
				return true;
			}
			throw new CDSyntaxError("block_tag_exists must accept either a String or an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "not"), value -> {
			if (value instanceof JsonObject) {
				JsonObject json = (JsonObject)value;
				for (String key : json.keySet()) {
					class_2960 id = new class_2960(key);
					Object result = ConditionalData.parseElement(json.get(key));
					if (ConditionalData.hasCondition(id)) {
						return !ConditionalData.testCondition(id, result);
					} else return false;
				}
			}
			throw new CDSyntaxError("not must accept an Object!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "none"), value -> {
			if (value instanceof JsonArray) {
				JsonArray json = (JsonArray) value;
				for (JsonElement elem : json) {
					if (elem instanceof JsonObject) {
						JsonObject obj = (JsonObject) elem;
						for (String key : obj.keySet()) {
							if (ConditionalData.testCondition(new class_2960(key), ConditionalData.parseElement(obj.get(key)))) return false;
						}
					}
				}
				return true;
			}
			throw new CDSyntaxError("none must accept an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "or"), value -> {
			if (value instanceof JsonArray) {
				JsonArray json = (JsonArray) value;
				for (JsonElement elem : json) {
					if (elem instanceof JsonObject) {
						JsonObject obj = (JsonObject) elem;
						for (String key : obj.keySet()) {
							if (ConditionalData.testCondition(new class_2960(key), ConditionalData.parseElement(obj.get(key)))) return true;
						}
					}
				}
			}
			throw new CDSyntaxError("or must accept an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "xor"), value -> {
			if (value instanceof JsonArray) {
				JsonArray json = (JsonArray) value;
				boolean ret = false;
				for (JsonElement elem : json) {
					if (elem instanceof JsonObject) {
						JsonObject obj = (JsonObject) elem;
						for (String key : obj.keySet()) {
							if (ConditionalData.testCondition(new class_2960(key), ConditionalData.parseElement(obj.get(key)))) {
								if(ret) return false;
								else ret = true;
							}
						}
					}
				}
				return ret;
			}
			throw new CDSyntaxError("xor must accept an Array!");
		});
		manager.registerCondition(new class_2960(CDCommons.MODID, "dev_mode"), value -> {
			if (value instanceof Boolean) return (Boolean)value == LibCD.isDevMode();
			throw new CDSyntaxError("dev_mode must accept a Boolean!");
		});
	}

	@Override
	public void initAdvancementRewards(AdvancementRewardsManager manager) {
		//no-op
	}
}
