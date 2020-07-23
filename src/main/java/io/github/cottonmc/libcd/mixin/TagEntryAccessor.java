package io.github.cottonmc.libcd.mixin;

import net.minecraft.class_3494;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//TODO: we can't split this between duck and impl, to my knowledge. Is there any way around that other than AWs?
@Mixin(class_3494.class_5145.class)
public interface TagEntryAccessor {
    @Invoker("<init>")
    static class_3494.class_5145 createTrackedEntry(class_3494.class_3496 entry, String string) {
        throw new UnsupportedOperationException();
    }
}
