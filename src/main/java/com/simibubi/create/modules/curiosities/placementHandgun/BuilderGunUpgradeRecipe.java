package com.simibubi.create.modules.curiosities.placementHandgun;

import com.google.gson.JsonObject;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipes;
import com.simibubi.create.modules.curiosities.placementHandgun.BuilderGunItem.ComponentTier;
import com.simibubi.create.modules.curiosities.placementHandgun.BuilderGunItem.Components;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BuilderGunUpgradeRecipe implements ICraftingRecipe {

	private ShapedRecipe recipe;
	private Components component;
	private ComponentTier tier;
	
	public BuilderGunUpgradeRecipe(ShapedRecipe recipe, Components component, ComponentTier tier) {
		this.recipe = recipe;
		this.component = component;
		this.tier = tier;
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		return getRecipe().matches(inv, worldIn);
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			ItemStack handgun = inv.getStackInSlot(slot).copy();
			if (!AllItems.PLACEMENT_HANDGUN.typeOf(handgun))
				continue;
			BuilderGunItem.setTier(getUpgradedComponent(), getTier(), handgun);
			return handgun;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack handgun = new ItemStack(AllItems.PLACEMENT_HANDGUN.get());
		BuilderGunItem.setTier(getUpgradedComponent(), getTier(), handgun);
		return handgun;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public ResourceLocation getId() {
		return getRecipe().getId();
	}

//	@Override
//	public IRecipeType<?> getType() {
//		return AllRecipes.Types.BLOCKZAPPER_UPGRADE;
//	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return AllRecipes.BLOCKZAPPER_UPGRADE.serializer;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BuilderGunUpgradeRecipe> {

		@Override
		public BuilderGunUpgradeRecipe read(ResourceLocation recipeId, JsonObject json) {
			ShapedRecipe recipe = IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, json);
			
			Components component = Components.valueOf(JSONUtils.getString(json, "component"));
			ComponentTier tier = ComponentTier.valueOf(JSONUtils.getString(json, "tier"));
			return new BuilderGunUpgradeRecipe(recipe, component, tier);
		}

		@Override
		public BuilderGunUpgradeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ShapedRecipe recipe = IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, buffer);
			
			Components component = Components.valueOf(buffer.readString(buffer.readInt()));
			ComponentTier tier = ComponentTier.valueOf(buffer.readString(buffer.readInt()));
			return new BuilderGunUpgradeRecipe(recipe, component, tier);
		}

		@Override
		public void write(PacketBuffer buffer, BuilderGunUpgradeRecipe recipe) {
			IRecipeSerializer.CRAFTING_SHAPED.write(buffer, recipe.getRecipe());
			
			String name = recipe.getUpgradedComponent().name();
			String name2 = recipe.getTier().name();
			buffer.writeInt(name.length());
			buffer.writeString(name);
			buffer.writeInt(name2.length());
			buffer.writeString(name2);
		}
		
	}

	@Override
	public boolean canFit(int width, int height) {
		return getRecipe().canFit(width, height);
	}

	public ShapedRecipe getRecipe() {
		return recipe;
	}

	public Components getUpgradedComponent() {
		return component;
	}

	public ComponentTier getTier() {
		return tier;
	}

}
