package io.github.cottonmc.libcd.mixin;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cottonmc.libcd.api.advancement.AdvancementRewardsManager;
import io.github.cottonmc.libcd.impl.CustomRewardsUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.class_170;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3518;

@Mixin(class_170.class)
public class MixinAdvancementRewardsDeserializer {
    @Inject(method = "fromJson", at = @At("TAIL"), cancellable = true)
    private static void onDeserialize(
            JsonObject json,
            CallbackInfoReturnable<class_170> cir
    ) {
        CustomRewardsUtils value = (CustomRewardsUtils) cir.getReturnValue();

        Map<class_2960, BiConsumer<class_3222, JsonObject>> appliers = Maps.newHashMap();
        Map<class_2960, JsonObject> settings = Maps.newHashMap();

        class_3518.method_15292(
                class_3518.method_15295(json, "rewards"),
                "libcd:custom",
                new JsonArray()
        ).forEach(element -> {
            if (element.isJsonObject()) {
                JsonObject current = class_3518.method_15295(element, "libcd:custom array entry");
                class_2960 id = new class_2960(class_3518.method_15265(current, "name"));
                appliers.put(id, AdvancementRewardsManager.INSTANCE.getHandlers().get(id));
                settings.put(id, class_3518.method_15296(current, "settings"));
            } else {
                class_2960 id = new class_2960(class_3518.method_15287(element, "libcd:custom array entry"));
                appliers.put(id, AdvancementRewardsManager.INSTANCE.getHandlers().get(id));
                settings.put(id, null);
            }
        });

        value.setAllAppliers(appliers);
        value.setAllSettings(settings);
        cir.setReturnValue((class_170) value);
    }
}
