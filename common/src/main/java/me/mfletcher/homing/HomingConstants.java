package me.mfletcher.homing;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class HomingConstants {
    public static final Set<Item> IRON_ARMOR = new HashSet<>();
    public static final Set<Item> GOLD_ARMOR = new HashSet<>();
    public static final Set<Item> DIAMOND_ARMOR = new HashSet<>();
    public static final Set<Item> NETHERITE_ARMOR = new HashSet<>();

    static {
        IRON_ARMOR.add(Items.IRON_HELMET);
        IRON_ARMOR.add(Items.IRON_CHESTPLATE);
        IRON_ARMOR.add(Items.IRON_LEGGINGS);
        IRON_ARMOR.add(Items.IRON_BOOTS);

        GOLD_ARMOR.add(Items.GOLDEN_HELMET);
        GOLD_ARMOR.add(Items.GOLDEN_CHESTPLATE);
        GOLD_ARMOR.add(Items.GOLDEN_LEGGINGS);
        GOLD_ARMOR.add(Items.GOLDEN_BOOTS);

        DIAMOND_ARMOR.add(Items.DIAMOND_HELMET);
        DIAMOND_ARMOR.add(Items.DIAMOND_CHESTPLATE);
        DIAMOND_ARMOR.add(Items.DIAMOND_LEGGINGS);
        DIAMOND_ARMOR.add(Items.DIAMOND_BOOTS);

        NETHERITE_ARMOR.add(Items.NETHERITE_HELMET);
        NETHERITE_ARMOR.add(Items.NETHERITE_CHESTPLATE);
        NETHERITE_ARMOR.add(Items.NETHERITE_LEGGINGS);
        NETHERITE_ARMOR.add(Items.NETHERITE_BOOTS);
    }

    public static final KeyMapping HOMING_KEY = new KeyMapping(
            "key.homing_attack.attack",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.homing_attack.main");

    public static final KeyMapping BOOST_KEY = new KeyMapping(
            "key.homing_attack.boost",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.homing_attack.main");
}
