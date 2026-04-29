/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.timelesswastaken.scalpelmod.init;

import net.timelesswastaken.scalpelmod.ScalpelmodMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScalpelmodModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ScalpelmodMod.MODID);
	public static final RegistryObject<CreativeModeTab> SCALPEL_MOD = REGISTRY.register("scalpel_mod",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.scalpelmod.scalpel_mod")).icon(() -> new ItemStack(ScalpelmodModItems.SCALPEL.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ScalpelmodModItems.SCALPEL.get());
			}).withSearchBar().build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(ScalpelmodModItems.SCALPEL.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(ScalpelmodModItems.SCALPEL.get());
		}
	}
}