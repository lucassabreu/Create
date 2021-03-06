package com.simibubi.create;

import com.simibubi.create.modules.logistics.block.FlexcrateContainer;
import com.simibubi.create.modules.logistics.block.FlexcrateScreen;
import com.simibubi.create.modules.schematics.block.SchematicTableContainer;
import com.simibubi.create.modules.schematics.block.SchematicTableScreen;
import com.simibubi.create.modules.schematics.block.SchematicannonContainer;
import com.simibubi.create.modules.schematics.block.SchematicannonScreen;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.ContainerType.IFactory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.IContainerFactory;

@EventBusSubscriber(bus = Bus.MOD)
public enum AllContainers {

	SCHEMATIC_TABLE(SchematicTableContainer::new), 
	SCHEMATICANNON(SchematicannonContainer::new),
	FLEXCRATE(FlexcrateContainer::new),
	
	;

	public ContainerType<? extends Container> type;
	private IFactory<?> factory;

	private <C extends Container> AllContainers(IContainerFactory<C> factory) {
		this.factory = factory;
	}

	@SubscribeEvent
	public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> e) {

		for (AllContainers container : values()) {
			container.type = new ContainerType<>(container.factory)
					.setRegistryName(new ResourceLocation(Create.ID, container.name().toLowerCase()));
			e.getRegistry().register(container.type);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerScreenFactories() {
		bind(SCHEMATIC_TABLE, SchematicTableScreen::new);
		bind(SCHEMATICANNON, SchematicannonScreen::new);
		bind(FLEXCRATE, FlexcrateScreen::new);
	}

	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	private static <C extends Container, S extends Screen & IHasContainer<C>> void bind(AllContainers c, IScreenFactory<C, S> factory) {
		ScreenManager.registerFactory((ContainerType<C>) c.type, factory);
	}

}
