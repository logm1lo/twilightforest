package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.loot.TFLootTables;
import twilightforest.util.FeatureLogic;
import twilightforest.util.FeatureUtil;
import twilightforest.world.components.feature.config.RuinedFoundationConfig;

public class FoundationFeature extends Feature<RuinedFoundationConfig> {

	public FoundationFeature(Codec<RuinedFoundationConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<RuinedFoundationConfig> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();
		RuinedFoundationConfig config = ctx.config();

		IntProvider wallWidths = config.wallWidth();
		int xWidth = wallWidths.sample(rand);
		int zWidth = wallWidths.sample(rand);

		if (!FeatureUtil.isAreaSuitable(world, pos.offset(1, 0, 1), xWidth - 1, 4, zWidth - 1)) {
			return false;
		}

		//okay!
		generateFoundation(world, rand, pos, xWidth, zWidth, config.wallHeights(), config.placeFloorTest(), config.wallBlock(), config.wallTop(), config.decayedWall(), config.decayedTop(), config.floor());

		//TODO: chimney?

		// 50% basement chance!
		if (rand.nextInt(2) == 0) {
			BlockPos basementCeilingPos = pos.offset(1, -3, 1);
			generateBasement(xWidth - 2, zWidth - 2, world, basementCeilingPos, rand, config.placeFloorTest(), config.floor(), config.basementPosts());
		}

		return true;
	}

	private static void generateFoundation(WorldGenLevel world, RandomSource rand, BlockPos origin, int xWidth, int zWidth, IntProvider wallHeights, FloatProvider placeFloorTest, BlockStateProvider wallBlock, BlockStateProvider wallTop, BlockStateProvider decayedWall, BlockStateProvider decayedTop, BlockStateProvider floor) {
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				// stone on the edges
				Rotation wallRotation = FeatureLogic.wallVolumeRotation(rand, dX, dZ, xWidth, zWidth);
				if (wallRotation != null) {
					int height = wallHeights.sample(rand);

					for (int yBlock = 0; yBlock < height; yBlock++) {
						BlockPos placeAt = origin.offset(dX, yBlock - 1, dZ);
						setWallBlock(world, rand, wallBlock, decayedWall, yBlock, placeAt, wallRotation);
					}

					setWallBlock(world, rand, wallTop, decayedTop, height, origin.offset(dX, height - 1, dZ), wallRotation);
				} else if (placeFloorTest.sample(rand) <= 0) {
					// destroyed wooden plank floor
					setAndUpdate(world, rand, floor, origin.offset(dX, -1, dZ));
				}
			}
		}
	}

	private static void setWallBlock(WorldGenLevel world, RandomSource rand, BlockStateProvider main, BlockStateProvider decay, int yBlock, BlockPos placeAt, Rotation rotation) {
		setAndUpdate(world, rand, rollDecay(rand, yBlock, main, decay), placeAt, rotation);
	}

	public static BlockStateProvider rollDecay(RandomSource rand, int decayRarity, BlockStateProvider main, BlockStateProvider decay) {
		return rand.nextInt(decayRarity + 1) >= 1 ? main : decay;
	}

	private static void generateBasement(int xWidth, int zWidth, WorldGenLevel world, BlockPos ceilingPos, RandomSource rand, FloatProvider placeFloorTest, BlockStateProvider floor, BlockStateProvider basementPost) {
		if (xWidth < 1 || zWidth < 1) return;

		int chestX = rollChestCoord(xWidth, rand);
		int chestZ = rollChestCoord(zWidth, rand);

		// clear basement
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				world.setBlock(ceilingPos.offset(dX, 0, dZ), Blocks.AIR.defaultBlockState(), 3);
				world.setBlock(ceilingPos.offset(dX, -1, dZ), Blocks.AIR.defaultBlockState(), 3);
				world.setBlock(ceilingPos.offset(dX, -2, dZ), Blocks.AIR.defaultBlockState(), 3);

				int cornerOverlap = 0;
				if (dX == 0) cornerOverlap++;
				if (dZ == 0) cornerOverlap++;
				if (dX == xWidth) cornerOverlap++;
				if (dZ == zWidth) cornerOverlap++;

				if (cornerOverlap > 1) {
					setAndUpdate(world, rand, basementPost, ceilingPos.offset(dX, 0, dZ));
					setAndUpdate(world, rand, basementPost, ceilingPos.offset(dX, -1, dZ));
					setAndUpdate(world, rand, basementPost, ceilingPos.offset(dX, -2, dZ));
				} else if ((dX == chestX && dZ == chestZ) || (cornerOverlap == 0 && placeFloorTest.sample(rand) <= 0)) {
					// destroyed wooden plank floor, placed by chance or because a chest is going to generate above it
					setAndUpdate(world, rand, floor, ceilingPos.offset(dX, -3, dZ));
				}
			}
		}

		// make chest
		TFLootTables.FOUNDATION_BASEMENT.generateChest(world, ceilingPos.offset(chestX, -2, chestZ), Direction.NORTH, false);
	}

	private static int rollChestCoord(int width, RandomSource rand) {
		if (width < 3) // No room to not be on an edge
			return rand.nextInt(Math.max(0, width) + 1);

		return rand.nextInt(Math.max(0, width - 1) + 1) + 1;
	}

	private static void setAndUpdate(WorldGenLevel world, RandomSource rand, BlockStateProvider floor, BlockPos placeAt) {
		setAndUpdate(world, rand, floor, placeAt, Rotation.NONE);
	}

	private static void setAndUpdate(WorldGenLevel world, RandomSource rand, BlockStateProvider floor, BlockPos placeAt, Rotation rotation) {
		BlockState state = floor.getState(rand, placeAt).rotate(rotation);

		if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			boolean hasWaterOrAbove = world.getFluidState(placeAt).is(FluidTags.WATER) || world.getFluidState(placeAt.above()).is(FluidTags.WATER);
			if (hasWaterOrAbove)
				state = state.setValue(BlockStateProperties.WATERLOGGED, true);
		}

		world.setBlock(placeAt, state, 3);

		world.getChunk(placeAt).markPosForPostprocessing(placeAt);
	}
}
