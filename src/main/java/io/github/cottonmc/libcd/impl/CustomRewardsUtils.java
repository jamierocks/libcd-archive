package io.github.cottonmc.libcd.impl;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.class_2960;
import net.minecraft.class_3222;

public interface CustomRewardsUtils {
    Map<class_2960, JsonObject> getAllSettings();

    void setAllSettings(Map<class_2960, JsonObject> settings);

    JsonObject getSettings(class_2960 id);

    void setSettings(class_2960 id, JsonObject settings);

    Map<class_2960, BiConsumer<class_3222, JsonObject>> getAllAppliers();

    void setAllAppliers(Map<class_2960, BiConsumer<class_3222, JsonObject>> appliers);

    BiConsumer<class_3222, JsonObject> getApplier(class_2960 id);

    void setApplier(class_2960 id, BiConsumer<class_3222, JsonObject> applier);
}
