package com.simibubi.create.foundation.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.render.KineticRenderer;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
@Mixin(value = World.class, priority = 1100) // this and create.mixins.json have high priority to load after Performant
public class TileWorldHookMixin {

	final World self = (World) (Object) this;

	@Shadow @Final public boolean isRemote;

	@Shadow @Final protected Set<TileEntity> tileEntitiesToBeRemoved;

	@Inject(at = @At("TAIL"), method = "addTileEntity")
	private void onAddTile(TileEntity te, CallbackInfoReturnable<Boolean> cir) {
		if (isRemote) {
			CreateClient.kineticRenderer.get(self).queueAdd(te);
		}
	}

	/**
	 * Without this we don't unload instances when a chunk unloads.
	 */
	@Inject(at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;clear()V", ordinal = 0
	),
			method = "tickBlockEntities")
	private void onChunkUnload(CallbackInfo ci) {
		if (isRemote) {
			KineticRenderer kineticRenderer = CreateClient.kineticRenderer.get(self);
			for (TileEntity tile : tileEntitiesToBeRemoved) {
				kineticRenderer.remove(tile);
			}
		}
	}
}
