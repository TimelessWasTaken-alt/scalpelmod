/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.timelesswastaken.scalpelmod.init;

import net.timelesswastaken.scalpelmod.item.ScalpelItem;
import net.timelesswastaken.scalpelmod.ScalpelmodMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

public class ScalpelmodModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ScalpelmodMod.MODID);
	public static final RegistryObject<Item> SCALPEL = REGISTRY.register("scalpel", () -> new ScalpelItem());
	// Start of user code block custom items
	// End of user code block custom items
}