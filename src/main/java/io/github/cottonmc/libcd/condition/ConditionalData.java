package io.github.cottonmc.libcd.condition;

import blue.endless.jankson.*;
import blue.endless.jankson.impl.SyntaxError;
import io.github.cottonmc.libcd.LibCD;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1802;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ConditionalData {
	private static final Map<class_2960, Predicate<Object>> conditions = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static void init() {
		registerCondition(new class_2960(LibCD.MODID, "mod_loaded"), (value) -> {
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
			return false;
		});
		registerCondition(new class_2960(LibCD.MODID, "item_exists"), (value) -> {
			if (value instanceof String) return class_2378.field_11142.method_10223(new class_2960((String)value)) != class_1802.field_8162;
			if (value instanceof List) {
				for (JsonElement el : (List<JsonElement>)value) {
					if (!(el instanceof JsonPrimitive)) return false;
					Object obj = ((JsonPrimitive)el).getValue();
					if (obj instanceof String) {
						if (class_2378.field_11142.method_10223(new class_2960((String)obj)) == class_1802.field_8162) return false;
					}  else return false;
				}
				return true;
			}
			return false;
		});
		registerCondition(new class_2960(LibCD.MODID, "not"), (value) -> {
			if (value instanceof JsonObject) {
				JsonObject json = (JsonObject)value;
				for (String key : json.keySet()) {
					class_2960 id = new class_2960(key);
					Object result = parseElement(json.get(key));
					if (hasCondition(id)) {
						return !testCondition(id, result);
					} else return false;
				}
			}
			return false;
		});
		registerCondition(new class_2960(LibCD.MODID, "or"), (value) -> {
			if (value instanceof JsonArray) {
				JsonArray json = (JsonArray) value;
				for (JsonElement elem : json) {
					if (elem instanceof JsonObject) {
						JsonObject obj = (JsonObject) elem;
						for (String key : obj.keySet()) {
							if (!testCondition(new class_2960(key), obj)) return false;
						}
					}
				}
			}
			return false;
		});
	}

	public static boolean shouldLoad(class_2960 resourceId, String meta) {
		try {
			JsonObject json = LibCD.jankson.load(meta);
			JsonElement elem = json.get("when");
			if (elem instanceof JsonArray) {
				JsonArray array = (JsonArray)elem;
				for (JsonElement condition : array) {
					if (!(condition instanceof JsonObject)) {
						LibCD.logger.error("Error parsing meta for {}: item {} in condition list not a JsonObject", resourceId, condition.toString());
						return false;
					}
					JsonObject obj = (JsonObject)condition;
					for (String key : obj.keySet()) {
						class_2960 id = key.equals("or")? new class_2960(LibCD.MODID, "or") : new class_2960(key);
						if (!testCondition(id, obj)) return false;
					}
				}
			}
			return true;
		} catch (SyntaxError e) {
			LibCD.logger.error("Error parsing meta for {}: {}", resourceId, e.getLineMessage());
		}
		return false;
	}

	@Nullable
	private static Object parseElement(JsonElement element) {
		if (element instanceof JsonPrimitive) {
			return ((JsonPrimitive)element).getValue();
		} else if (element instanceof JsonNull) {
			return null;
		} else {
			return element;
		}
	}

	/**
	 * Register a condition that recipes can use as a requirement for loading.
	 * @param id The Id of the condition, namespaced to prevent duplicates.
	 * @param condition What must be true for the condition to be met.
	 */
	public static void registerCondition(class_2960 id, Predicate<Object> condition) {
		conditions.put(id, condition);
	}

	public static boolean hasCondition(class_2960 id) {
		return conditions.containsKey(id);
	}

	public static boolean testCondition(class_2960 id, Object toTest) {
		if (!hasCondition(id)) {
			//put a log here if I can find a way to get it to trace back to which file it should check?
			return false;
		}
		return conditions.get(id).test(toTest);
	}
}
