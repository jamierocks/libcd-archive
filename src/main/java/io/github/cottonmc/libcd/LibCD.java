package io.github.cottonmc.libcd;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.cottonmc.jankson.JanksonFactory;
import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.LibCDInitializer;
import io.github.cottonmc.libcd.api.condition.ConditionManager;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import io.github.cottonmc.libcd.command.DebugExportCommand;
import io.github.cottonmc.libcd.command.HeldItemCommand;
import io.github.cottonmc.libcd.loader.TweakerLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2588;
import net.minecraft.class_3264;
import java.io.File;
import java.io.FileOutputStream;

public class LibCD implements ModInitializer {

	public static CDConfig config;

	public static boolean isDevMode() {
		return config.dev_mode;
	}

	@Override
	public void onInitialize() {
		config = loadConfig();
		FabricLoader.getInstance().getEntrypoints("libcd", LibCDInitializer.class).forEach(init -> {
			init.initTweakers(TweakerManager.INSTANCE);
			init.initConditions(ConditionManager.INSTANCE);
		});
		ResourceManagerHelper.get(class_3264.field_14190).registerReloadListener(new TweakerLoader());
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			
			//New nodes
			LiteralCommandNode<class_2168> libcdNode = class_2170
					.method_9247("libcd")
					.build();

			LiteralCommandNode<class_2168> subsetNode = class_2170
					.method_9247("subset")
					.requires(source -> source.method_9259(3))
					.build();

			ArgumentCommandNode<class_2168, String> setSubsetNode = class_2170
					.method_9244("subset", StringArgumentType.string())
					.executes(context -> changeSubset(context, context.getArgument("subset", String.class)))
					.build();

			LiteralCommandNode<class_2168> resetSubsetNode = class_2170
					.method_9247("-reset")
					.executes(context -> changeSubset(context, ""))
					.build();
			
			LiteralCommandNode<class_2168> heldNode = class_2170
					.method_9247("held")
					.executes(new HeldItemCommand())
					.build();

			LiteralCommandNode<class_2168> debugNode = class_2170
					.method_9247("debug")
					.requires(source -> source.method_9259(3))
					.build();

			LiteralCommandNode<class_2168> debugExportNode = class_2170
					.method_9247("export")
					.executes(new DebugExportCommand())
					.build();

			//Stitch nodes together
			subsetNode.addChild(setSubsetNode);
			subsetNode.addChild(resetSubsetNode);
			libcdNode.addChild(subsetNode);
			libcdNode.addChild(heldNode);
			debugNode.addChild(debugExportNode);
			libcdNode.addChild(debugNode);
			dispatcher.getRoot().addChild(libcdNode);
		});
	}

	private int changeSubset(CommandContext<class_2168> context, String setTo) {
		config.tweaker_subset = setTo;
		saveConfig(config);
		context.getSource().method_9226(new class_2588("libcd.reload.success"), false);
		(context.getSource()).method_9211().method_3848();
		return 1;
	}

	public CDConfig loadConfig() {
		try {
			Jankson jankson = CDCommons.newJankson();
			File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("libcd.json5").toFile();
			if (!file.exists()) saveConfig(new CDConfig());
			JsonObject json = jankson.load(file);
			CDConfig result =  jankson.fromJson(json, CDConfig.class);
			JsonElement jsonElementNew = jankson.toJson(new CDConfig());
			if(jsonElementNew instanceof JsonObject){
				JsonObject jsonNew = (JsonObject) jsonElementNew;
				if(json.getDelta(jsonNew).size()>= 0){
					saveConfig(result);
				}
			}
			return result;
		} catch (Exception e) {
			CDCommons.logger.error("Error loading config: {}", e.getMessage());
		}
		return new CDConfig();
	}

	public void saveConfig(CDConfig config) {
		try {
			File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("libcd.json5").toFile();
			JsonElement json = CDCommons.newJankson().toJson(config);
			String result = json.toJson(true, true);
			if (!file.exists()) file.createNewFile();
			FileOutputStream out = new FileOutputStream(file,false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			CDCommons.logger.error("Error saving config: {}", e.getMessage());
		}
	}
}
