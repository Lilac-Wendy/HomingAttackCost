package me.mfletcher.homing.data;


import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.AttackS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

public class PlayerHomingAttackInfo {
    private final ServerPlayer player;

    private final Entity target;
    private Vec3 velocity;
    private final int startTime;

    private float prevDist;

    public PlayerHomingAttackInfo(ServerPlayer player, Entity target) {
        this.player = player;
        this.target = target;
        startTime = player.tickCount;

        velocity = target.position().subtract(player.position()).normalize().scale(HomingAttack.config.homingSpeed);
        player.setDeltaMovement(velocity);
        player.hasImpulse = true;
        player.hurtMarked = true;
        player.causeFoodExhaustion(1f);

        prevDist = player.distanceTo(target);

        sendHomingPacket(true);
    }

    public boolean tick() {
        try (Level level = player.level()) {
            if (player.getBoundingBox().inflate(2).intersects(target.getBoundingBox().inflate(2))) {
                target.hurt(level.damageSources().playerAttack(player), getDamage());
                player.setDeltaMovement(velocity.multiply(-1, 0, -1).normalize().add(0, 0.5, 0));
                player.hasImpulse = true;
                player.hurtMarked = true;
                sendHomingPacket(false);
                return false;
            } else if (player.tickCount - startTime >= HomingAttack.config.homingTicksTimeout ||
                    level.getBlockCollisions(player, player.getBoundingBox()).iterator().hasNext()) {
                sendHomingPacket(false);
                return false;
            } else if (prevDist < (prevDist = player.distanceTo(target))) {
                sendHomingPacket(false);
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (player.tickCount % 5 == 0)
            velocity = target.position().subtract(player.position()).normalize().scale(HomingAttack.config.homingSpeed);
        player.setDeltaMovement(velocity);
        player.hasImpulse = true;
        player.hurtMarked = true;
        return true;
    }

    private float getDamage() {
        final float[] damage = {HomingAttack.config.baseHomingDamage};
        player.getArmorSlots().forEach(itemStack -> {
            if (itemStack.getItem() instanceof ArmorItem armorItem)
                damage[0] += armorItem.getDefense() * HomingAttack.config.defenseHomingDamageMultiplier + armorItem.getToughness() * HomingAttack.config.toughnessHomingDamageMultiplier;
        });
        return damage[0];
    }

    private void sendHomingPacket(boolean isHoming) {
        try (Level level = player.level()) {
            for (Player p : level.players())
                if (p.distanceTo(player) < 128)
                    HomingMessages.sendToPlayer(new AttackS2CPacket(player.getId(), isHoming), (ServerPlayer) p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return player.getDisplayName() + " -> " + target.getDisplayName()
                + " with UUID " + target.getStringUUID();
    }

    public Entity getTarget() {
        return target;
    }
}
