package twilightforest.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.ParrotImitation;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.*;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
	public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void gather() {
		var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
		compostables.add(TFBlocks.FALLEN_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0), false);
		compostables.add(TFBlocks.CANOPY_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.CLOVER_PATCH.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.DARK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.FIDDLEHEAD.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.HEDGE.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MANGROVE_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MAYAPPLE.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MINING_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TWILIGHT_OAK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.RAINBOW_OAK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.ROOT_STRAND.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.SORTING_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TIME_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TRANSFORMATION_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TWILIGHT_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.CANOPY_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MANGROVE_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.DARKWOOD_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.RAINBOW_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFItems.TORCHBERRIES, new Compostable(0.3F), false);
		compostables.add(TFBlocks.BEANSTALK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.MOSS_PATCH.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.ROOT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TROLLVIDR.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.HOLLOW_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TIME_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TRANSFORMATION_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.MINING_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.SORTING_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TORCHBERRY_PLANT.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFItems.LIVEROOT, new Compostable(0.5F), false);
		compostables.add(TFBlocks.HUGE_MUSHGLOOM_STEM.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_WATER_LILY.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.LIVEROOT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.UBEROUS_SOIL.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_STALK.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.UNRIPE_TROLLBER.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.TROLLBER.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFItems.MAZE_WAFER, new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_LILY_PAD.asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
		compostables.add(TFBlocks.HUGE_MUSHGLOOM.asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
		compostables.add(TFItems.EXPERIMENT_115, new Compostable(0.85F), false);
		compostables.add(TFItems.MAGIC_BEANS, new Compostable(0.85F), false);

		var fuels = this.builder(NeoForgeDataMaps.FURNACE_FUELS);
		fuels.add(ItemTagGenerator.BANISTERS, new FurnaceFuel(300), false);
		
		var parrot = this.builder(NeoForgeDataMaps.PARROT_IMITATIONS);
		parrot.add(TFEntities.ALPHA_YETI, new ParrotImitation(TFSounds.ALPHA_YETI_PARROT.get()), false);
		parrot.add(TFEntities.BLOCKCHAIN_GOBLIN, new ParrotImitation(TFSounds.REDCAP_PARROT.get()), false);
		parrot.add(TFEntities.CARMINITE_BROODLING, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.CARMINITE_GOLEM, new ParrotImitation(TFSounds.CARMINITE_GOLEM_PARROT.get()), false);
		parrot.add(TFEntities.FIRE_BEETLE, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.CARMINITE_GHASTLING, new ParrotImitation(SoundEvents.PARROT_IMITATE_GHAST), false);
		parrot.add(TFEntities.CARMINITE_GHASTGUARD, new ParrotImitation(SoundEvents.PARROT_IMITATE_GHAST), false);
		parrot.add(TFEntities.HEDGE_SPIDER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.HELMET_CRAB, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.HOSTILE_WOLF, new ParrotImitation(TFSounds.HOSTILE_WOLF_PARROT.get()), false);
		parrot.add(TFEntities.HYDRA, new ParrotImitation(TFSounds.HYDRA_PARROT.get()), false);
		parrot.add(TFEntities.STABLE_ICE_CORE, new ParrotImitation(TFSounds.ICE_CORE_PARROT.get()), false);
		parrot.add(TFEntities.KING_SPIDER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.KOBOLD, new ParrotImitation(TFSounds.KOBOLD_PARROT.get()), false);
		parrot.add(TFEntities.LICH, new ParrotImitation(SoundEvents.PARROT_IMITATE_BLAZE), false);
		parrot.add(TFEntities.MAZE_SLIME, new ParrotImitation(SoundEvents.PARROT_IMITATE_SLIME), false);
		parrot.add(TFEntities.LICH_MINION, new ParrotImitation(SoundEvents.PARROT_IMITATE_ZOMBIE), false);
		parrot.add(TFEntities.MINOSHROOM, new ParrotImitation(TFSounds.MINOTAUR_PARROT.get()), false);
		parrot.add(TFEntities.MINOTAUR, new ParrotImitation(TFSounds.MINOTAUR_PARROT.get()), false);
		parrot.add(TFEntities.MIST_WOLF, new ParrotImitation(TFSounds.HOSTILE_WOLF_PARROT.get()), false);
		parrot.add(TFEntities.MOSQUITO_SWARM, new ParrotImitation(TFSounds.MOSQUITO_PARROT.get()), false);
		parrot.add(TFEntities.NAGA, new ParrotImitation(TFSounds.NAGA_PARROT.get()), false);
		parrot.add(TFEntities.KNIGHT_PHANTOM, new ParrotImitation(TFSounds.WRAITH_PARROT.get()), false);
		parrot.add(TFEntities.PINCH_BEETLE, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.REDCAP, new ParrotImitation(TFSounds.REDCAP_PARROT.get()), false);
		parrot.add(TFEntities.REDCAP_SAPPER, new ParrotImitation(TFSounds.REDCAP_PARROT.get()), false);
		parrot.add(TFEntities.SKELETON_DRUID, new ParrotImitation(SoundEvents.PARROT_IMITATE_SKELETON), false);
		parrot.add(TFEntities.SLIME_BEETLE, new ParrotImitation(SoundEvents.PARROT_IMITATE_SLIME), false);
		parrot.add(TFEntities.SNOW_GUARDIAN, new ParrotImitation(TFSounds.ICE_CORE_PARROT.get()), false);
		parrot.add(TFEntities.SNOW_QUEEN, new ParrotImitation(TFSounds.ICE_CORE_PARROT.get()), false);
		parrot.add(TFEntities.SWARM_SPIDER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.TOWERWOOD_BORER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SILVERFISH), false);
		parrot.add(TFEntities.DEATH_TOME, new ParrotImitation(TFSounds.DEATH_TOME_PARROT.get()), false);
		parrot.add(TFEntities.UR_GHAST, new ParrotImitation(SoundEvents.PARROT_IMITATE_GHAST), false);
		parrot.add(TFEntities.WINTER_WOLF, new ParrotImitation(TFSounds.HOSTILE_WOLF_PARROT.get()), false);
		parrot.add(TFEntities.WRAITH, new ParrotImitation(TFSounds.WRAITH_PARROT.get()), false);
		parrot.add(TFEntities.YETI, new ParrotImitation(TFSounds.ALPHA_YETI_PARROT.get()), false);

		var transformation = this.builder(TFDataMaps.TRANSFORMATION_POWDER);
		this.add2WayTransform(transformation, TFEntities.MINOTAUR, EntityType.ZOMBIFIED_PIGLIN);
		this.add2WayTransform(transformation, TFEntities.DEER, EntityType.COW);
		this.add2WayTransform(transformation, TFEntities.BOAR, EntityType.PIG);
		this.add2WayTransform(transformation, TFEntities.BIGHORN_SHEEP, EntityType.SHEEP);
		this.add2WayTransform(transformation, TFEntities.DWARF_RABBIT, EntityType.RABBIT);
		this.add2WayTransform(transformation, TFEntities.TINY_BIRD, EntityType.PARROT);
		this.add2WayTransform(transformation, TFEntities.RAVEN, EntityType.BAT);
		this.add2WayTransform(transformation, TFEntities.HOSTILE_WOLF, EntityType.WOLF);
		this.add2WayTransform(transformation, TFEntities.PENGUIN, EntityType.CHICKEN);
		this.add2WayTransform(transformation, TFEntities.HEDGE_SPIDER, EntityType.SPIDER);
		this.add2WayTransform(transformation, TFEntities.SWARM_SPIDER, EntityType.CAVE_SPIDER);
		this.add2WayTransform(transformation, TFEntities.WRAITH, EntityType.VEX);
		this.add2WayTransform(transformation, TFEntities.SKELETON_DRUID, EntityType.WITCH);
		this.add2WayTransform(transformation, TFEntities.CARMINITE_GHASTGUARD, EntityType.GHAST);
		this.add2WayTransform(transformation, TFEntities.TOWERWOOD_BORER, EntityType.SILVERFISH);
		this.add2WayTransform(transformation, TFEntities.MAZE_SLIME, EntityType.SLIME);

		var crumble = this.builder(TFDataMaps.CRUMBLE_HORN);
		crumble.add(Blocks.STONE_BRICKS.builtInRegistryHolder(), Blocks.CRACKED_STONE_BRICKS, false);
		crumble.add(Blocks.INFESTED_STONE_BRICKS.builtInRegistryHolder(), Blocks.INFESTED_CRACKED_STONE_BRICKS, false);
		crumble.add(Blocks.POLISHED_BLACKSTONE_BRICKS.builtInRegistryHolder(), Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, false);
		crumble.add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.builtInRegistryHolder(), Blocks.BLACKSTONE, false);
		crumble.add(Blocks.NETHER_BRICKS.builtInRegistryHolder(), Blocks.CRACKED_NETHER_BRICKS, false);
		crumble.add(Blocks.DEEPSLATE_BRICKS.builtInRegistryHolder(), Blocks.CRACKED_DEEPSLATE_BRICKS, false);
		crumble.add(Blocks.DEEPSLATE_TILES.builtInRegistryHolder(), Blocks.CRACKED_DEEPSLATE_TILES, false);
		crumble.add(TFBlocks.MAZESTONE_BRICK, TFBlocks.CRACKED_MAZESTONE.get(), false);
		crumble.add(TFBlocks.UNDERBRICK, TFBlocks.CRACKED_UNDERBRICK.get(), false);
		crumble.add(TFBlocks.DEADROCK, TFBlocks.CRACKED_DEADROCK.get(), false);
		crumble.add(TFBlocks.CRACKED_DEADROCK, TFBlocks.WEATHERED_DEADROCK.get(), false);
		crumble.add(TFBlocks.TOWERWOOD, TFBlocks.CRACKED_TOWERWOOD.get(), false);
		crumble.add(TFBlocks.CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK.get(), false);
		crumble.add(TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK.get(), false);
		crumble.add(TFBlocks.NAGASTONE_PILLAR, TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), false);
		crumble.add(TFBlocks.ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), false);
		crumble.add(TFBlocks.CASTLE_BRICK_STAIRS, TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), false);
		crumble.add(TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), false);
		crumble.add(TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), false);
		crumble.add(Blocks.STONE.builtInRegistryHolder(), Blocks.COBBLESTONE, false);
		crumble.add(Blocks.COBBLESTONE.builtInRegistryHolder(), Blocks.GRAVEL, false);
		crumble.add(Blocks.SANDSTONE.builtInRegistryHolder(), Blocks.SAND, false);
		crumble.add(Blocks.RED_SANDSTONE.builtInRegistryHolder(), Blocks.RED_SAND, false);
		crumble.add(Blocks.GRASS_BLOCK.builtInRegistryHolder(), Blocks.DIRT, false);
		crumble.add(Blocks.PODZOL.builtInRegistryHolder(), Blocks.DIRT, false);
		crumble.add(Blocks.MYCELIUM.builtInRegistryHolder(), Blocks.DIRT, false);
		crumble.add(Blocks.COARSE_DIRT.builtInRegistryHolder(), Blocks.DIRT, false);
		crumble.add(Blocks.ROOTED_DIRT.builtInRegistryHolder(), Blocks.DIRT, false);
		crumble.add(Blocks.OXIDIZED_COPPER.builtInRegistryHolder(), Blocks.WEATHERED_COPPER, false);
		crumble.add(Blocks.WEATHERED_COPPER.builtInRegistryHolder(), Blocks.EXPOSED_COPPER, false);
		crumble.add(Blocks.EXPOSED_COPPER.builtInRegistryHolder(), Blocks.COPPER_BLOCK, false);
		crumble.add(Blocks.OXIDIZED_CUT_COPPER.builtInRegistryHolder(), Blocks.WEATHERED_CUT_COPPER, false);
		crumble.add(Blocks.WEATHERED_CUT_COPPER.builtInRegistryHolder(), Blocks.EXPOSED_CUT_COPPER, false);
		crumble.add(Blocks.EXPOSED_CUT_COPPER.builtInRegistryHolder(), Blocks.CUT_COPPER, false);
		crumble.add(Blocks.GRAVEL.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.DIRT.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.SAND.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.RED_SAND.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.CLAY.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.ANDESITE.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.DIORITE.builtInRegistryHolder(), Blocks.AIR, false);
		crumble.add(Blocks.GRANITE.builtInRegistryHolder(), Blocks.AIR, false);
	}

	private void add2WayTransform(DataMapProvider.Builder<EntityType<?>, EntityType<?>> builder, Holder<EntityType<?>> tfMob, EntityType<?> vanillaMob) {
		builder.add(tfMob, vanillaMob, false);
		builder.add(BuiltInRegistries.ENTITY_TYPE.getKey(vanillaMob), tfMob.value(), false);
	}
}
