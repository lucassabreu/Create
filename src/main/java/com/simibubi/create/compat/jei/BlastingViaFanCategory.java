package com.simibubi.create.compat.jei;

import com.mojang.blaze3d.platform.GlStateManager;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.ScreenResources;
import com.simibubi.create.foundation.gui.ScreenElementRenderer;
import com.simibubi.create.foundation.utility.Lang;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.util.ResourceLocation;

public class BlastingViaFanCategory extends ProcessingViaFanCategory<AbstractCookingRecipe> {

	private static ResourceLocation ID = new ResourceLocation(Create.ID, "blasting_via_fan");
	private IDrawable icon;

	public BlastingViaFanCategory() {
		icon = new DoubleItemIcon(() -> new ItemStack(AllItems.PROPELLER.get()),
				() -> new ItemStack(Items.LAVA_BUCKET));
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public ResourceLocation getUid() {
		return ID;
	}

	@Override
	public Class<? extends AbstractCookingRecipe> getRecipeClass() {
		return AbstractCookingRecipe.class;
	}

	@Override
	public String getTitle() {
		return Lang.translate("recipe.blastingViaFan");
	}

	@Override
	public IDrawable getBackground() {
		return new ScreenResourceWrapper(ScreenResources.FAN_RECIPE);
	}

	@Override
	public void renderAttachedBlock() {
		BlockState state = Blocks.LAVA.getDefaultState().with(FlowingFluidBlock.LEVEL, 8);
		// This is stupid
		GlStateManager.pushMatrix();
		GlStateManager.translated(0, 0, 200);
		GlStateManager.enableRescaleNormal();
		
		GlStateManager.pushMatrix();
		GlStateManager.translated(0, 200, 0);
		GlStateManager.rotated(90, 1, 0, 0);
		ScreenElementRenderer.renderBlock(() -> state);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translated(0, 200, 0);
		GlStateManager.rotated(90, 1, 0, 0);
		GlStateManager.rotated(270, 0, 0, 1);
		ScreenElementRenderer.renderBlock(() -> state);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translated(-103, -100, 0);
		ScreenElementRenderer.renderBlock(() -> state);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

}
