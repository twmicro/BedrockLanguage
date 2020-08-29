package com.twmicro.bedrocklang

import net.minecraft.item.Items
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry

class MultiForge (val bedrockValue: String) {
    fun toJVM() : Any {
        val itemRegex = Regex("""@item\[.*?]""")
        val entityRegex = Regex("""@entity\[.*?]""")
        val blockRegex = Regex("""@block\[.*?]""")
        val biomeRegex = Regex("""@biome\[.*?]""")

        val isItem = itemRegex.matchEntire(bedrockValue) != null
        val isEntity = entityRegex.matchEntire(bedrockValue) != null
        val isBlock = blockRegex.matchEntire(bedrockValue) != null
        val isBiome = biomeRegex.matchEntire(bedrockValue) != null

        if(isItem) {
            ForgeRegistries.ITEMS.forEach {
                if(bedrockValue.contains(ForgeRegistries.ITEMS.getKey(it)?.namespace!!) && bedrockValue.contains(ForgeRegistries.ITEMS.getKey(it)?.path!!)) {
                    return it
                }
            }
        }
        else if(isEntity) {
            ForgeRegistries.ENTITIES.forEach {
                if(bedrockValue.contains(ForgeRegistries.ENTITIES.getKey(it)?.namespace!!) && bedrockValue.contains(ForgeRegistries.ENTITIES.getKey(it)?.path!!)) {
                    return it
                }
            }
        }
        else if(isBlock) {
            ForgeRegistries.BLOCKS.forEach {
                if(bedrockValue.contains(ForgeRegistries.BLOCKS.getKey(it)?.namespace!!) && bedrockValue.contains(ForgeRegistries.BLOCKS.getKey(it)?.path!!)) {
                    return it
                }
            }
        }
        else if(isBiome) {
            ForgeRegistries.BIOMES.forEach {
                if(bedrockValue.contains(ForgeRegistries.BIOMES.getKey(it)?.namespace!!) && bedrockValue.contains(ForgeRegistries.BIOMES.getKey(it)?.path!!)) {
                    return it
                }
            }
        }
        else throw BedrockException("Invalid Minecraft object reference!")
        return Items.AIR
    }
}