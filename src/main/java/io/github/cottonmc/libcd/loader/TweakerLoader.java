package io.github.cottonmc.libcd.loader;

import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import io.github.cottonmc.libcd.LibCD;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3695;
import org.apache.commons.io.IOUtils;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TweakerLoader implements SimpleResourceReloadListener {
	public static Map<class_2960, String> TWEAKERS = new HashMap<>();
	public static final ScriptEngineManager SCRIPT_MANAGER = new ScriptEngineManager();

	@Override
	public CompletableFuture load(class_3300 manager, class_3695 profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			TWEAKERS.clear();
			Collection<class_2960> resources = manager.method_14488("tweakers", name -> true);
			for (class_2960 fileId : resources) {
				try {
					class_3298 res = manager.method_14486(fileId);
					String script = IOUtils.toString(res.method_14482(), StandardCharsets.UTF_8);
					int localPath = fileId.method_12832().indexOf('/')+1;
					class_2960 scriptId = new class_2960(fileId.method_12836(), fileId.method_12832().substring(localPath));
					TWEAKERS.put(scriptId, script);
				} catch (IOException e) {
					CDCommons.logger.error("Error when accessing tweaker script %s: %s", fileId.toString(), e.getMessage());
				}
			}
			String subset = LibCD.config.tweaker_subset;
			if (!subset.equals("")) {
				Collection<class_2960> setResources = manager.method_14488("tweakers_"+subset, name -> true);
				for (class_2960 fileId : setResources) {
					try {
						class_3298 res = manager.method_14486(fileId);
						String script = IOUtils.toString(res.method_14482(), StandardCharsets.UTF_8);
						TWEAKERS.put(fileId, script);
					} catch (IOException e) {
						CDCommons.logger.error("Error when accessing tweaker script %s in subset %s: %s", fileId.toString(), subset, e.getMessage());
					}
				}
			}
			return TWEAKERS;
		});
	}

	@Override
	public CompletableFuture<Void> apply(Object o, class_3300 manager, class_3695 profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			for (Tweaker tweaker : TweakerManager.INSTANCE.getTweakers()) {
				tweaker.prepareReload(manager);
			}
			int loaded = 0;
			for (class_2960 tweaker : TWEAKERS.keySet()) {
				String extension = tweaker.method_12832().substring(tweaker.method_12832().lastIndexOf('.') + 1);
				String script = TWEAKERS.get(tweaker);
				if (script == null) {
					CDCommons.logger.error("Tweaker script not found: " + tweaker.toString());
					continue;
				}
				ScriptEngine engine = SCRIPT_MANAGER.getEngineByExtension(extension);
				if (engine == null) {
					CDCommons.logger.error("Engine for tweaker script not found: " + tweaker.toString());
					continue;
				}
				try {
					if (!script.contains("libcd.require")) {
						CDCommons.logger.warn("WARNING! Script %s doesn't use the new `libcd.require` system! It will break in a future update!", tweaker.toString());
					}
					ScriptContext ctx = engine.getContext();
					ScriptBridge bridge = new ScriptBridge(engine, script, tweaker);
					for (String name : TweakerManager.INSTANCE.getLegacyAssistants().keySet()) {
						ctx.setAttribute(name, TweakerManager.INSTANCE.getLegacyAssistants().get(name).apply(bridge), ScriptContext.ENGINE_SCOPE);
					}
					ctx.setAttribute("libcd", bridge, ScriptContext.ENGINE_SCOPE);
					ctx.setAttribute("log", new CDLogger(tweaker.method_12836()), ScriptContext.ENGINE_SCOPE);
					engine.eval(script);
				} catch (ScriptException e) {
					CDCommons.logger.error("Error executing tweaker script %s: %s", tweaker.toString(), e.getMessage());
					continue;
				}
				loaded++;
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

	public String formatApplied(List<String> messages) {
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

	@Override
	public class_2960 getFabricId() {
		return new class_2960(CDCommons.MODID, "tweaker_loader");
	}

}