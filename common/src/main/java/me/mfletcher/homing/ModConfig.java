package me.mfletcher.homing;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "homing")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    public int homingRange = 20;

    int homingSpeed = 3;

    @ConfigEntry.Gui.Excluded
    int homingTicksTimeout = 40;

    @ConfigEntry.Gui.Tooltip
    public int boostLevel = 50;

    float baseHomingDamage = 0.5f;
    float defenseHomingDamageMultiplier = 0.3f;
    float toughnessHomingDamageMultiplier = 2.5f;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int reticleVolume = 100;
}