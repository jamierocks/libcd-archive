package io.github.cottonmc.libcd.loader;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import io.github.cottonmc.parchment.api.Script;
import io.github.cottonmc.parchment.api.ScriptLoader;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3695;
import org.apache.commons.io.IOUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

//TODO: add another system to ScriptDataLoader to allow subsets and stuff, so I don't need to do this all myself?
public class TweakerLoader implements SimpleResourceReloadListener<Map<class_2960, ScriptBridge>> {
	public static Map<class_2960, ScriptBridge> SCRIPTS = new HashMap<>();

	@Override
	public CompletableFuture<Map<class_2960, ScriptBridge>> load(class_3300 manager, class_3695 profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			Map<class_2960, ScriptBridge> scripts = new HashMap<>();
			Collection<class_2960> resources = manager.method_14488("tweakers", name -> true);
			for (class_2960 fileId : resources) {
				int localPath = fileId.method_12832().indexOf('/')+1;
				class_2960 scriptId = new class_2960(fileId.method_12836(), fileId.method_12832().substring(localPath));
				try {
					class_3298 res = manager.method_14486(fileId);
					ScriptBridge script = (ScriptBridge) ScriptLoader.INSTANCE.loadScript(ScriptBridge::new, scriptId, res.method_14482());
					ScriptBridge oldScript = scripts.put(scriptId, script);
					if (oldScript != null) {
						CDCommons.logger.error("Duplicate script file ignored with ID %s", scriptId.toString());
					}
				} catch (IOException e) {
					CDCommons.logger.error("Error when accessing tweaker script %s: %s", scriptId.toString(), e.getMessage());
				}
			}
			String subset = LibCD.config.tweaker_subset;
			if (!subset.equals("")) {
				Collection<class_2960> setResources = manager.method_14488("tweakers_"+subset, name -> true);
				for (class_2960 fileId : setResources) {
					class_2960 scriptId = new class_2960(fileId.method_12836(), fileId.method_12832().substring("tweakers_".length()));
					try {
						class_3298 res = manager.method_14486(fileId);
						ScriptBridge script = (ScriptBridge) ScriptLoader.INSTANCE.loadScript(ScriptBridge::new, scriptId, res.method_14482());
						ScriptBridge oldScript = scripts.put(scriptId, script);
						if (oldScript != null) {
							CDCommons.logger.error("Duplicate script file ignored with ID %s", scriptId.toString());
						}
					} catch (IOException | IllegalArgumentException e) {
						CDCommons.logger.error("Error when accessing tweaker script %s (in subset %s): %s", scriptId.toString(), subset, e.getMessage());
					}
				}
			}
			return scripts;
		});
	}

	@Override
	public CompletableFuture<Void> apply(Map<class_2960, ScriptBridge> scripts, class_3300 manager, class_3695 profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			SCRIPTS = scripts;
			for (Tweaker tweaker : TweakerManager.INSTANCE.getTweakers()) {
				tweaker.prepareReload(manager);
			}
			int loaded = 0;
			for (class_2960 id : scripts.keySet()) {
				ScriptBridge script = scripts.get(id);
				if (!script.hasRun()) script.run();
				if (!script.hadError()) loaded++;
			}
			List<String> applied = new ArrayList<>();
			for (Tweaker tweaker : TweakerManager.INSTANCE.getTweakers()) {
				tweaker.applyReload(manager, executor);
				applied.add(tweaker.getApplyMessage());
			}
			String confirm = formatApplied(applied);
			if (loaded > 0) CDCommons.logger.info("Applied %s tweaker %s, including %s", loaded, (loaded == 1? "script" : "scripts"), confirm);
		});
	}

	private String formatApplied(List<String> messages) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < messages.size(); i++) {
			String message = messages.get(i);
			ret.append(message);
			if (i < messages.size() - 1) {
				if (messages.size() <= 2) ret.append(" ");
				else ret.append(", ");
			}
			if (i == messages.size() - 2) ret.append("and ");
		}
		return ret.toString();
	}

	public static JsonObject getDebugObject() {
		JsonObject ret = new JsonObject();
		JsonArray successful = new JsonArray();
		JsonArray errored = new JsonArray();
		for (class_2960 id : SCRIPTS.keySet()) {
			ScriptBridge bridge = SCRIPTS.get(id);
			if (bridge.hasErrored()) {
				errored.add(new JsonPrimitive(id.toString()));
			} else {
				successful.add(new JsonPrimitive(id.toString()));
			}
		}
		ret.put("successful", successful);
		ret.put("errored", errored);
		return ret;
	}

	@Override
	public class_2960 getFabricId() {
		return new class_2960(CDCommons.MODID, "tweaker_loader");
	}

}