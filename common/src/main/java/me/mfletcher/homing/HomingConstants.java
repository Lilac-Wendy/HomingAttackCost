package me.mfletcher.homing;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class HomingConstants {
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
}
