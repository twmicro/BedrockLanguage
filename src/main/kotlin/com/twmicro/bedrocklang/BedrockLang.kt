package com.twmicro.bedrocklang

import com.twmicro.bedrocklang.types.ModBlocks
import com.twmicro.bedrocklang.types.ModItems
import net.minecraft.block.AbstractBlock
import net.minecraft.block.material.Material
import net.minecraft.item.ItemGroup
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

object BedrockLang {
    val bedrockSourcesPath = "bedrock"

    fun init() : Array<String> {
        val bedrockSources = mutableListOf<String>()
        val folder = File(bedrockSourcesPath)
        if(!folder.isDirectory) {
            Files.createDirectories(Paths.get(bedrockSourcesPath))
        }
        else {
            val files = folder.listFiles()
            if(files != null) {
                for(file in files) {
                    if(!file.isDirectory) {
                        bedrockSources.add(file.inputStream().reader().readText())
                    }
                }
            }
        }

        return bedrockSources.toTypedArray()
    }

    @BedrockType
    class block (val registry_name: String) {
        @BedrockType
        var breakingSound = MultiStatic(Material::class, "iron")
        @BedrockType
        var hardness = 0F
        @BedrockType
        var resistance = 0F
        @BedrockType
        var slipperiness = 0.6F
        @BedrockType
        var speedFactor = 1F
        @BedrockType
        var group = MultiStatic(ItemGroup::class, "building_blocks")

        fun execute() {
            val block = Block(AbstractBlock.Properties.create(breakingSound.toJVM() as Material).
                hardnessAndResistance(hardness, resistance).
                slipperiness(slipperiness).
                speedFactor(speedFactor))
            ModBlocks.REGISTRY.register(registry_name) { block }
            ModItems.REGISTRY.register(registry_name){
                BlockItem(block, Item.Properties().group(group.toJVM() as ItemGroup))
            }
        }
    }
}