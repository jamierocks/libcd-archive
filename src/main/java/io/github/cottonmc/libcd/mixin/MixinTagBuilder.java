package io.github.cottonmc.libcd.mixin;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import io.github.cottonmc.libcd.api.util.GsonOps;
import io.github.cottonmc.libcd.api.util.JanksonOps;
import io.github.cottonmc.libcd.impl.TagBuilderWarningAccessor;
import io.github.cottonmc.libcd.loader.TagExtensions;
import io.github.cottonmc.libcd.tag.ItemTagHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.class_1792;
import net.minecraft.class_2960;
import net.minecraft.class_3494;
import net.minecraft.class_3518;

@Mixin(class_3494.class_3495.class)
public class MixinTagBuilder implements TagBuilderWarningAccessor {

    @Shadow @Final private List<class_3494.class_5145> entries;
    @Unique
    private final List<Object> libcdWarnings = new ArrayList<>();

    private class_2960 defaultEntry;

    @Inject(method = "read", at = @At(value = "RETURN", remap = false))
    private void onFromJson(JsonObject json, String string, CallbackInfoReturnable<class_3494.class_3495> info) {
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
                    this.entries.add(TagEntryAccessor.createTrackedEntry(entry, string));
                });

                libcdWarnings.addAll(result.getWarnings());
                defaultEntry = result.getDefaultEntry();
            }
        } catch (Exception e) {
            libcdWarnings.add(e);
        }
    }

    @Override
    public List<Object> libcd$getWarnings() {
        return libcdWarnings;
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "build", at = @At("RETURN"))
    private <T> void injectDefaultEntry(Function<class_2960, class_3494<T>> tagGetter, Function<class_2960, T> objectGetter, CallbackInfoReturnable<Optional<class_3494<T>>> info) {
        Optional<class_3494<T>> opt = info.getReturnValue();
        if (opt.isPresent()) {
            class_3494<T> tag = opt.get();
            T t = objectGetter.apply(defaultEntry);
            if (t instanceof class_1792) {
                ItemTagHelper.INSTANCE.add((class_3494<class_1792>) tag, (class_1792) t);
            }
        }
    }
}
