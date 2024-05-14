package me.mfletcher.homing.mixin;

import me.mfletcher.homing.mixinaccess.IKeyboardInputMixin;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input implements IKeyboardInputMixin {

    @Shadow
    @Final
    private Options options;

    @Unique
    private boolean homing$isBoosting;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void onTick(CallbackInfo ci) {
        if (homing$isBoosting) {
            this.up = true;
            this.down = false;
            this.left = false;
            this.right = false;
            this.forwardImpulse = 1f;
            this.leftImpulse = 0f;
            this.jumping = this.options.keyJump.isDown();
            this.shiftKeyDown = false;
            ci.cancel();
        }
    }

    @Unique
    public void homing$setBoosting(boolean isBoosting) {
        this.homing$isBoosting = isBoosting;
    }

}
