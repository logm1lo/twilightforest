package twilightforest.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class ModdedItemTagGenerator extends ItemTagsProvider {
	public static final TagKey<Item> AC_FERNS = createTagFor("alexscaves", "ferns");
	public static final TagKey<Item> AC_FERROMAGNETIC_ITEMS = createTagFor("alexscaves", "ferromagnetic_items");
	public static final TagKey<Item> AC_RAW_MEATS = createTagFor("alexscaves", "raw_meats");

	public static final TagKey<Item> CURIOS_CHARM = createTagFor("curios", "charm");
	public static final TagKey<Item> CURIOS_HEAD = createTagFor("curios", "head");

	public static final TagKey<Item> CA_PLANTS = createTagFor("createaddition", "plants");
	public static final TagKey<Item> CA_PLANT_FOODS = createTagFor("createaddition", "plant_foods");

	public static final TagKey<Item> FD_CABBAGE_ROLL_INGREDIENTS = createTagFor("farmersdelight", "cabbage_roll_ingredients");

	public static final TagKey<Item> RANDOMIUM_BLACKLIST = createTagFor("randomium", "blacklist");

	public ModdedItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, TwilightForestMod.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(AC_FERNS).add(TFBlocks.FIDDLEHEAD.value().asItem());
		tag(AC_FERROMAGNETIC_ITEMS)
				.addTag(ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD)
				.addTag(ItemTagGenerator.STORAGE_BLOCKS_STEELEAF)
				.addTag(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL)
				.add(TFBlocks.CANDELABRA.value().asItem(), TFBlocks.WROUGHT_IRON_FENCE.value().asItem())
				.add(TFItems.RAW_IRONWOOD.value(), TFItems.IRONWOOD_INGOT.value(), TFItems.STEELEAF_INGOT.value(),
						TFItems.ARMOR_SHARD.value(), TFItems.ARMOR_SHARD_CLUSTER.value(), TFItems.KNIGHTMETAL_INGOT.value(), TFItems.KNIGHTMETAL_RING.value(),
						TFItems.FIERY_INGOT.value(), TFItems.CHARM_OF_KEEPING_2.value(), TFItems.ORE_MAGNET.value(),
						TFItems.IRONWOOD_HELMET.value(), TFItems.IRONWOOD_CHESTPLATE.value(), TFItems.IRONWOOD_LEGGINGS.value(), TFItems.IRONWOOD_BOOTS.value(),
						TFItems.STEELEAF_HELMET.value(), TFItems.STEELEAF_CHESTPLATE.value(), TFItems.STEELEAF_LEGGINGS.value(), TFItems.STEELEAF_BOOTS.value(),
						TFItems.KNIGHTMETAL_HELMET.value(), TFItems.KNIGHTMETAL_CHESTPLATE.value(), TFItems.KNIGHTMETAL_LEGGINGS.value(), TFItems.KNIGHTMETAL_BOOTS.value(),
						TFItems.FIERY_HELMET.value(), TFItems.FIERY_CHESTPLATE.value(), TFItems.FIERY_LEGGINGS.value(), TFItems.FIERY_BOOTS.value(),
						TFItems.IRONWOOD_SWORD.value(), TFItems.IRONWOOD_PICKAXE.value(), TFItems.IRONWOOD_AXE.value(), TFItems.IRONWOOD_SHOVEL.value(), TFItems.IRONWOOD_HOE.value(),
						TFItems.STEELEAF_SWORD.value(), TFItems.STEELEAF_PICKAXE.value(), TFItems.STEELEAF_AXE.value(), TFItems.STEELEAF_SHOVEL.value(), TFItems.STEELEAF_HOE.value(),
						TFItems.KNIGHTMETAL_SWORD.value(), TFItems.KNIGHTMETAL_PICKAXE.value(), TFItems.KNIGHTMETAL_AXE.value(), TFItems.BLOCK_AND_CHAIN.value(), TFItems.KNIGHTMETAL_SHIELD.value(),
						TFItems.FIERY_SWORD.value(), TFItems.FIERY_PICKAXE.value(), TFItems.MAZEBREAKER_PICKAXE.value());

		tag(AC_RAW_MEATS).add(TFItems.RAW_VENISON.value(), TFItems.RAW_MEEF.value());

		tag(CURIOS_CHARM).add(
				TFItems.CHARM_OF_LIFE_1.value(), TFItems.CHARM_OF_LIFE_2.value(),
				TFItems.CHARM_OF_KEEPING_1.value(), TFItems.CHARM_OF_KEEPING_2.value(), TFItems.CHARM_OF_KEEPING_3.value()
		);

		tag(CURIOS_HEAD).add(
				TFItems.NAGA_TROPHY.value(),
				TFItems.LICH_TROPHY.value(),
				TFItems.MINOSHROOM_TROPHY.value(),
				TFItems.HYDRA_TROPHY.value(),
				TFItems.KNIGHT_PHANTOM_TROPHY.value(),
				TFItems.UR_GHAST_TROPHY.value(),
				TFItems.ALPHA_YETI_TROPHY.value(),
				TFItems.SNOW_QUEEN_TROPHY.value(),
				TFItems.QUEST_RAM_TROPHY.value(),
				TFItems.CICADA.value(),
				TFItems.FIREFLY.value(),
				TFItems.MOONWORM.value(),
				TFItems.CREEPER_SKULL_CANDLE.value(),
				TFItems.PIGLIN_SKULL_CANDLE.value(),
				TFItems.PLAYER_SKULL_CANDLE.value(),
				TFItems.SKELETON_SKULL_CANDLE.value(),
				TFItems.WITHER_SKELETON_SKULL_CANDLE.value(),
				TFItems.ZOMBIE_SKULL_CANDLE.value());

		tag(CA_PLANT_FOODS).add(TFItems.TORCHBERRIES.value());

		tag(CA_PLANTS).add(TFItems.LIVEROOT.value(), TFItems.MAGIC_BEANS.value(),
				TFBlocks.HUGE_WATER_LILY.value().asItem(), TFBlocks.HUGE_LILY_PAD.value().asItem(),
				TFBlocks.TROLLVIDR.value().asItem(), TFBlocks.UNRIPE_TROLLBER.value().asItem(),
				TFBlocks.TROLLBER.value().asItem(), TFBlocks.HUGE_STALK.value().asItem(),
				TFBlocks.THORN_ROSE.value().asItem(), TFBlocks.MAYAPPLE.value().asItem(),
				TFBlocks.CLOVER_PATCH.value().asItem(), TFBlocks.FIDDLEHEAD.value().asItem(),
				TFBlocks.MUSHGLOOM.value().asItem(), TFBlocks.TORCHBERRY_PLANT.value().asItem(),
				TFBlocks.ROOT_STRAND.value().asItem(), TFBlocks.FALLEN_LEAVES.value().asItem(),
				TFBlocks.HEDGE.value().asItem(), TFBlocks.ROOT_BLOCK.value().asItem(), TFBlocks.LIVEROOT_BLOCK.value().asItem());

		tag(FD_CABBAGE_ROLL_INGREDIENTS).add(TFItems.RAW_VENISON.value(), TFItems.RAW_MEEF.value());

		tag(RANDOMIUM_BLACKLIST).addTag(ItemTagGenerator.NYI).addTag(ItemTagGenerator.WIP).add(TFItems.GLASS_SWORD.value(), //this one is here because the ore can give the unbreakable one
				TFBlocks.TIME_LOG_CORE.value().asItem(), TFBlocks.TRANSFORMATION_LOG_CORE.value().asItem(),
				TFBlocks.MINING_LOG_CORE.value().asItem(), TFBlocks.SORTING_LOG_CORE.value().asItem(),
				TFBlocks.ANTIBUILDER.value().asItem(), TFBlocks.STRONGHOLD_SHIELD.value().asItem(),
				TFBlocks.LOCKED_VANISHING_BLOCK.value().asItem(), TFBlocks.BROWN_THORNS.value().asItem(),
				TFBlocks.GREEN_THORNS.value().asItem(), TFBlocks.BURNT_THORNS.value().asItem(),
				TFBlocks.PINK_FORCE_FIELD.value().asItem(), TFBlocks.ORANGE_FORCE_FIELD.value().asItem(),
				TFBlocks.GREEN_FORCE_FIELD.value().asItem(), TFBlocks.BLUE_FORCE_FIELD.value().asItem(),
				TFBlocks.VIOLET_FORCE_FIELD.value().asItem(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.value().asItem(),
				TFBlocks.NAGA_BOSS_SPAWNER.value().asItem(), TFBlocks.LICH_BOSS_SPAWNER.value().asItem(),
				TFBlocks.MINOSHROOM_BOSS_SPAWNER.value().asItem(), TFBlocks.HYDRA_BOSS_SPAWNER.value().asItem(),
				TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.value().asItem(), TFBlocks.UR_GHAST_BOSS_SPAWNER.value().asItem(),
				TFBlocks.ALPHA_YETI_BOSS_SPAWNER.value().asItem(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.value().asItem());
	}

	private static TagKey<Item> createTagFor(String modid, String tagName) {
		return ItemTags.create(new ResourceLocation(modid, tagName));
	}
}
