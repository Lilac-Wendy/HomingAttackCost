package me.mfletcher.homing.client;


import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
    public static final KeyMapping HOMING_KEY = new KeyMapping(
            "key.homing.attack",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.homing.main");

    public static final KeyMapping BOOST_KEY = new KeyMapping(
            "key.homing.boost",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.homing.main");

    public static void register() {
        KeyMappingRegistry.register(HOMING_KEY);
        KeyMappingRegistry.register(BOOST_KEY);
    }
}
