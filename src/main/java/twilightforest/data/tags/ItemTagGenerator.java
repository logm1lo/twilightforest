package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ModdedItemTagGenerator {
	public static final TagKey<Item> TWILIGHT_OAK_LOGS = create("twilight_oak_logs");
	public static final TagKey<Item> CANOPY_LOGS = create("canopy_logs");
	public static final TagKey<Item> MANGROVE_LOGS = create("mangrove_logs");
	public static final TagKey<Item> DARKWOOD_LOGS = create("darkwood_logs");
	public static final TagKey<Item> TIME_LOGS = create("timewood_logs");
	public static final TagKey<Item> TRANSFORMATION_LOGS = create("transwood_logs");
	public static final TagKey<Item> MINING_LOGS = create("mining_logs");
	public static final TagKey<Item> SORTING_LOGS = create("sortwood_logs");

	public static final TagKey<Item> TWILIGHT_LOGS = create("logs");

	public static final TagKey<Item> PAPER = makeForgeTag("paper");

	public static final TagKey<Item> TOWERWOOD = create("towerwood");

	public static final TagKey<Item> FIERY_VIAL = create("fiery_vial");

	public static final TagKey<Item> ARCTIC_FUR = create("arctic_fur");
	public static final TagKey<Item> CARMINITE_GEMS = makeForgeTag("gems/carminite");
	public static final TagKey<Item> FIERY_INGOTS = makeForgeTag("ingots/fiery");
	public static final TagKey<Item> IRONWOOD_INGOTS = makeForgeTag("ingots/ironwood");
	public static final TagKey<Item> KNIGHTMETAL_INGOTS = makeForgeTag("ingots/knightmetal");
	public static final TagKey<Item> STEELEAF_INGOTS = makeForgeTag("ingots/steeleaf");

	public static final TagKey<Item> STORAGE_BLOCKS_ARCTIC_FUR = makeForgeTag("storage_blocks/arctic_fur");
	public static final TagKey<Item> STORAGE_BLOCKS_CARMINITE = makeForgeTag("storage_blocks/carminite");
	public static final TagKey<Item> STORAGE_BLOCKS_FIERY = makeForgeTag("storage_blocks/fiery");
	public static final TagKey<Item> STORAGE_BLOCKS_IRONWOOD = makeForgeTag("storage_blocks/ironwood");
	public static final TagKey<Item> STORAGE_BLOCKS_KNIGHTMETAL = makeForgeTag("storage_blocks/knightmetal");
	public static final TagKey<Item> STORAGE_BLOCKS_STEELEAF = makeForgeTag("storage_blocks/steeleaf");

	public static final TagKey<Item> RAW_MATERIALS_IRONWOOD = makeForgeTag("raw_materials/ironwood");
	public static final TagKey<Item> RAW_MATERIALS_KNIGHTMETAL = makeForgeTag("raw_materials/knightmetal");

	public static final TagKey<Item> PORTAL_ACTIVATOR = create("portal/activator");

	public static final TagKey<Item> WIP = create("wip");
	public static final TagKey<Item> NYI = create("nyi");

	public static final TagKey<Item> KOBOLD_PACIFICATION_BREADS = create("kobold_pacification_breads");

	public static final TagKey<Item> BANNED_UNCRAFTING_INGREDIENTS = create("banned_uncrafting_ingredients");
	public static final TagKey<Item> BANNED_UNCRAFTABLES = create("banned_uncraftables");
	public static final TagKey<Item> UNCRAFTING_IGNORES_COST = create("uncrafting_ignores_cost");

	public static final TagKey<Item> KEPT_ON_DEATH = create("kept_on_death");

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.copy(BlockTagGenerator.TWILIGHT_OAK_LOGS, TWILIGHT_OAK_LOGS);
		this.copy(BlockTagGenerator.CANOPY_LOGS, CANOPY_LOGS);
		this.copy(BlockTagGenerator.MANGROVE_LOGS, MANGROVE_LOGS);
		this.copy(BlockTagGenerator.DARKWOOD_LOGS, DARKWOOD_LOGS);
		this.copy(BlockTagGenerator.TIME_LOGS, TIME_LOGS);
		this.copy(BlockTagGenerator.TRANSFORMATION_LOGS, TRANSFORMATION_LOGS);
		this.copy(BlockTagGenerator.MINING_LOGS, MINING_LOGS);
		this.copy(BlockTagGenerator.SORTING_LOGS, SORTING_LOGS);

		this.copy(BlockTagGenerator.TF_LOGS, TWILIGHT_LOGS);
		tag(ItemTags.LOGS).addTag(TWILIGHT_LOGS);
		tag(ItemTags.LOGS_THAT_BURN)
				.addTag(TWILIGHT_OAK_LOGS).addTag(CANOPY_LOGS).addTag(MANGROVE_LOGS)
				.addTag(TIME_LOGS).addTag(TRANSFORMATION_LOGS).addTag(MINING_LOGS).addTag(SORTING_LOGS);

		this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		this.copy(BlockTags.LEAVES, ItemTags.LEAVES);

		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(Tags.Blocks.FENCES, Tags.Items.FENCES);
		this.copy(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);

		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);

		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);

		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		this.copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
		this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);

		this.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);

		this.copy(BlockTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_ARCTIC_FUR);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_CARMINITE);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_FIERY);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_IRONWOOD);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_KNIGHTMETAL);
		this.copy(BlockTagGenerator.STORAGE_BLOCKS_STEELEAF, STORAGE_BLOCKS_STEELEAF);

		tag(Tags.Items.STORAGE_BLOCKS)
				.addTag(STORAGE_BLOCKS_FIERY).addTag(STORAGE_BLOCKS_ARCTIC_FUR)
				.addTag(STORAGE_BLOCKS_CARMINITE).addTag(STORAGE_BLOCKS_IRONWOOD)
				.addTag(STORAGE_BLOCKS_KNIGHTMETAL).addTag(STORAGE_BLOCKS_STEELEAF);

		this.copy(BlockTagGenerator.TOWERWOOD, TOWERWOOD);

		tag(PAPER).add(Items.PAPER);
		tag(Tags.Items.FEATHERS).add(Items.FEATHER).add(TFItems.RAVEN_FEATHER.value());

		tag(FIERY_VIAL).add(TFItems.FIERY_BLOOD.value(), TFItems.FIERY_TEARS.value());

		tag(ARCTIC_FUR).add(TFItems.ARCTIC_FUR.value());
		tag(CARMINITE_GEMS).add(TFItems.CARMINITE.value());
		tag(FIERY_INGOTS).add(TFItems.FIERY_INGOT.value());
		tag(IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.value());
		tag(KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.value());
		tag(STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.value());

		tag(Tags.Items.GEMS).addTag(CARMINITE_GEMS);

		tag(Tags.Items.INGOTS)
				.addTag(IRONWOOD_INGOTS).addTag(FIERY_INGOTS)
				.addTag(KNIGHTMETAL_INGOTS).addTag(STEELEAF_INGOTS);

		tag(RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.value());
		tag(RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.value());
		tag(Tags.Items.RAW_MATERIALS).addTag(RAW_MATERIALS_IRONWOOD).addTag(RAW_MATERIALS_KNIGHTMETAL);

		tag(PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);
		
		tag(ItemTags.BOATS).add(
				TFItems.TWILIGHT_OAK_BOAT.value(), TFItems.CANOPY_BOAT.value(),
				TFItems.MANGROVE_BOAT.value(), TFItems.DARK_BOAT.value(),
				TFItems.TIME_BOAT.value(), TFItems.TRANSFORMATION_BOAT.value(),
				TFItems.MINING_BOAT.value(), TFItems.SORTING_BOAT.value()
		);

		tag(ItemTags.CHEST_BOATS).add(
				TFItems.TWILIGHT_OAK_CHEST_BOAT.value(), TFItems.CANOPY_CHEST_BOAT.value(),
				TFItems.MANGROVE_CHEST_BOAT.value(), TFItems.DARK_CHEST_BOAT.value(),
				TFItems.TIME_CHEST_BOAT.value(), TFItems.TRANSFORMATION_CHEST_BOAT.value(),
				TFItems.MINING_CHEST_BOAT.value(), TFItems.SORTING_CHEST_BOAT.value()
		);

		tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
				TFItems.FIERY_HELMET.value(),
				TFItems.FIERY_CHESTPLATE.value(),
				TFItems.FIERY_LEGGINGS.value(),
				TFItems.FIERY_BOOTS.value(),
				TFItems.ARCTIC_HELMET.value(),
				TFItems.ARCTIC_CHESTPLATE.value(),
				TFItems.ARCTIC_LEGGINGS.value(),
				TFItems.ARCTIC_BOOTS.value(),
				TFItems.YETI_HELMET.value(),
				TFItems.YETI_CHESTPLATE.value(),
				TFItems.YETI_LEGGINGS.value(),
				TFItems.YETI_BOOTS.value()
		);

		tag(WIP).add(
				TFItems.CUBE_OF_ANNIHILATION.value()
		);

		tag(NYI).add(
				TFBlocks.CINDER_FURNACE.value().asItem(),
				TFBlocks.CINDER_LOG.value().asItem(),
				TFBlocks.CINDER_WOOD.value().asItem(),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.value().asItem(),
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.value().asItem(),
				TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.value().asItem(),
				TFBlocks.AURORALIZED_GLASS.value().asItem(),
				TFBlocks.SLIDER.value().asItem(),
				TFItems.ORE_METER.value(),
				TFItems.MAGIC_PAINTING.value(),
				TFItems.POCKET_WATCH.value()
		);

		tag(KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);

		tag(ItemTags.MUSIC_DISCS).add(
				TFItems.MUSIC_DISC_FINDINGS.value(),
				TFItems.MUSIC_DISC_HOME.value(),
				TFItems.MUSIC_DISC_MAKER.value(),
				TFItems.MUSIC_DISC_MOTION.value(),
				TFItems.MUSIC_DISC_RADIANCE.value(),
				TFItems.MUSIC_DISC_STEPS.value(),
				TFItems.MUSIC_DISC_SUPERSTITIOUS.value(),
				TFItems.MUSIC_DISC_THREAD.value(),
				TFItems.MUSIC_DISC_WAYFARER.value());

		tag(BANNED_UNCRAFTING_INGREDIENTS).add(
				TFBlocks.INFESTED_TOWERWOOD.value().asItem(),
				TFBlocks.HOLLOW_OAK_SAPLING.value().asItem(),
				TFBlocks.TIME_SAPLING.value().asItem(),
				TFBlocks.TRANSFORMATION_SAPLING.value().asItem(),
				TFBlocks.MINING_SAPLING.value().asItem(),
				TFBlocks.SORTING_SAPLING.value().asItem(),
				TFItems.TRANSFORMATION_POWDER.value());

		tag(BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.value().asItem());
		tag(UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);

		tag(KEPT_ON_DEATH).add(TFItems.TOWER_KEY.value(), TFItems.PHANTOM_HELMET.value(), TFItems.PHANTOM_CHESTPLATE.value());

		tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.value(), TFItems.CHARM_OF_KEEPING_3.value(), TFItems.CHARM_OF_LIFE_2.value(), TFItems.LAMP_OF_CINDERS.value());

		tag(Tags.Items.HEADS).add(
				TFItems.ZOMBIE_SKULL_CANDLE.value(),
				TFItems.SKELETON_SKULL_CANDLE.value(),
				TFItems.WITHER_SKELETON_SKULL_CANDLE.value(),
				TFItems.CREEPER_SKULL_CANDLE.value(),
				TFItems.PLAYER_SKULL_CANDLE.value(),
				TFItems.PIGLIN_SKULL_CANDLE.value());

		tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
				TFItems.ZOMBIE_SKULL_CANDLE.value(),
				TFItems.SKELETON_SKULL_CANDLE.value(),
				TFItems.WITHER_SKELETON_SKULL_CANDLE.value(),
				TFItems.CREEPER_SKULL_CANDLE.value(),
				TFItems.PLAYER_SKULL_CANDLE.value(),
				TFItems.PIGLIN_SKULL_CANDLE.value());

		tag(Tags.Items.ARMORS_HELMETS).add(
				TFItems.IRONWOOD_HELMET.value(),
				TFItems.STEELEAF_HELMET.value(),
				TFItems.KNIGHTMETAL_HELMET.value(),
				TFItems.PHANTOM_HELMET.value(),
				TFItems.FIERY_HELMET.value(),
				TFItems.ARCTIC_HELMET.value(),
				TFItems.YETI_HELMET.value());
		
		tag(Tags.Items.ARMORS_CHESTPLATES).add(
				TFItems.NAGA_CHESTPLATE.value(),
				TFItems.IRONWOOD_CHESTPLATE.value(),
				TFItems.STEELEAF_CHESTPLATE.value(),
				TFItems.KNIGHTMETAL_CHESTPLATE.value(),
				TFItems.PHANTOM_CHESTPLATE.value(),
				TFItems.FIERY_CHESTPLATE.value(),
				TFItems.ARCTIC_CHESTPLATE.value(),
				TFItems.YETI_CHESTPLATE.value());
		
		tag(Tags.Items.ARMORS_LEGGINGS).add(
				TFItems.NAGA_LEGGINGS.value(),
				TFItems.IRONWOOD_LEGGINGS.value(),
				TFItems.STEELEAF_LEGGINGS.value(),
				TFItems.KNIGHTMETAL_LEGGINGS.value(),
				TFItems.FIERY_LEGGINGS.value(),
				TFItems.ARCTIC_LEGGINGS.value(),
				TFItems.YETI_LEGGINGS.value());
		
		tag(Tags.Items.ARMORS_BOOTS).add(
				TFItems.IRONWOOD_BOOTS.value(),
				TFItems.STEELEAF_BOOTS.value(),
				TFItems.KNIGHTMETAL_BOOTS.value(),
				TFItems.FIERY_BOOTS.value(),
				TFItems.ARCTIC_BOOTS.value(),
				TFItems.YETI_BOOTS.value());
		
		tag(ItemTags.SWORDS).add(
				TFItems.IRONWOOD_SWORD.value(),
				TFItems.STEELEAF_SWORD.value(),
				TFItems.KNIGHTMETAL_SWORD.value(),
				TFItems.FIERY_SWORD.value(),
				TFItems.GIANT_SWORD.value(),
				TFItems.ICE_SWORD.value(),
				TFItems.GLASS_SWORD.value());

		tag(ItemTags.PICKAXES).add(
				TFItems.IRONWOOD_PICKAXE.value(),
				TFItems.STEELEAF_PICKAXE.value(),
				TFItems.KNIGHTMETAL_PICKAXE.value(),
				TFItems.MAZEBREAKER_PICKAXE.value(),
				TFItems.FIERY_PICKAXE.value(),
				TFItems.GIANT_PICKAXE.value());

		tag(ItemTags.AXES).add(TFItems.IRONWOOD_AXE.value(), TFItems.STEELEAF_AXE.value(), TFItems.KNIGHTMETAL_AXE.value());
		tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.value(), TFItems.STEELEAF_SHOVEL.value());
		tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.value(), TFItems.STEELEAF_HOE.value());
		tag(Tags.Items.TOOLS_SHIELDS).add(TFItems.KNIGHTMETAL_SHIELD.value());
		tag(Tags.Items.TOOLS_BOWS).add(TFItems.TRIPLE_BOW.value(), TFItems.SEEKER_BOW.value(), TFItems.ICE_BOW.value(), TFItems.ENDER_BOW.value());

		tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
				TFItems.IRONWOOD_PICKAXE.value(),
				TFItems.STEELEAF_PICKAXE.value(),
				TFItems.KNIGHTMETAL_PICKAXE.value(),
				TFItems.MAZEBREAKER_PICKAXE.value(),
				TFItems.FIERY_PICKAXE.value(),
				TFItems.GIANT_PICKAXE.value());

		tag(ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.value().asItem());

		tag(ItemTags.TRIMMABLE_ARMOR)
				.add(TFItems.IRONWOOD_HELMET.value(), TFItems.IRONWOOD_CHESTPLATE.value(), TFItems.IRONWOOD_LEGGINGS.value(), TFItems.IRONWOOD_BOOTS.value())
				.add(TFItems.STEELEAF_HELMET.value(), TFItems.STEELEAF_CHESTPLATE.value(), TFItems.STEELEAF_LEGGINGS.value(), TFItems.STEELEAF_BOOTS.value())
				.add(TFItems.KNIGHTMETAL_HELMET.value(), TFItems.KNIGHTMETAL_CHESTPLATE.value(), TFItems.KNIGHTMETAL_LEGGINGS.value(), TFItems.KNIGHTMETAL_BOOTS.value())
				.add(TFItems.ARCTIC_HELMET.value(), TFItems.ARCTIC_CHESTPLATE.value(), TFItems.ARCTIC_LEGGINGS.value(), TFItems.ARCTIC_BOOTS.value())
				//due to yeti helmets being bigger than normal helmets trims won't work properly on it. If we ever decide to change the model we can add trim compatibility
				.add(/*TFItems.YETI_HELMET.value(),*/ TFItems.YETI_CHESTPLATE.value(), TFItems.YETI_LEGGINGS.value(), TFItems.YETI_BOOTS.value())
				.add(TFItems.FIERY_HELMET.value(), TFItems.FIERY_CHESTPLATE.value(), TFItems.FIERY_LEGGINGS.value(), TFItems.FIERY_BOOTS.value())
				.add(TFItems.PHANTOM_HELMET.value(), TFItems.PHANTOM_CHESTPLATE.value(), TFItems.NAGA_CHESTPLATE.value(), TFItems.NAGA_LEGGINGS.value());

		tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.value(), TFItems.STEELEAF_INGOT.value(), TFItems.KNIGHTMETAL_INGOT.value(), TFItems.NAGA_SCALE.value(), TFItems.CARMINITE.value(), TFItems.FIERY_INGOT.value());
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}

	public static TagKey<Item> create(String tagName) {
		return ItemTags.create(TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Item> makeForgeTag(String tagName) {
		return ItemTags.create(new ResourceLocation("forge", tagName));
	}
}
