package me.mfletcher.homing.block;

import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.DashRingAnimS2CPacket;
import me.mfletcher.homing.sounds.HomingSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DashRingBlock extends DashBlock {
    private static final VoxelShape X_AXIS_AABB = Block.box(7.5, 0, 0, 8.5, 16, 16);
    private static final VoxelShape Z_AXIS_AABB = Block.box(0, 0, 7.5, 16, 16, 8.5);


    public DashRingBlock(Properties properties) {
        super(properties, HomingSounds.DASH_RING);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (direction.getAxis() == Direction.Axis.X) {
            return X_AXIS_AABB;
        }
        return Z_AXIS_AABB;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);

        if (entity instanceof ServerPlayer player && level instanceof ServerLevel serverLevel) {
            for (ServerPlayer p : serverLevel.players()) {
                if (player.distanceTo(p) <= 32) {
                    HomingMessages.sendToPlayer(new DashRingAnimS2CPacket(player.getId()), p);
                }
            }
        }
    }
}
