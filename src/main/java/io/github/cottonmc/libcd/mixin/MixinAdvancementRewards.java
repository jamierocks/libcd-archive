package io.github.cottonmc.libcd.mixin;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cottonmc.libcd.impl.CustomRewardsUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.class_170;
import net.minecraft.class_2960;
import net.minecraft.class_3222;

@Mixin(class_170.class)
public class MixinAdvancementRewards implements CustomRewardsUtils {
    private final Map<class_2960, JsonObject> settings = Maps.newHashMap();
    private final Map<class_2960, BiConsumer<class_3222, JsonObject>> appliers = Maps.newHashMap();

    @Inject(method = "apply", at = @At("TAIL"))
    public void onApply(class_3222 serverPlayerEntity, CallbackInfo ci) {
        getAllAppliers().forEach((id, applier) -> applier.accept(serverPlayerEntity, getSettings(id)));
    }

    @Inject(method = "toJson", at = @At("TAIL"), cancellable = true)
    public void onToJson(CallbackInfoReturnable<JsonElement> cir) {
        JsonObject jsonObject = cir.getReturnValue().getAsJsonObject();
        JsonArray jsonArray = new JsonArray();
        getAllSettings().forEach((id, settings) -> {
            if (settings == null) {
                jsonArray.add(id.toString());
            } else {
                JsonObject current = new JsonObject();
                current.addProperty("name", id.toString());
                current.add("settings", settings);
                jsonArray.add(current);
            }
        });
        jsonObject.add("libcd:custom", jsonArray);
        cir.setReturnValue(jsonObject);
    }

    @Override
    public Map<class_2960, JsonObject> getAllSettings() {
        return settings;
    }

    @Override
    public void setAllSettings(Map<class_2960, JsonObject> rewardsSettings) {
        settings.putAll(rewardsSettings);
    }

    @Override
    public JsonObject getSettings(class_2960 id) {
        return settings.get(id);
    }

    @Override
    public void setSettings(class_2960 id, JsonObject settings) {
        this.settings.put(id, settings);
    }

    @Override
    public Map<class_2960, BiConsumer<class_3222, JsonObject>> getAllAppliers() {
        return appliers;
    }

    @Override
    public void setAllAppliers(Map<class_2960, BiConsumer<class_3222, JsonObject>> appliers) {
        this.appliers.putAll(appliers);
    }

    @Override
    public BiConsumer<class_3222, JsonObject> getApplier(class_2960 id) {
        return appliers.get(id);
    }

    @Override
    public void setApplier(class_2960 id, BiConsumer<class_3222, JsonObject> applier) {
        appliers.put(id, applier);
    }
}
