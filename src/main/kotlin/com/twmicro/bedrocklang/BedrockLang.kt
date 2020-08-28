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
                        val bedrockFileContent = file.inputStream().reader().readText()
                        bedrockSources.add(bedrockFileContent)
                        BedrockLex.parse(bedrockFileContent)
                    }
                }
            }
        }

        return bedrockSources.toTypedArray()
    }



    @BedrockType
    class block (val registry_name: String) {
        @BedrockType
        var breakingSound : MultiStatic<Material>? = null
        @BedrockType
        var hardness : Float? = null
        @BedrockType
        var resistance : Float? = null
        @BedrockType
        var slipperiness : Float? = null
        @BedrockType
        var speedFactor : Float? = null
        @BedrockType
        var group : MultiStatic<ItemGroup>? = null

        fun execute() {
            if(breakingSound == null) breakingSound = MultiStatic(Material::class, "iron")
            if(hardness == null) hardness = 0F
            if(resistance == null) resistance = 0F
            if(slipperiness == null) slipperiness = 0.6F
            if(speedFactor == null) speedFactor = 1.0F
            if(group == null) group = MultiStatic(ItemGroup::class, "building_blocks")
            val block = Block(AbstractBlock.Properties.create(breakingSound!!.toJVM() as Material).
                hardnessAndResistance(hardness!!, resistance!!).
                slipperiness(slipperiness!!).
                speedFactor(speedFactor!!))
            ModBlocks.REGISTRY.register(registry_name) { block }
            ModItems.REGISTRY.register(registry_name){
                BlockItem(block, Item.Properties().group(group!!.toJVM() as ItemGroup))
            }
        }
    }
}