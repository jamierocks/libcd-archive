package io.github.cottonmc.libcd;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.cottonmc.jankson.JanksonFactory;
import io.github.cottonmc.libcd.condition.ConditionalData;
import io.github.cottonmc.libcd.tweaker.*;
import io.github.cottonmc.libcd.util.CDConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2588;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.function.Predicate;

public class LibCD implements ModInitializer {
	public static final String MODID = "libcd";

	public static final Logger logger = LogManager.getLogger();
	public static CDConfig config;
	public static final Jankson jankson = JanksonFactory.createJankson();

	@Override
	public void onInitialize() {
		config = loadConfig();
		ConditionalData.init();
		ResourceManagerHelper.get(class_3264.field_14190).registerReloadListener(new TweakerLoader());
		Tweaker.addTweaker("RecipeTweaker", RecipeTweaker.INSTANCE);
		Tweaker.addAssistant("TweakerUtils", TweakerUtils.INSTANCE);
		TweakerStackGetter.registerGetter(new class_2960("minecraft", "potion"), (id) -> {
			class_1842 potion = class_1842.method_8048(id.toString());
			if (potion == class_1847.field_8984) return class_1799.field_8037;
			return class_1844.method_8061(new class_1799(class_1802.field_8574), potion);
		});
		CommandRegistry.INSTANCE.register(false, dispatcher -> dispatcher.register((
				class_2170.method_9247("cd_subset").requires(source -> source.method_9259(3))
						.then(class_2170.method_9244("subset", StringArgumentType.string())
								.executes(context -> changeSubset(context, context.getArgument("subset", String.class))))
						.then(class_2170.method_9247("-reset").executes(context -> changeSubset(context, "")))
		)));
	}

	private int changeSubset(CommandContext<class_2168> context, String setTo) {
		config.tweaker_subset = setTo;
		saveConfig(config);
		context.getSource().method_9226(new class_2588("libcd.reload.success"), false);
		(context.getSource()).method_9211().method_3848();
		return 1;
	}

	/**
	 * Moved to {@link ConditionalData#registerCondition(Identifier, Predicate)}
	 */
	@Deprecated
	public static void registerCondition(class_2960 id, Predicate<Object> condition) {
		ConditionalData.registerCondition(id, condition);
	}

	public CDConfig loadConfig() {
		try {
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
		} catch (Exception e) {
			logger.error("Error loading config: {}", e.getMessage());
		}
		return new CDConfig();
	}

	public void saveConfig(CDConfig config) {
		try {
			File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("libcd.json5").toFile();
			JsonElement json = jankson.toJson(config);
			String result = json.toJson(true, true);
			if (!file.exists()) file.createNewFile();
			FileOutputStream out = new FileOutputStream(file,false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("Error saving config: {}", e.getMessage());
		}
	}
}
