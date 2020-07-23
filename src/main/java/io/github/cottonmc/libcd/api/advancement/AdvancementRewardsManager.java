package io.github.cottonmc.libcd.api.advancement;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.class_2960;
import net.minecraft.class_3222;

public class AdvancementRewardsManager {
    public static final AdvancementRewardsManager INSTANCE = new AdvancementRewardsManager();
    private final Map<class_2960, BiConsumer<class_3222, JsonObject>> handlers = Maps.newHashMap();

    private AdvancementRewardsManager() {
    }

    /**
     * Register a custom advancement reward handler.
     * @param id The ID of the handler to register.
     * @param handler A handler which accepts both the player earning this advancement and JSON configuration of the custom handler.
     */
    public void register(class_2960 id, BiConsumer<class_3222, JsonObject> handler) {
        handlers.put(id, handler);
    }

    /**
     * Register a custom advancement reward handler.
     * @param id The ID of the handler to register.
     * @param handler A handler which accepts the player earning this advancement.
     */
    public void register(class_2960 id, Consumer<class_3222> handler) {
        register(id, (serverPlayerEntity, o) -> handler.accept(serverPlayerEntity));
    }

    public Map<class_2960, BiConsumer<class_3222, JsonObject>> getHandlers() {
        return handlers;
    }
}
