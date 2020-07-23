package io.github.cottonmc.libcd.mixin;

import net.minecraft.class_3494;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_3494.class_5145.class)
public interface class_5145Accessor {
    @Invoker("<init>")
    static class_3494.class_5145 createClass_5145(class_3494.class_3496 entry, String string) {
        throw new UnsupportedOperationException();
    }
}
