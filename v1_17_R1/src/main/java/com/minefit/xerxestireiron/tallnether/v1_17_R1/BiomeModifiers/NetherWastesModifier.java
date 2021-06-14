package com.minefit.xerxestireiron.tallnether.v1_17_R1.BiomeModifiers;

import com.minefit.xerxestireiron.tallnether.v1_17_R1.BiomeDecorators;
import com.minefit.xerxestireiron.tallnether.v1_17_R1.SurfaceComposites;
import com.minefit.xerxestireiron.tallnether.v1_17_R1.WorldInfo;
import com.minefit.xerxestireiron.tallnether.v1_17_R1.Transition.TStructureFeatures;
import com.minefit.xerxestireiron.tallnether.v1_17_R1.Transition.TWorldGenCarvers;
import com.minefit.xerxestireiron.tallnether.v1_17_R1.Transition.TWorldGenStage;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.WorldGenCarvers;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.levelgen.WorldGenStage;

public class NetherWastesModifier extends BiomeModifier {
    private BiomeBase biomeBase;
    private BiomeDecorators biomeDecorators;
    private BiomeSettingsGeneration originalBiomeSettings;
    private BiomeSettingsGeneration modifiedBiomeSettings;
    private SurfaceComposites surfaceComposites;

    public NetherWastesModifier(WorldInfo worldInfo, BiomeBase biomeBase) {
        this.biomeBase = biomeBase;
        this.originalBiomeSettings = biomeBase.e();
        this.biomeDecorators = new BiomeDecorators("nether-wastes");
        this.surfaceComposites = new SurfaceComposites();
        this.modifiedBiomeSettings = createModifiedSettings();
    }

    public boolean modify() {
        return injectSettings(this.biomeBase, this.modifiedBiomeSettings);
    }

    @SuppressWarnings("unchecked")
    private BiomeSettingsGeneration createModifiedSettings () {
        BiomeSettingsGeneration.a biomeSettingsGeneration_a = new BiomeSettingsGeneration.a();

        // BiomeSettingsDefault.s()
        biomeSettingsGeneration_a.a(this.surfaceComposites.NETHER);
        biomeSettingsGeneration_a.a(TStructureFeatures.RUINED_PORTAL_NETHER);
        biomeSettingsGeneration_a.a(TStructureFeatures.NETHER_BRIDGE);
        biomeSettingsGeneration_a.a(TStructureFeatures.BASTION_REMNANT);
        biomeSettingsGeneration_a.a(TWorldGenStage.Features.AIR, TWorldGenCarvers.NETHER_CAVE);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.VEGETAL_DECORATION, this.biomeDecorators.SPRING_LAVA);

        // BiomeSettings.ac(biomesettingsgeneration_a)
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.VEGETAL_DECORATION, this.biomeDecorators.BROWN_MUSHROOM_NORMAL);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.VEGETAL_DECORATION, this.biomeDecorators.RED_MUSHROOM_NORMAL);

        // BiomeSettingsDefault.s()
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.SPRING_OPEN);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.PATCH_FIRE);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.PATCH_SOUL_FIRE);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.GLOWSTONE_EXTRA);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.GLOWSTONE);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.BROWN_MUSHROOM_NETHER);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.RED_MUSHROOM_NETHER);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_MAGMA);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.SPRING_CLOSED);


        // BiomeSettings.ar(biomesettingsgeneration_a)
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_GRAVEL_NETHER);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_BLACKSTONE);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_GOLD_NETHER);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_QUARTZ_NETHER);

        // BiomeSettings.as(biomesettingsgeneration_a)
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_DEBRIS_LARGE);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.ORE_DEBRIS_SMALL);

        // TallNether
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.TALLNETHER_GLOWSTONE1);
        biomeSettingsGeneration_a.a(TWorldGenStage.Decoration.UNDERGROUND_DECORATION, this.biomeDecorators.TALLNETHER_GLOWSTONE2);

        return biomeSettingsGeneration_a.a();
    }
}