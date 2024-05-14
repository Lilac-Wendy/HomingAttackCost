package me.mfletcher.homing;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "assets.homing")
public class ModConfig implements ConfigData {
    public int homingRange = 20;

    public int homingSpeed = 3;

    @ConfigEntry.Gui.Excluded
    public int homingTicksTimeout = 40;

    @ConfigEntry.Gui.Tooltip
    public int boostLevel = 50;

    public float baseHomingDamage = 0.5f;
    public float defenseHomingDamageMultiplier = 0.3f;
    public float toughnessHomingDamageMultiplier = 2.5f;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int reticleVolume = 100;
}
