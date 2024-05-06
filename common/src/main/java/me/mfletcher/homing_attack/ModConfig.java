package me.mfletcher.homing_attack;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "assets.homing_attack")
public class ModConfig implements ConfigData {
    public int homingRange = 20;

    int homingSpeed = 3;

    @ConfigEntry.Gui.Excluded
    int homingTicksTimeout = 40;

    @ConfigEntry.Gui.Tooltip
    public int boostLevel = 50;

    float baseHomingDamage = 0.5f;
    float ironArmorHomingDamage = 0.5f;
    float goldArmorHomingDamage = 1f;
    float diamondArmorHomingDamage = 1.5f;
    float netheriteArmorHomingDamage = 2.5f;
}
