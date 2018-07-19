package com.minefit.xerxestireiron.tallnether.v1_13_R1;

import java.util.Random;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.ChunkGenerator;
import net.minecraft.server.v1_13_R1.GeneratorAccess;
import net.minecraft.server.v1_13_R1.GeneratorSettings;
import net.minecraft.server.v1_13_R1.WorldGenDecoratorFrequencyConfiguration;
import net.minecraft.server.v1_13_R1.WorldGenDecoratorNetherMagma;
import net.minecraft.server.v1_13_R1.WorldGenFeatureConfiguration;
import net.minecraft.server.v1_13_R1.WorldGenerator;

public class TallNether_WorldGenDecoratorNetherMagma extends WorldGenDecoratorNetherMagma {

    public TallNether_WorldGenDecoratorNetherMagma() {}

    public <C extends WorldGenFeatureConfiguration> boolean a(GeneratorAccess generatoraccess, ChunkGenerator<? extends GeneratorSettings> chunkgenerator, Random random, BlockPosition blockposition, WorldGenDecoratorFrequencyConfiguration worldgendecoratorfrequencyconfiguration, WorldGenerator<C> worldgenerator, C c0) {
        int i = generatoraccess.getSeaLevel() / 2 + 1;

        for (int j = 0; j < worldgendecoratorfrequencyconfiguration.a; ++j) {
            int k = random.nextInt(16);
            int l = ConfigValues.magmaMinHeight + random.nextInt(ConfigValues.magmaRangeSize);
            int i1 = random.nextInt(16);

            worldgenerator.generate(generatoraccess, chunkgenerator, random, blockposition.a(k, l, i1), c0);
        }

        return true;
    }
}