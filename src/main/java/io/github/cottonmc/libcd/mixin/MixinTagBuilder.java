package io.github.cottonmc.libcd.mixin;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import io.github.cottonmc.libcd.api.util.GsonOps;
import io.github.cottonmc.libcd.api.util.JanksonOps;
import io.github.cottonmc.libcd.impl.TagBuilderWarningAccessor;
import io.github.cottonmc.libcd.loader.TagExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_3494;
import net.minecraft.class_3518;

@Mixin(class_3494.class_3495.class)
public class MixinTagBuilder implements TagBuilderWarningAccessor {

    @Shadow @Final private List<class_3494.class_5145> entries;
    @Unique
    private final List<Object> libcdWarnings = new ArrayList<>();

    @Inject(method = "read", at = @At(value = "RETURN", remap = false))
    private void onFromJson(JsonObject json, String string, CallbackInfoReturnable<class_3494.class_3495> cir) {
        try {
            if (json.has("libcd")) {
                TagExtensions.ExtensionResult result = TagExtensions.load(
                        (blue.endless.jankson.JsonObject) Dynamic.convert(
                                GsonOps.INSTANCE, JanksonOps.INSTANCE, class_3518.method_15296(json, "libcd")
                        )
                );

                if (result.shouldReplace()) {
                    entries.clear();
                }

                result.getEntries().forEach((entry) -> {
                    this.entries.add(class_5145Accessor.createClass_5145(entry, string));
                });

                libcdWarnings.addAll(result.getWarnings());
            }
        } catch (Exception e) {
            libcdWarnings.add(e);
        }
    }

    @Override
    public List<Object> getWarnings() {
        return libcdWarnings;
    }
}
