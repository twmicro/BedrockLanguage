package com.twmicro.bedrocklang.types

import com.twmicro.bedrocklang.ExampleMod
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object ModItems {
    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, ExampleMod.ID)

    val EXAMPLE_BLOCK = REGISTRY.register("example_block"){
        BlockItem(ModBlocks.EXAMPLE_BLOCK, Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
    }
}