package com.minefit.xerxestireiron.tallnether.v1_13_R2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.Listener;

import com.minefit.xerxestireiron.tallnether.Messages;

import net.minecraft.server.v1_13_R2.BiomeLayout;
import net.minecraft.server.v1_13_R2.Biomes;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.ChunkGenerator;
import net.minecraft.server.v1_13_R2.ChunkProviderServer;
import net.minecraft.server.v1_13_R2.ChunkTaskScheduler;
import net.minecraft.server.v1_13_R2.GeneratorSettingsNether;
import net.minecraft.server.v1_13_R2.WorldProvider;
import net.minecraft.server.v1_13_R2.WorldServer;

public class LoadHell implements Listener {
    private final World world;
    private final WorldServer nmsWorld;
    private final String worldName;
    private String originalGenName;
    private WorldProvider worldProvider;
    private final Messages messages;
    private ChunkGenerator<?> originalGenerator;
    private ChunkProviderServer chunkServer;
    private final ConfigurationSection worldConfig;
    private boolean enabled = false;
    public final ConfigValues configValues;
    private final Decorators decorators;

    public LoadHell(World world, ConfigurationSection worldConfig, String pluginName) {
        this.world = world;
        this.worldConfig = worldConfig;
        this.nmsWorld = ((CraftWorld) world).getHandle();
        this.worldName = this.world.getName();
        this.configValues = new ConfigValues(this.worldName, this.worldConfig);
        this.messages = new Messages(pluginName);
        this.chunkServer = this.nmsWorld.getChunkProviderServer();
        this.originalGenerator = this.chunkServer.getChunkGenerator();
        this.originalGenName = this.originalGenerator.getClass().getSimpleName();
        this.worldProvider = this.nmsWorld.worldProvider;
        this.decorators = new Decorators(this.configValues);
        overrideGenerator();
    }

    public void restoreGenerator() {
        if (this.enabled) {
            if (!setGenerator(this.originalGenerator, true) || !this.decorators.restore()) {
                this.messages.restoreFailed(this.worldName);
            }

            this.enabled = false;
        }
    }

    public void overrideGenerator() {
        GeneratorSettingsNether generatorsettingsnether = new GeneratorSettingsNether();
        generatorsettingsnether.a(Blocks.NETHERRACK.getBlockData());
        generatorsettingsnether.b(Blocks.LAVA.getBlockData());
        Environment environment = this.world.getEnvironment();
        TallNether_ChunkProviderHell tallNetherGenerator = new TallNether_ChunkProviderHell(this.nmsWorld,
                BiomeLayout.b.a(BiomeLayout.b.b().a(Biomes.j)), generatorsettingsnether, this.configValues);
        this.decorators.initialize();

        if (environment != Environment.NETHER) {
            this.messages.unknownEnvironment(this.worldName, environment.toString());
            return;
        }

        if (this.originalGenName.equals("TallNether_ChunkProviderHell")) {
            this.messages.alreadyEnabled(this.worldName);
            return;
        }

        if (!isRecognizedGenerator(environment, this.originalGenName)) {
            this.messages.unknownGenerator(this.worldName, originalGenName);
            return;
        }

        this.enabled = setGenerator(tallNetherGenerator, false);

        if (this.enabled) {
            this.messages.enableSuccess(this.worldName);
        } else {
            this.messages.enableFailed(this.worldName);
        }
    }

    private boolean isRecognizedGenerator(Environment environment, String originalGenName) {
        if (environment == Environment.NETHER) {
            return originalGenName.equals("NetherChunkGenerator") || originalGenName.equals("TimedChunkGenerator");
        }

        return false;
    }

    private boolean setGenerator(ChunkGenerator<?> generator, boolean heightValue) {
        try {
            Field chunkGenerator = this.chunkServer.getClass().getDeclaredField("chunkGenerator");
            chunkGenerator.setAccessible(true);
            setFinal(chunkGenerator, this.chunkServer, generator);

            Field worldHeight = this.worldProvider.getClass().getSuperclass().getDeclaredField("d");
            worldHeight.setAccessible(true);
            worldHeight.setBoolean(this.worldProvider, heightValue);

            Field scheduler = this.chunkServer.getClass().getDeclaredField("chunkScheduler");
            scheduler.setAccessible(true);
            ChunkTaskScheduler taskScheduler = (ChunkTaskScheduler) scheduler.get(this.chunkServer);

            Field schedulerGenerator = taskScheduler.getClass().getDeclaredField("d");
            scheduler.setAccessible(true);
            setFinal(schedulerGenerator, taskScheduler, generator);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void setFinal(Field field, Object instance, Object obj) throws Exception {
        field.setAccessible(true);
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(instance, obj);
    }
}
