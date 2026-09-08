package com.github.talrey.createdeco.blocks;

import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.entity.LivingEntity;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
public class CatwalkRailingBlock extends Block implements IWrenchable, ProperWaterloggedBlock {
  private static final VoxelShape VOXEL_NORTH = Block.box(
          0d, 0d, 0d,
          16d, 14d, 2d
  );
  private static final VoxelShape VOXEL_SOUTH = Block.box(
          0d, 0d, 14d,
          16d, 14d, 16d
  );
  private static final VoxelShape VOXEL_EAST = Block.box(
          14d, 0d, 0d,
          16d, 14d, 16d
  );
  private static final VoxelShape VOXEL_WEST = Block.box(
          0d, 0d, 0d,
          2d, 14d, 16d
  );

  public static final BooleanProperty NORTH_FENCE = BlockStateProperties.NORTH;
  public static final BooleanProperty SOUTH_FENCE = BlockStateProperties.SOUTH;
  public static final BooleanProperty EAST_FENCE  = BlockStateProperties.EAST;
  public static final BooleanProperty WEST_FENCE  = BlockStateProperties.WEST;

  public CatwalkRailingBlock (Properties props) {
    super(props);
    // Changed this to have north face defaulted to true.
    // 4 false values should be considered air
    this.registerDefaultState(this.defaultBlockState()
            .setValue(NORTH_FENCE, true)
            .setValue(SOUTH_FENCE, false)
            .setValue(EAST_FENCE,  false)
            .setValue(WEST_FENCE,  false)
            .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
    BlockPos pos   = context.getClickedPos();
    Vec3 subbox    = context.getClickLocation().subtract(Vec3.atCenterOf(pos));
    Direction face = context.getClickedFace();
    Level level    = context.getLevel();
    Player player  = context.getPlayer();
    var x = subbox.x;
    var z = subbox.z;

    if (level.isClientSide() || face == Direction.DOWN) return InteractionResult.PASS;
    //Fixes block state not properly removing
    if(getSideCount(state) == 1) return IWrenchable.super.onSneakWrenched(state, context);

    //check if the top face is wrenched, remove side
    if (face == Direction.UP) {
      boolean bottomleft = x < -z;
      boolean topleft = x < z;
      var dir = Direction.WEST;
      if (!bottomleft && topleft) dir = Direction.SOUTH;
      if (!bottomleft && !topleft) dir = Direction.EAST;
      if (bottomleft && !topleft) dir = Direction.NORTH;

      //obscure edge case where a corner of the top face cannot be wrenched
      if (state.getValue(fromDirection(dir))) {
        state = state.setValue(fromDirection(dir), false);
        level.setBlock(pos, state, 3);
        IWrenchable.playRemoveSound(level, pos);
        if (player != null && !player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
        return InteractionResult.SUCCESS;
      }
      else return InteractionResult.PASS;
    }

    //check for wrenching the inside faces
    if (x == 0.375 || x == -0.375 || z == 0.375 || z == -0.375) state = state.setValue(fromDirection(face.getOpposite()), false);

    //check for wrenching the outside faces
    if (x == 0.5 || x == -0.5 || z == 0.5 || z == -0.5) {
      if (!state.getValue(fromDirection(face))) {
        if (x >= 0.375) state = state.setValue(EAST_FENCE, false);
        if (x <= -0.375) state = state.setValue(WEST_FENCE, false);
        if (z <= -0.375) state = state.setValue(NORTH_FENCE, false);
        if (z >= 0.375) state = state.setValue(SOUTH_FENCE, false);
      }
      else state = state.setValue(fromDirection(face), false);
    }

    level.setBlock(pos, state, 3);
    IWrenchable.playRemoveSound(level, pos);
    if (player != null && !player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    Direction facing = ctx.getHorizontalDirection();
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
      return defaultBlockState()
              .setValue(NORTH_FENCE, (facing == Direction.NORTH))
              .setValue(SOUTH_FENCE, (facing == Direction.SOUTH))
              .setValue(EAST_FENCE,  (facing == Direction.EAST))
              .setValue(WEST_FENCE,  (facing == Direction.WEST))
              .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(NORTH_FENCE);
    builder.add(SOUTH_FENCE);
    builder.add(EAST_FENCE);
    builder.add(WEST_FENCE);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
    if (targetedFace.getAxis() == Direction.Axis.Y) {
      int state =
              (originalState.getValue(NORTH_FENCE) ? 8 : 0) +
                      (originalState.getValue(EAST_FENCE)  ? 4 : 0) +
                      (originalState.getValue(SOUTH_FENCE) ? 2 : 0) +
                      (originalState.getValue(WEST_FENCE)  ? 1 : 0);
      return originalState
              .setValue(NORTH_FENCE, (state & 1) == 1)
              .setValue(EAST_FENCE,  (state & 8) == 8)
              .setValue(SOUTH_FENCE, (state & 4) == 4)
              .setValue(WEST_FENCE,  (state & 2) == 2);
    }
    return originalState;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return getInteractionShape(state, reader, pos);
  }

  @Override
  public VoxelShape getInteractionShape (BlockState state, BlockGetter world, BlockPos pos) {
    VoxelShape shape = Shapes.empty();
    if (state.getValue(NORTH_FENCE)) shape = Shapes.join(shape, VOXEL_NORTH, BooleanOp.OR);
    if (state.getValue(SOUTH_FENCE)) shape = Shapes.join(shape, VOXEL_SOUTH, BooleanOp.OR);
    if (state.getValue(EAST_FENCE))  shape = Shapes.join(shape, VOXEL_EAST,  BooleanOp.OR);
    if (state.getValue(WEST_FENCE))  shape = Shapes.join(shape, VOXEL_WEST,  BooleanOp.OR);

    return shape;
  }

  @Override
  public boolean canPlaceLiquid(@Nullable LivingEntity placer, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  // used to ensure the block doesn't leave a ghost behind if all 4 sides are gone
  @Override
  protected void neighborChanged (
          BlockState state, Level level, BlockPos pos,
          Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston
  ) {

    if (isEmpty(state)) level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
    super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
  }

  public static boolean isRailing (ItemStack test) {
    return (test.getItem() instanceof BlockItem) && isRailing(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isRailing (Block test) {
    return test instanceof CatwalkRailingBlock;
  }

  public static BooleanProperty fromDirection (Direction face) {
    return switch (face) {
      case SOUTH -> SOUTH_FENCE;
      case EAST  -> EAST_FENCE;
      case WEST  -> WEST_FENCE;
      default -> NORTH_FENCE;
    };
  }

  public int getSideCount(BlockState state) {
    int c = 0;
    if(state.getValue(NORTH_FENCE)) c++;
    if(state.getValue(SOUTH_FENCE)) c++;
    if(state.getValue(WEST_FENCE)) c++;
    if(state.getValue(EAST_FENCE)) c++;
    return c;
  }

  public boolean isEmpty(BlockState state) {
    return getSideCount(state) == 0;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    boolean north = state.getValue(NORTH_FENCE);
    boolean south = state.getValue(SOUTH_FENCE);
    boolean east = state.getValue(EAST_FENCE);
    boolean west = state.getValue(WEST_FENCE);
    switch (rotation){
      case CLOCKWISE_90 -> {
        north = state.getValue(WEST_FENCE);
        south = state.getValue(EAST_FENCE);
        east = state.getValue(NORTH_FENCE);
        west = state.getValue(SOUTH_FENCE);
      }
      case CLOCKWISE_180 -> {
        north = state.getValue(SOUTH_FENCE);
        south = state.getValue(NORTH_FENCE);
        east = state.getValue(WEST_FENCE);
        west = state.getValue(EAST_FENCE);
      }
      case COUNTERCLOCKWISE_90 -> {
        north = state.getValue(EAST_FENCE);
        south = state.getValue(WEST_FENCE);
        east = state.getValue(SOUTH_FENCE);
        west = state.getValue(NORTH_FENCE);
      }
      case NONE -> {
        north = state.getValue(NORTH_FENCE);
        south = state.getValue(SOUTH_FENCE);
        east = state.getValue(EAST_FENCE);
        west = state.getValue(WEST_FENCE);
      }
    }
    return defaultBlockState().setValue(NORTH_FENCE, north).setValue(SOUTH_FENCE, south).setValue(EAST_FENCE, east).setValue(WEST_FENCE, west);
  }
}
