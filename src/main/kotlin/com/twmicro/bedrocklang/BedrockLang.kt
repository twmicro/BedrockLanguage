package com.twmicro.bedrocklang

import com.twmicro.bedrocklang.types.ModBlocks
import com.twmicro.bedrocklang.types.ModItems
import net.minecraft.block.AbstractBlock
import net.minecraft.block.material.Material
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import net.minecraft.block.Block
import net.minecraft.item.*
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.common.ToolType
import net.minecraftforge.registries.ForgeRegistries

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

    abstract class TieredBedrock (registry_name: String){
        @BedrockType
        var maxUses: Int? = null

        @BedrockType
        var damage: Float? = null

        @BedrockType
        var efficiency: Float? = null

        @BedrockType
        var harvestLevel: Int? = null

        @BedrockType
        var repairMaterial: MultiForge? = null

        @BedrockType
        var enchantability: Int? = null

        @BedrockType
        var group: MultiStatic<ItemGroup>? = null

        abstract fun execute()

        fun getTier() : IItemTier {
            if(damage == null) damage = 1F
            if(efficiency == null) efficiency = 1F
            if(harvestLevel == null) harvestLevel = 1
            if(maxUses == null) maxUses = 50
            if(enchantability == null) enchantability = 50
            if(repairMaterial == null) repairMaterial = MultiForge("@item[minecraft:barrier]")
            if(group == null) group = MultiStatic(ItemGroup::class, "building_blocks")
            return object: IItemTier {
                override fun getMaxUses(): Int {
                    return this@TieredBedrock.maxUses!!
                }

                override fun getEfficiency(): Float {
                    return this@TieredBedrock.efficiency!!
                }

                override fun getAttackDamage(): Float {
                    return this@TieredBedrock.damage!!
                }

                override fun getHarvestLevel(): Int {
                    return this@TieredBedrock.harvestLevel!!
                }

                override fun getEnchantability(): Int {
                    return this@TieredBedrock.enchantability!!
                }

                override fun getRepairMaterial(): Ingredient {
                    return Ingredient.fromItems(this@TieredBedrock.repairMaterial!!.toJVM() as Item)
                }
            }
        }
    }

    @BedrockType
    class sword (val registry_name: String) : TieredBedrock(registry_name) {
        @BedrockType
        var attackSpeed: Float? = null

        override fun execute() {
            if(attackSpeed == null) attackSpeed = 0.1F
            ModItems.REGISTRY.register(registry_name) {
                SwordItem(getTier(), damage!!.toInt(), attackSpeed!!, Item.Properties().group(group!!.toJVM()!! as ItemGroup))
            }
        }
    }

    @BedrockType
    class pickaxe (val registry_name: String) : TieredBedrock(registry_name) {
        @BedrockType
        var attackSpeed: Float? = null

        @BedrockType
        var level : Int? = null

        override fun execute() {
            if(attackSpeed == null) attackSpeed = 0.1F
            if(level != null) level = 1
            ModItems.REGISTRY.register(registry_name) {
                PickaxeItem(getTier(), damage!!.toInt(), attackSpeed!!, Item.Properties().group(group!!.toJVM()!! as ItemGroup).addToolType(
                    ToolType.AXE, level!!))
            }
        }
    }

    @BedrockType
    class axe (val registry_name: String) : TieredBedrock(registry_name) {
        @BedrockType
        var attackSpeed: Float? = null

        @BedrockType
        var level : Int? = null

        override fun execute() {
            if(attackSpeed == null) attackSpeed = 0.1F
            if(level != null) level = 1
            ModItems.REGISTRY.register(registry_name) {
                AxeItem(getTier(), damage!!, attackSpeed!!, Item.Properties().group(group!!.toJVM()!! as ItemGroup).addToolType(ToolType.AXE, level!!))
            }
        }
    }

    @BedrockType
    class hoe (val registry_name: String) : TieredBedrock(registry_name) {
        @BedrockType
        var attackSpeed: Float? = null

        @BedrockType
        var level : Int? = null

        override fun execute() {
            if(attackSpeed == null) attackSpeed = 0.1F
            if(level != null) level = 1
            ModItems.REGISTRY.register(registry_name) {
                HoeItem(getTier(),
                    damage!!.toInt(), attackSpeed!!, Item.Properties().group(group!!.toJVM()!! as ItemGroup).addToolType(ToolType.HOE, level!!))
            }
        }
    }

    @BedrockType
    class shovel (val registry_name: String) : TieredBedrock(registry_name) {
        @BedrockType
        var attackSpeed: Float? = null

        @BedrockType
        var level : Int? = null

        override fun execute() {
            if(attackSpeed == null) attackSpeed = 0.1F
            if(level != null) level = 1
            ModItems.REGISTRY.register(registry_name) {
                ShovelItem(getTier(),
                    damage!!, attackSpeed!!, Item.Properties().group(group!!.toJVM()!! as ItemGroup).addToolType(ToolType.SHOVEL, level!!))
            }
        }
    }


}