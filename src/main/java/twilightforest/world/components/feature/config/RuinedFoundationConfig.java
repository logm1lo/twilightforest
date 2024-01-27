package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

public record RuinedFoundationConfig(
		IntProvider wallWidth,
		IntProvider wallHeights,
		FloatProvider placeFloorTest,
		BlockStateProvider floor,
		BlockStateProvider basementPosts,
		// Blockstate given is used for the north-facing wall. Rotations will apply on other-facing walls, corners resolving randomly.
		BlockStateProvider wallBlock,
		BlockStateProvider decayedWall,
		BlockStateProvider wallTop,
		BlockStateProvider decayedTop
) implements FeatureConfiguration {
	public static final Codec<RuinedFoundationConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			IntProvider.codec(1, 16).fieldOf("wall_width").forGetter(RuinedFoundationConfig::wallWidth),
			IntProvider.codec(1, 32).fieldOf("wall_heights").forGetter(RuinedFoundationConfig::wallHeights),
			FloatProvider.codec(-8, 8).fieldOf("random_floor_chance").forGetter(RuinedFoundationConfig::placeFloorTest),
			BlockStateProvider.CODEC.fieldOf("floor").forGetter(RuinedFoundationConfig::floor),
			BlockStateProvider.CODEC.fieldOf("basement_posts").forGetter(RuinedFoundationConfig::basementPosts),
			BlockStateProvider.CODEC.fieldOf("wall_block").forGetter(RuinedFoundationConfig::wallBlock),
			BlockStateProvider.CODEC.fieldOf("decayed_wall_block").forGetter(RuinedFoundationConfig::decayedWall),
			BlockStateProvider.CODEC.fieldOf("wall_top_block").forGetter(RuinedFoundationConfig::wallTop),
			BlockStateProvider.CODEC.fieldOf("decayed_wall_top_block").forGetter(RuinedFoundationConfig::decayedTop)
	).apply(inst, RuinedFoundationConfig::new));

	public static RuinedFoundationConfig defaultConfig(boolean floorWaterlogged) {
		return numbersDefault(
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(Blocks.OAK_PLANKS.defaultBlockState(), 39)
						.add(Blocks.OAK_SLAB.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 1)
						.add(Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
						.add(Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
						.add(Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 6)
						.add(Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(BlockStateProperties.WATERLOGGED, floorWaterlogged), 2)
						.build()
				),
				BlockStateProvider.simple(Blocks.OAK_FENCE),
				BlockStateProvider.simple(Blocks.COBBLESTONE),
				BlockStateProvider.simple(Blocks.MOSSY_COBBLESTONE),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(Blocks.COBBLESTONE.defaultBlockState(), 5)
						.add(Blocks.COBBLESTONE_SLAB.defaultBlockState(), 1)
						.add(Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
						.add(Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
						.build()
				),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5)
						.add(Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState(), 1)
						.add(Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2)
						.add(Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2)
						.build()
				)
		);
	}

	public static RuinedFoundationConfig numbersDefault(WeightedStateProvider floor, SimpleStateProvider basementPosts, SimpleStateProvider wallBlock, SimpleStateProvider decayedWall, WeightedStateProvider wallTop, WeightedStateProvider decayedTop) {
		return new RuinedFoundationConfig(
				UniformInt.of(5, 9),
				UniformInt.of(1, 5),
				UniformFloat.of(-2, 1),
				floor, basementPosts, wallBlock, decayedWall, wallTop, decayedTop
		);
	}
}
