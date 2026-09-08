package com.github.talrey.createdeco.blocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.redstone.Orientation;
import com.mojang.serialization.MapCodec;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class CageLampBlock extends DirectionalBlock implements ProperWaterloggedBlock, IWrenchable {
  public final DustParticleOptions particle;

  protected static final VoxelShape AABB_UP = Block.box(
    5.0D, 0.0D, 5.0D,
    11.0D, 8.0D, 11.0D
  );

  protected static final VoxelShape AABB_DOWN = Block.box(
    5.0D, 8.0D, 5.0D,
    11.0D, 16.0D, 11.0D
  );

  protected static final VoxelShape AABB_EAST = Block.box(
    0.0D, 5.0D, 5.0D,
    8.0D, 11.0D, 11.0D
  );

  protected static final VoxelShape AABB_WEST = Block.box(
    8.0D, 5.0D, 5.0D,
    16.0D, 11.0D, 11.0D
  );

  protected static final VoxelShape AABB_SOUTH = Block.box(
    5.0D, 5.0D, 0.0D,
    11.0D, 11.0D, 8.0D
  );

  protected static final VoxelShape AABB_NORTH = Block.box(
    5.0D, 5.0D, 8.0D,
    11.0D, 11.0D, 16.0D
  );

  public CageLampBlock(Properties props, Vector3f color) {
    super(props);
    this.particle = new DustParticleOptions(
      (Math.round(color.x() * 255) << 16) | (Math.round(color.y() * 255) << 8) | Math.round(color.z() * 255),
      0.3f);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.LIT, false)
      .setValue(BlockStateProperties.INVERTED, false)
      .setValue(BlockStateProperties.FACING, Direction.UP)
      .setValue(WATERLOGGED, false));
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    return withWater(defaultBlockState()
      .setValue(BlockStateProperties.FACING, ctx.getClickedFace())
      .setValue(BlockStateProperties.LIT, ctx.getLevel().hasSignal(ctx.getClickedPos(), ctx.getClickedFace())),
    ctx);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
    return switch (state.getValue(BlockStateProperties.FACING)) {
      case UP    -> AABB_UP;
      case DOWN  -> AABB_DOWN;
      case EAST  -> AABB_EAST;
      case WEST  -> AABB_WEST;
      case SOUTH -> AABB_SOUTH;
      case NORTH -> AABB_NORTH;
    };
  }

  @Override
  public BlockState rotate (BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  protected BlockState updateShape (BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    updateWater(level, tickAccess, state, pos);
    return !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() :
      super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public FluidState getFluidState (BlockState pState) {
    return fluidState(pState);
  }

  public static boolean shouldBeLit (BlockState state, Level level, BlockPos pos) {
    Direction attach = state.getValue(BlockStateProperties.FACING).getOpposite();
    return state.getValue(BlockStateProperties.INVERTED) ^ level.hasSignal(pos.relative(attach), attach);
  }

  @Override
  protected void neighborChanged (BlockState state, Level level, BlockPos pos, Block neighbor, @Nullable Orientation orientation, boolean movedByPiston) {
    if (state.getValue(BlockStateProperties.LIT) != shouldBeLit(state, level, pos)) {
      toggle(state, level, pos);
    }
  }

  @Override
  protected InteractionResult useWithoutItem (BlockState state, Level level, BlockPos pos, Player entity, BlockHitResult hitResult) {
    BlockState next = this.toggle(state.cycle(BlockStateProperties.INVERTED), level, pos);
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    } else {
      float pitch = next.getValue(BlockStateProperties.INVERTED) ? 0.6f : 0.5f;
      level.playSound((Player)null, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3f, pitch);
      return InteractionResult.CONSUME;
    }
  }

  private BlockState toggle (BlockState state, Level level, BlockPos pos) {
    if (level.isClientSide()) {
      makeParticle(state, level, pos);
    }
    BlockState next = state.setValue(BlockStateProperties.LIT, shouldBeLit(state, level, pos));
    level.setBlock(pos, next, 3);
    return next;
  }

  private void makeParticle (BlockState state, LevelAccessor level, BlockPos pos) {
    Direction direction = state.getValue(BlockStateProperties.FACING);
    float x = pos.getX() + 0.5f + 0.1f * direction.getStepX();
    float y = pos.getY() + 0.5f + 0.1f * direction.getStepY();
    float z = pos.getZ() + 0.5f + 0.1f * direction.getStepZ();
    level.addParticle(particle, x, y, z, 0d, 0d, 0d);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(
      BlockStateProperties.LIT,
      BlockStateProperties.INVERTED,
      BlockStateProperties.FACING,
      WATERLOGGED
    );
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    return canSurvive(level, pos, (Direction)state.getValue(BlockStateProperties.FACING));
  }

  public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
    BlockPos opposite = pos.relative(facing.getOpposite());
    return Block.canSupportCenter(level, opposite, facing.getOpposite());
  }

  @Override
  protected MapCodec<? extends DirectionalBlock> codec() {
        return null;
    }
}
