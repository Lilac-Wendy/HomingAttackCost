package me.mfletcher.homing_attack;


import me.mfletcher.homing_attack.network.HomingMessages;
import me.mfletcher.homing_attack.network.protocol.AttackS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerHomingAttackInfo {
    private final ServerPlayer player;

    private final Entity target;
    private Vec3 velocity;
    private final int startTime;

    private float prevDist;

    public PlayerHomingAttackInfo(ServerPlayer player, Entity target) {
        this.player = player;
        this.target = target;
        startTime = player.getServer().getTickCount();

        velocity = target.position().subtract(player.position()).normalize().scale(HomingAttack.config.homingSpeed);
        player.setDeltaMovement(velocity);
        player.hasImpulse = true;
        player.hurtMarked = true;
        player.causeFoodExhaustion(1f);

        prevDist = player.distanceTo(target);

        sendHomingPacket(true);
    }

    public boolean tick() {
        if (player.getBoundingBox().expandTowards(velocity.scale(1.5)).intersects(target.getBoundingBox())) {
            target.hurt(player.level().damageSources().playerAttack(player), getDamage());
            player.setDeltaMovement(velocity.multiply(-1, 0, -1).normalize().add(0, 0.5, 0));
            player.hasImpulse = true;
            player.hurtMarked = true;
            sendHomingPacket(false);
            return false;
        } else if (player.getServer().getTickCount() - startTime >= HomingAttack.config.homingTicksTimeout ||
                player.level().getBlockCollisions(player, player.getBoundingBox()).iterator().hasNext()) {
            sendHomingPacket(false);
            return false;
        } else if (prevDist < (prevDist = player.distanceTo(target))) {
            sendHomingPacket(false);
            return false;
        }

        if (player.getServer().getTickCount() % 5 == 0)
            velocity = target.position().subtract(player.position()).normalize().scale(HomingAttack.config.homingSpeed);
        player.setDeltaMovement(velocity);
        player.hasImpulse = true;
        player.hurtMarked = true;
        return true;
    }

    private float getDamage() {
        final float[] damage = {HomingAttack.config.baseHomingDamage};
        player.getArmorSlots().forEach(itemStack -> {
            if (HomingConstants.IRON_ARMOR.contains(itemStack.getItem())) {
                damage[0] += HomingAttack.config.ironArmorHomingDamage;
            } else if (HomingConstants.GOLD_ARMOR.contains(itemStack.getItem())) {
                damage[0] += HomingAttack.config.goldArmorHomingDamage;
            } else if (HomingConstants.DIAMOND_ARMOR.contains(itemStack.getItem())) {
                damage[0] += HomingAttack.config.diamondArmorHomingDamage;
            } else if (HomingConstants.NETHERITE_ARMOR.contains(itemStack.getItem())) {
                damage[0] += HomingAttack.config.netheriteArmorHomingDamage;
            }
        });
        return damage[0];
    }

    private void sendHomingPacket(boolean isHoming) {
        for (Player p : player.level().players())
            if (p.distanceTo(player) < 128)
                HomingMessages.sendToPlayer(new AttackS2CPacket(player.getId(), isHoming), (ServerPlayer) p);
    }

    public String toString() {
        return player.getDisplayName() + " -> " + target.getDisplayName()
                + " with UUID " + target.getStringUUID();
    }

    public Entity getTarget() {
        return target;
    }
}
