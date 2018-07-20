package com.minefit.xerxestireiron.tallnether.v1_12_R1;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigValues {

    private final ConfigurationSection worldConfig;
    public final PaperSpigot paperSpigot;
    public final int lavafallAttempts;
    public final int lavafallMinHeight;
    public final int lavafallMaxMinus;
    public final int lavafallMaxHeight;
    public final int glowstone1Attempts;
    public final int glowstone1MinHeight;
    public final int glowstone1MaxHeight;
    public final int glowstone2Attempts;
    public final int glowstone2MinHeight;
    public final int glowstone2MaxMinus;
    public final int glowstone2MaxHeight;
    public final int fireAttempts;
    public final int fireMinHeight;
    public final int fireMaxHeight;
    public final int brownShroomAttempts;
    public final int brownShroomMinHeight;
    public final int brownShroomMaxHeight;
    public final int redShroomAttempts;
    public final int redShroomMinHeight;
    public final int redShroomMaxHeight;
    public final int quartzAttempts;
    public final int quartzMinHeight;
    public final int quartzMaxMinus;
    public final int quartzMaxHeight;
    public final int magmaAttempts;
    public final int magmaMinHeight;
    public final int magmaMaxHeight;
    public final int magmaRangeSize;
    public final int magmaRangeMedian;
    public final int hiddenLavaAttempts;
    public final int hiddenLavaMinHeight;
    public final int hiddenLavaMaxMinus;
    public final int hiddenLavaMaxHeight;
    public final boolean generateFortress;
    public final int fortressMin;
    public final int fortressMax;
    public final boolean generateFarLands;
    public final int farLandsLowX;
    public final int farLandsLowZ;
    public final int farLandsHighX;
    public final int farLandsHighZ;
    public final int lavaSeaLevel;
    public final boolean generateGravel;
    public final boolean generateSoulsand;
    public final int gravelSoulsandLimit;
    public final boolean flatBedrockCeiling;
    public final boolean flatBedrockFloor;

    public ConfigValues(String worldName, ConfigurationSection worldConfig) {
        this.worldConfig = worldConfig;
        this.paperSpigot = new PaperSpigot(worldName, Bukkit.getName().contains("Paper"));
        this.lavafallAttempts = setDecoration("lavafall-attempts", 12, false);
        this.lavafallMinHeight = setDecoration("lavafall-min-height", 4, true);
        this.lavafallMaxHeight = setDecoration("lavafall-max-height", 248, true);
        this.lavafallMaxMinus = 256 - this.lavafallMaxHeight;
        this.glowstone1Attempts = setDecoration("glowstone1-attempts", 10, false);
        this.glowstone1MinHeight = setDecoration("glowstone1-min-height", 4, true);
        this.glowstone1MaxHeight = setDecoration("glowstone1-max-height", 248, true);
        this.glowstone2Attempts = setDecoration("glowstone2-attempts", 20, false);
        this.glowstone2MinHeight = setDecoration("glowstone2-min-height", 0, true);
        this.glowstone2MaxHeight = setDecoration("glowstone2-max-height", 256, true);
        this.glowstone2MaxMinus = 256 - this.glowstone2MaxHeight;
        this.fireAttempts = setDecoration("fire-attempts", 20, false);
        this.fireMinHeight = setDecoration("fire-min-height", 4, true);
        this.fireMaxHeight = setDecoration("fire-max-height", 248, true);
        this.brownShroomAttempts = setDecoration("brown-shroom-attempts", 2, false);
        this.brownShroomMinHeight = setDecoration("brown-shroom-min-height", 0, true);
        this.brownShroomMaxHeight = setDecoration("brown-shroom-max-height", 256, true);
        this.redShroomAttempts = setDecoration("red-shroom-attempts", 2, false);
        this.redShroomMinHeight = setDecoration("red-shroom-min-height", 0, true);
        this.redShroomMaxHeight = setDecoration("red-shroom-max-height", 256, true);
        this.quartzAttempts = setDecoration("quartz-attempts", 32, false);
        this.quartzMinHeight = setDecoration("quartz-min-height", 10, true);
        this.quartzMaxHeight = setDecoration("quartz-max-height", 246, true);
        this.quartzMaxMinus = 256 - this.quartzMaxHeight;
        this.magmaAttempts = setDecoration("magma-attempts", 4, false);
        this.magmaMinHeight = setDecoration("magma-min-height", 43, true);
        this.magmaMaxHeight = setDecoration("magma-max-height", 53, true);
        this.magmaRangeSize = this.magmaMaxHeight - this.magmaMinHeight;
        this.magmaRangeMedian = (int) (this.magmaRangeSize / 2);
        this.hiddenLavaAttempts = setDecoration("hidden-lava-attempts", 32, false);
        this.hiddenLavaMinHeight = setDecoration("hidden-lava-min-height", 10, true);
        this.hiddenLavaMaxHeight = setDecoration("hidden-lava-max-height", 246, true);
        this.hiddenLavaMaxMinus = 256 - this.hiddenLavaMaxHeight;
        this.generateFortress = this.worldConfig.getBoolean("generate-fortress", this.paperSpigot.generateFortress);
        this.fortressMin = setDecoration("fortress-min", 64, true);
        this.fortressMax = setDecoration("fortress-max", 90, true);
        this.generateFarLands = this.worldConfig.getBoolean("farlands", false);
        this.farLandsLowX = this.worldConfig.getInt("lowX", -12550824);
        this.farLandsLowZ = this.worldConfig.getInt("lowZ", -12550824);
        this.farLandsHighX = this.worldConfig.getInt("highX", 12550824);
        this.farLandsHighZ = this.worldConfig.getInt("highZ", 12550824);
        this.lavaSeaLevel = setDecoration("lava-sea-level", 48, true);
        this.generateGravel = this.worldConfig.getBoolean("generate-gravel", true);
        this.generateSoulsand = this.worldConfig.getBoolean("generate-soulsand", true);
        this.gravelSoulsandLimit = setDecoration("gravel-soulsand-limit", 128, true);
        this.flatBedrockCeiling = this.worldConfig.getBoolean("flat-bedrock-ceiling",
                this.paperSpigot.generateFlatBedrock);
        this.flatBedrockFloor = this.worldConfig.getBoolean("flat-bedrock-floor", this.paperSpigot.generateFlatBedrock);
    };

    private int setDecoration(String setting, int defaultValue, boolean safetyMax) {
        int value = this.worldConfig.getInt(setting, defaultValue);

        if (value < 0) {
            value = 0;
        }

        if (safetyMax && value > 256) {
            value = 256;
        }

        return value;
    }
}
