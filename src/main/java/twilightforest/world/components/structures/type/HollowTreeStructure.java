package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.structure.*;
import twilightforest.block.CritterBlock;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.structures.hollowtree.HollowTreeTrunk;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HollowTreeStructure extends LandmarkStructure {
	public static final Codec<HollowTreeStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, HollowTreeStructure::new));


	public HollowTreeStructure(DecorationConfig decorationConfig, StructureSettings settings) {
		super(decorationConfig, settings);
	}

	@Override
	protected StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		// TODO codec
		UniformInt heightRandom = UniformInt.of(32, 95);
		UniformInt radiusRandom = UniformInt.of(1, 4);
		BlockStateProvider log = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.value());
		BlockStateProvider wood = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());
		BlockStateProvider root = BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.value().defaultBlockState());
		BlockStateProvider leaves = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.value().defaultBlockState());
		BlockStateProvider vine = BlockStateProvider.simple(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true));
		BlockStateProvider bug = new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(TFBlocks.FIREFLY.value().defaultBlockState().setValue(CritterBlock.FACING, Direction.NORTH)).add(TFBlocks.CICADA.value().defaultBlockState().setValue(CritterBlock.FACING, Direction.NORTH)));
		BlockStateProvider dungeonWood = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());
		BlockStateProvider dungeonAir = BlockStateProvider.simple(Blocks.AIR.defaultBlockState());
		BlockStateProvider dungeonLootBlock = BlockStateProvider.simple(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
		ResourceLocation dungeonLootTable = TFLootTables.TREE_CACHE.lootTable;
		Holder<EntityType<?>> dungeonMonster = TFEntities.SWARM_SPIDER;

		int height = heightRandom.sample(random);
		int radius = radiusRandom.sample(random);

		BoundingBox boundingBox = new BoundingBox(x, y, z, (x + radius * 2) + 2, y + height, (z + radius * 2) + 2);

		return new HollowTreeTrunk(height, radius, boundingBox, log, wood, root, leaves, vine, bug, dungeonWood, dungeonAir, dungeonLootBlock, dungeonLootTable, dungeonMonster);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.HOLLOW_TREE.get();
	}

	public static HollowTreeStructure buildStructureConfig(BootstapContext<Structure> context) {
		return new HollowTreeStructure(
				new DecorationClearance.DecorationConfig(2, false, true, true),
				new Structure.StructureSettings(
						context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_TREE_BIOMES),
						Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
						GenerationStep.Decoration.SURFACE_STRUCTURES,
						TerrainAdjustment.NONE
				)
		);
	}
}
