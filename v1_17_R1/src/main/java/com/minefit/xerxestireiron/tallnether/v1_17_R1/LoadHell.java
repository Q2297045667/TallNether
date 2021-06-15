package com.minefit.xerxestireiron.tallnether.v1_17_R1;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Supplier;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import com.minefit.xerxestireiron.tallnether.ConfigAccessor;
import com.minefit.xerxestireiron.tallnether.Messages;
import com.minefit.xerxestireiron.tallnether.ReflectionHelper;
import com.minefit.xerxestireiron.tallnether.WorldConfig;

import net.minecraft.core.QuartPos;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.levelgen.GeneratorSettingBase;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.NoiseSettings;

public class LoadHell {
    private final Messages messages;
    private final ConfigAccessor configAccessor = new ConfigAccessor();
    private final HashMap<String, WorldInfo> worldInfos;
    private final boolean isPaper;
    private boolean decoratorsModified;

    public LoadHell(ConfigurationSection worldConfig, boolean isPaper, String pluginName) {
        this.messages = new Messages(pluginName);
        this.worldInfos = new HashMap<>();
        this.isPaper = isPaper;
    }

    public boolean overrideDecorators(WorldInfo worldInfo) {
        worldInfo.collectBiomeData();
        worldInfo.basaltDeltasModifier.modify();
        worldInfo.crimsonForestModifier.modify();
        worldInfo.warpedForestModifier.modify();
        worldInfo.netherWastesModifier.modify();
        worldInfo.soulSandValleyModifier.modify();
        return true;
    }

    public boolean restoreDecorators(WorldInfo worldInfo) {
        return true;
    }

    public void addWorld(World world, ConfigurationSection worldConfig) {
        Environment environment = world.getEnvironment();

        if (environment != Environment.NETHER) {
            this.messages.unknownEnvironment(world.getName(), environment.toString());
            return;
        }

        String worldName = world.getName();
        this.configAccessor.newWorldConfig(worldName, new PaperSpigot(worldName, this.isPaper).settingsMap, true);
        WorldInfo worldInfo = new WorldInfo(world);
        this.worldInfos.putIfAbsent(worldName, worldInfo);

        // For the moment Minecraft still shares a single biome instance for all worlds
        // We need the chunk manager to get them
        if (!this.decoratorsModified) {
            this.decoratorsModified = overrideDecorators(worldInfo);
        }
    }

    public void removeWorld(World world) {
        this.worldInfos.remove(world.getName());
    }

    @SuppressWarnings("unchecked")
    public void overrideGenerator(World world) {
        String worldName = world.getName();
        WorldInfo worldInfo = this.worldInfos.get(worldName);

        if (worldInfo.modified) {
            return;
        }

        Environment environment = world.getEnvironment();

        if (environment != Environment.NETHER) {
            this.messages.unknownEnvironment(worldName, environment.toString());
            return;
        }

        /*if (worldInfo.originalGenName.equals("TallNether_ChunkGeneratorAbstract")
                || worldInfo.originalGenName.equals("TallNether_ChunkGeneratorAbstract_Paper")) {
            this.messages.alreadyEnabled(worldName);
            return;
        }*/

        if (!isRecognizedGenerator(environment, worldInfo.originalGenName)) {
            this.messages.unknownGenerator(worldName, worldInfo.originalGenName);
            return;
        }


        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        ChunkProviderServer chunkServer = (ChunkProviderServer) nmsWorld.getChunkProvider();
        ChunkGenerator originalGenerator = chunkServer.getChunkGenerator();
        WorldConfig worldConfig = this.configAccessor.getWorldConfig(worldName);

        try {
            Field gField = ReflectionHelper.getField(originalGenerator.getClass(), "g", true);
            gField.setAccessible(true);
            Supplier<GeneratorSettingBase> g = (Supplier<GeneratorSettingBase>) gField.get(originalGenerator); // settings -> g
            GeneratorSettingBase generatorsettingbase = (GeneratorSettingBase) g.get();
            NoiseSettings noisesettings = generatorsettingbase.b();
            Field uField = ReflectionHelper.getField(originalGenerator.getClass(), "u", true);
            uField.setAccessible(true);
            NoiseSampler sampler = (NoiseSampler) uField.get(originalGenerator);
            DimensionManager dimensionManager = nmsWorld.getDimensionManager();
            Field dimHeight = ReflectionHelper.getField(dimensionManager.getClass(), "G", true); // height -> G
            ReflectionHelper.setFinalInt(dimHeight, dimensionManager, 256);
            Field logicalHeight = ReflectionHelper.getField(dimensionManager.getClass(), "H", true); // logicalHeight -> H
            ReflectionHelper.setFinalInt(logicalHeight, dimensionManager, 256);
            Field seaLevel = ReflectionHelper.getField(generatorsettingbase.getClass(), "o", true); // seaLevel -> o
            ReflectionHelper.setFinalInt(seaLevel, generatorsettingbase, worldConfig.getWorldValues().lavaSeaLevel);
            Field noiseHeight = ReflectionHelper.getField(noisesettings.getClass(), "c", true); // height -> c
            ReflectionHelper.setFinalInt(noiseHeight, noisesettings, 256);
            Field genHeight = ReflectionHelper.getField(originalGenerator.getClass(), "t", true); // height -> t
            ReflectionHelper.setFinalInt(genHeight, originalGenerator, 256);
            int cellCountY = 256 / QuartPos.b(noisesettings.g());
            Field genCellCountY = ReflectionHelper.getField(originalGenerator.getClass(), "m", true); // cellCountY -> m
            ReflectionHelper.setFinalInt(genCellCountY, originalGenerator, 256 / QuartPos.b(noisesettings.g()));
            Field samplerCellCountY = ReflectionHelper.getField(sampler.getClass(), "f", true); // cellCountY -> f
            ReflectionHelper.setFinalInt(samplerCellCountY, sampler, cellCountY);
            this.messages.enableSuccess(worldName);
        } catch (Exception e) {
            e.printStackTrace();
            this.messages.enableFailed(worldName);
        }
    }

    public boolean restoreGenerator(World world) {
        return true;
    }

    private boolean isRecognizedGenerator(Environment environment, String originalGenName) {
        if (environment == Environment.NETHER) {
            return originalGenName.equals("ChunkGeneratorAbstract") || originalGenName.equals("TimedChunkGenerator");
        }

        return false;
    }
}
