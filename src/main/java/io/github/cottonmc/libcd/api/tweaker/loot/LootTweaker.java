package io.github.cottonmc.libcd.api.tweaker.loot;

import blue.endless.jankson.JsonObject;
import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import io.github.cottonmc.libcd.impl.LootTableMapAccessor;
import io.github.cottonmc.libcd.impl.ReloadListenersAccessor;
import net.minecraft.class_173;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_39;
import net.minecraft.class_52;
import net.minecraft.class_58;
import net.minecraft.class_60;
import net.minecraft.loot.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class LootTweaker implements Tweaker {
	public static final LootTweaker INSTANCE = new LootTweaker();
	private class_60 lootManager;
	private int tableCount;
	private Map<class_2960, MutableLootTable> tables = new HashMap<>();
	private CDLogger logger;
	private JsonObject tableDebug;

	@Override
	public void prepareReload(class_3300 manager) {
		tableDebug = new JsonObject();
		tables.clear();
		tableCount = 0;
		if (manager instanceof ReloadListenersAccessor) {
			List<class_3302> listeners = ((ReloadListenersAccessor)manager).libcd$getListeners();
			for (class_3302 listener : listeners) {
				if (listener instanceof class_60) {
					this.lootManager = (class_60)listener;
					return;
				}
			}
			logger.error("No loot manager was found! Tweaker cannot edit loot tables!");
			throw new IllegalStateException("No loot manager was found! Tweaker cannot edit loot tables!");
		}
		logger.error("No reload listeners accessor found! Tweaker cannot edit loot tables!");
		throw new IllegalStateException("No reload listeners accessor found! Tweaker cannot edit loot tables!");
	}

	@Override
	public void applyReload(class_3300 manager, Executor executor) {
		Map<class_2960, class_52> tableMap = new HashMap<>(((LootTableMapAccessor)lootManager).libcd$getLootTableMap());
		Map<class_2960, class_52> toAdd = new HashMap<>();
		for (class_2960 id : tables.keySet()) {
			toAdd.put(id, tables.get(id).get());
		}
		if (toAdd.containsKey(class_39.field_844)) {
			toAdd.remove(class_39.field_844);
			logger.error("Tried to redefine empty loot table, ignoring");
		}
		class_58 reporter = new class_58(class_173.field_1177, ((LootTableMapAccessor)lootManager).libcd$getConditionManager()::method_22564, toAdd::get);
		toAdd.forEach((id, table) -> check(reporter, id, table));
		reporter.method_361().forEach((context, message) -> {
			logger.error("Found validation problem in modified table %s: %s", context, message);
			class_2960 id = new class_2960(context.substring(1, context.indexOf('}')));
			toAdd.remove(id);
		});
		tableCount = toAdd.size();
		tableMap.putAll(toAdd);
		((LootTableMapAccessor)lootManager).libcd$setLootTableMap(tableMap);
	}

	private void check(class_58 reporter, class_2960 id, class_52 table) {
		table.method_330(reporter.method_22568(table.method_322()).method_22569("{" + id + "}", id));
	}

	@Override
	public String getApplyMessage() {
		return tableCount + " modified loot " + (tableCount == 1? "table" : "tables");
	}

	@Override
	public void prepareFor(ScriptBridge bridge) {
		this.logger = new CDLogger(bridge.getId().method_12836());
	}

	/**
	 * Get a new loot table, or create one if it doesn't yet exist.
	 * @param id The ID of the table to get or create.
	 * @return A modifiable form of that table.
	 */
	public MutableLootTable getTable(String id) {
		class_2960 tableId = new class_2960(id);
		if (tables.containsKey(tableId)) {
			return tables.get(tableId);
		} else {
			class_52 table = lootManager.method_367(tableId);
			MutableLootTable mutable = new MutableLootTable(table);
			tables.put(tableId, mutable);
			return mutable;
		}
	}

	@Override
	public JsonObject getDebugInfo() {
		return tableDebug;
	}

	public CDLogger getLogger() {
		return logger;
	}
}
