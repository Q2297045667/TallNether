package com.minefit.XerxesTireIron.TallNether.v1_9_R2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import com.minefit.XerxesTireIron.TallNether.Messages;
import com.minefit.XerxesTireIron.TallNether.TallNether;

import net.minecraft.server.v1_9_R2.ChunkGenerator;
import net.minecraft.server.v1_9_R2.WorldServer;

public class LoadHell implements Listener {
    private TallNether plugin;
    private World world;
    private WorldServer nmsWorld;
    private Messages messages;
    private ChunkGenerator originalGenerator;
    private TravelAgent portalTravelAgent;
    private String worldConfig;

    public LoadHell(World world, TallNether instance) {
        this.plugin = instance;
        this.world = world;
        this.worldConfig = "worlds." + world.getName() + ".";
        this.nmsWorld = ((CraftWorld) world).getHandle();
        this.messages = new Messages(this.plugin);
        overrideGenerator();
        this.portalTravelAgent = new TallNether_CraftTravelAgent(this.nmsWorld);
    }

    public void restoreGenerator() {
        try {
            Field cp = net.minecraft.server.v1_9_R2.ChunkProviderServer.class.getDeclaredField("chunkGenerator");
            cp.setAccessible(true);
            setFinal(cp, this.originalGenerator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void overrideGenerator() {
        String worldName = this.world.getName();
        this.originalGenerator = this.nmsWorld.getChunkProviderServer().chunkGenerator;
        String originalGenName = this.originalGenerator.getClass().getSimpleName();
        boolean genFeatures = this.nmsWorld.getWorldData().shouldGenerateMapFeatures();
        long worldSeed = this.nmsWorld.getSeed();
        Environment environment = this.world.getEnvironment();

        if (environment != Environment.NETHER) {
            this.messages.unknownEnvironment(worldName, environment.toString());
            return;
        }

        if (originalGenName.equals("TallNether_ChunkProviderHell")) {
            this.messages.alreadyEnabled(worldName);
            return;
        }

        try {
            Field cp = net.minecraft.server.v1_9_R2.ChunkProviderServer.class.getDeclaredField("chunkGenerator");
            cp.setAccessible(true);

            if (!originalGenName.equals("NetherChunkGenerator")) {
                this.messages.unknownGenerator(worldName, originalGenName);
                return;
            }

            TallNether_ChunkProviderHell generator = new TallNether_ChunkProviderHell(this.nmsWorld, genFeatures,
                    worldSeed, this.worldConfig, this.plugin);
            setFinal(cp, generator);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.messages.enabledSuccessfully(worldName);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Location destination = event.getTo();

        if (destination == null) {
            return;
        }

        World targetWorld = destination.getWorld();

        if (targetWorld.getName() != this.nmsWorld.worldData.getName()) {
            return;
        }

        if (this.plugin.getConfig().getBoolean(this.worldConfig + ".enabled", false)) {
            event.setPortalTravelAgent(this.portalTravelAgent);
        }
    }

    public void setFinal(Field field, Object obj) throws Exception {
        field.setAccessible(true);

        Field mf = Field.class.getDeclaredField("modifiers");
        mf.setAccessible(true);
        mf.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(this.nmsWorld.getChunkProviderServer(), obj);
    }
}