package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import twilightforest.data.TFBlockFamilies;

public record RuinedFoundationConfig(
		IntProvider wallWidth,
		IntProvider wallHeights,
		FloatProvider placeFloorTest,
		BlockStateProvider floor,
		BlockStateProvider basementPosts,
		// Blockstate given is used for the north-facing wall. Rotations will apply on other-facing walls, corners resolving randomly.
		BlockStateProvider wallBlock,
		BlockStateProvider wallTop,
		BlockStateProvider decayedWall,
		BlockStateProvider decayedTop
) implements FeatureConfiguration {
	public static final Codec<RuinedFoundationConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			IntProvider.codec(1, 16).fieldOf("wall_width").forGetter(RuinedFoundationConfig::wallWidth),
			IntProvider.codec(1, 32).fieldOf("wall_heights").forGetter(RuinedFoundationConfig::wallHeights),
			FloatProvider.codec(-8, 8).fieldOf("random_floor_chance").forGetter(RuinedFoundationConfig::placeFloorTest),
			BlockStateProvider.CODEC.fieldOf("floor").forGetter(RuinedFoundationConfig::floor),
			BlockStateProvider.CODEC.fieldOf("basement_posts").forGetter(RuinedFoundationConfig::basementPosts),
			BlockStateProvider.CODEC.fieldOf("wall_block").forGetter(RuinedFoundationConfig::wallBlock),
			BlockStateProvider.CODEC.fieldOf("wall_top_block").forGetter(RuinedFoundationConfig::wallTop),
			BlockStateProvider.CODEC.fieldOf("decayed_wall_block").forGetter(RuinedFoundationConfig::decayedWall),
			BlockStateProvider.CODEC.fieldOf("decayed_wall_top_block").forGetter(RuinedFoundationConfig::decayedTop)
	).apply(inst, RuinedFoundationConfig::new));

	public static RuinedFoundationConfig withDefaultBlocks(boolean floorWaterlogged) {
		if (false) {
			//return withBlockFamilies(floorWaterlogged, BlockFamilies.DEEPSLATE_TILES, BlockFamilies.COBBLED_DEEPSLATE, BlockFamilies.POLISHED_DEEPSLATE);
			//return withBlockFamilies(floorWaterlogged, BlockFamilies.MUD_BRICKS, BlockFamilies.ANDESITE, BlockFamilies.POLISHED_ANDESITE);
			//return withBlockFamilies(floorWaterlogged, BlockFamilies.DEEPSLATE_TILES, BlockFamilies.DIORITE, BlockFamilies.POLISHED_DIORITE);
			//return withBlockFamilies(floorWaterlogged, BlockFamilies.TUFF, BlockFamilies.GRANITE, BlockFamilies.POLISHED_GRANITE);
			//return withBlockFamilies(floorWaterlogged, BlockFamilies.ACACIA_PLANKS, BlockFamilies.PRISMARINE, BlockFamilies.PRISMARINE_BRICKS);
			//return withBlockFamilies(floorWaterlogged, BlockFamilies.SPRUCE_PLANKS, BlockFamilies.COBBLESTONE, BlockFamilies.STONE_BRICK);
			return withBlockFamilies(floorWaterlogged, BlockFamilies.ANDESITE, TFBlockFamilies.TWILIGHT_OAK, TFBlockFamilies.CANOPY);
		}

		return withBlockFamilies(floorWaterlogged, BlockFamilies.OAK_PLANKS, BlockFamilies.COBBLESTONE, BlockFamilies.MOSSY_COBBLESTONE);
	}

	@SuppressWarnings("ConstantValue")
	public static RuinedFoundationConfig withBlockFamilies(boolean floorWaterlogged, BlockFamily floorMaterial, BlockFamily wallMaterial, BlockFamily decayedMaterial) {
		boolean doFence = floorMaterial.get(BlockFamily.Variant.FENCE) != null;

		BlockFamily.Variant basementSupports = doFence ? BlockFamily.Variant.FENCE : BlockFamily.Variant.WALL;
		TFBlockFamilies.verifyFamilyShapes(floorMaterial, BlockFamily.Variant.SLAB, BlockFamily.Variant.STAIRS, basementSupports);

		TFBlockFamilies.verifyFamilyShapes(wallMaterial, BlockFamily.Variant.SLAB, BlockFamily.Variant.STAIRS);
		TFBlockFamilies.verifyFamilyShapes(decayedMaterial, BlockFamily.Variant.SLAB, BlockFamily.Variant.STAIRS);

		BlockState floorStairs = floorMaterial.get(BlockFamily.Variant.STAIRS).defaultBlockState();

		BlockState wallBlock = wallMaterial.getBaseBlock().defaultBlockState();
		BlockState wallStairs = wallMaterial.get(BlockFamily.Variant.STAIRS).defaultBlockState();

		BlockState decayedWall = decayedMaterial.getBaseBlock().defaultBlockState();
		BlockState decayedStairs = decayedMaterial.get(BlockFamily.Variant.STAIRS).defaultBlockState();

		return numbersDefault(
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(floorMaterial.getBaseBlock().defaultBlockState(), 39)
						.add(floorMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 1)
						.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
						.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
						.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
						.add(floorStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
						.build()
				),
				BlockStateProvider.simple(floorMaterial.get(basementSupports).defaultBlockState()),
				BlockStateProvider.simple(wallBlock),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(wallBlock, 5)
						.add(wallMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState(), 1)
						.add(wallStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
						.add(wallStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
						.build()
				),
				BlockStateProvider.simple(decayedWall),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(decayedWall, 5)
						.add(decayedMaterial.get(BlockFamily.Variant.SLAB).defaultBlockState(), 1)
						.add(decayedStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
						.add(decayedStairs.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
						.build()
				)
		);
	}

	public static RuinedFoundationConfig numbersDefault(BlockStateProvider floor, BlockStateProvider basementPosts, BlockStateProvider wallBlock, BlockStateProvider wallTop, BlockStateProvider decayedWall, BlockStateProvider decayedTop) {
		return new RuinedFoundationConfig(
				UniformInt.of(5, 9),
				UniformInt.of(1, 5),
				UniformFloat.of(-2, 1),
				floor, basementPosts, wallBlock, wallTop, decayedWall, decayedTop
		);
	}
}
