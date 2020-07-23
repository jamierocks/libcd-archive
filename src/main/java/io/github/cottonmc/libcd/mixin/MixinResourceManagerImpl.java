package io.github.cottonmc.libcd.mixin;

import com.google.common.base.Charsets;
import io.github.cottonmc.libcd.api.CDCommons;
import io.github.cottonmc.libcd.api.condition.ConditionalData;
import io.github.cottonmc.libcd.impl.ReloadListenersAccessor;
import io.github.cottonmc.libcd.impl.ResourceSearcher;
import net.minecraft.class_2960;
import net.minecraft.class_3294;
import net.minecraft.class_3296;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_3304;
import net.minecraft.resource.*;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

@Mixin(class_3304.class)
public abstract class MixinResourceManagerImpl implements class_3296, ReloadListenersAccessor, ResourceSearcher {

	@Shadow @Final private List<class_3302> listeners;

	@Shadow public abstract List<class_3298> method_14489(class_2960 id) throws IOException;

	@Shadow @Final private Map<String, class_3294> namespaceManagers;

	@Inject(method = "findResources(Ljava/lang/String;Ljava/util/function/Predicate;)Ljava/util/Collection;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void checkConditioalRecipes(String parent, Predicate<String> loadFilter, CallbackInfoReturnable<Collection<class_2960>> cir,
										Set<class_2960> foundResources, List<class_2960> sortedResources) {
		List<class_2960> sortedCopy = new ArrayList<>(sortedResources);
		for (class_2960 id : sortedCopy) {
			//don't try to load for things that use mcmetas already!
			if (id.method_12832().contains(".mcmeta") || id.method_12832().contains(".png")) continue;
			class_2960 metaId = new class_2960(id.method_12836(), id.method_12832() + ".mcmeta");
			if (libcd$contains(metaId)) {
				try {
					class_3298 meta = method_14486(metaId);
					String metaText = IOUtils.toString(meta.method_14482(), Charsets.UTF_8);
					if (!ConditionalData.shouldLoad(id, metaText)) {
						sortedResources.remove(id);
					}
				} catch (IOException e) {
					CDCommons.logger.error("Error when accessing resource metadata for %s: %s", id.toString(), e.getMessage());
				}
			}
		}
	}

	@Override
	public List<class_3302> libcd$getListeners() {
		return listeners;
	}

	public boolean libcd$contains(class_2960 id) {
		class_3300 manager = this.namespaceManagers.get(id.method_12836());
		return manager != null && ((ResourceSearcher) manager).libcd$contains(id);
	}


}
