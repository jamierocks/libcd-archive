package io.github.cottonmc.libcd.mixin;

import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.impl.TagBuilderWarningAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_2960;
import net.minecraft.class_3494;
import net.minecraft.class_3503;

@Mixin(class_3503.class)
public class MixinTagContainer {


    @Inject(method = "applyReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/tag/Tag$Builder;build(Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/util/Optional;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPut(
            Map<class_2960, class_3494.class_3495> map,
            CallbackInfo ci,
            Map map2,
            Function function,
            Function function2,
            boolean bl,
            Iterator iterator,
            Map.Entry<class_2960, class_3494.class_3495> entry
            ) {
        List<Object> warnings = ((TagBuilderWarningAccessor) entry.getValue()).getWarnings();
        if(!warnings.isEmpty()) {
            CDCommons.logger.warn("Found problems in tag extensions of tag " + entry.getKey() + ':');
            for (Object warning : warnings) {
                if (warning instanceof Throwable) {
                    Throwable t = (Throwable) warning;
                    CDCommons.logger.error("\t- %s", t.getMessage(), t);
                } else {
                    CDCommons.logger.warn("\t- %s", warning);
                }
            }

            warnings.clear();
        }
    }
}
