package me.mfletcher.homing;


import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.AttackS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public PlayerHomingAttackInfo(ServerPlayer player, Entity target) {
    this.player = player;
    this.target = target;
    startTime = Objects.requireNonNull(player.getServer()).getTickCount();

    velocity = target.position().subtract(player.position()).normalize().scale(HomingAttack.config.homingSpeed);
    player.setDeltaMovement(velocity);
    player.hasImpulse = true;
    player.hurtMarked = true;
    player.causeFoodExhaustion(1f);

    prevDist = player.distanceTo(target);

    sendHomingPacket(true);

    // Increment the player's scoreboard for homing attack usage. Under Testing. 
    Objective homingObjective = getOrCreateHomingAttackObjective(player);
    Score homingScore = player.getScoreboard().getOrCreatePlayerScore(player.getScoreboardName(), homingObjective);
    homingScore.setScore(homingScore.getScore() + 1);
}


    public boolean tick() {
        if (player.getBoundingBox().inflate(2).intersects(target.getBoundingBox().inflate(2))) {
            target.hurt(player.level().damageSources().playerAttack(player), getDamage());
            player.setDeltaMovement(velocity.multiply(-1, 0, -1).normalize().add(0, 0.5, 0));
            player.hasImpulse = true;
            player.hurtMarked = true;
            sendHomingPacket(false);
            return false;
        } else if (Objects.requireNonNull(player.getServer()).getTickCount() - startTime >= HomingAttack.config.homingTicksTimeout ||
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
            if(itemStack.getItem() instanceof ArmorItem armorItem)
                damage[0] += armorItem.getDefense() * HomingAttack.config.defenseHomingDamageMultiplier + armorItem.getToughness() * HomingAttack.config.toughnessHomingDamageMultiplier;
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
