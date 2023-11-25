package twilightforest.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.*;
import twilightforest.block.Experiment115Block;
import twilightforest.init.*;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.Optional;
import java.util.function.Consumer;

public class TFAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {

	private static final EntityType<?>[] TF_KILLABLE = new EntityType<?>[]{TFEntities.ADHERENT.value(), TFEntities.ARMORED_GIANT.value(), TFEntities.BIGHORN_SHEEP.value(), TFEntities.BLOCKCHAIN_GOBLIN.value(), TFEntities.DWARF_RABBIT.value(), TFEntities.DEATH_TOME.value(), TFEntities.DEER.value(), TFEntities.FIRE_BEETLE.value(), TFEntities.GIANT_MINER.value(), TFEntities.LOWER_GOBLIN_KNIGHT.value(), TFEntities.UPPER_GOBLIN_KNIGHT.value(), TFEntities.HARBINGER_CUBE.value(), TFEntities.HEDGE_SPIDER.value(), TFEntities.HELMET_CRAB.value(), TFEntities.HOSTILE_WOLF.value(), TFEntities.HYDRA.value(), TFEntities.KING_SPIDER.value(), TFEntities.KNIGHT_PHANTOM.value(), TFEntities.KOBOLD.value(), TFEntities.LICH.value(), TFEntities.LICH_MINION.value(), TFEntities.MAZE_SLIME.value(), TFEntities.CARMINITE_GHASTLING.value(), TFEntities.MINOSHROOM.value(), TFEntities.MINOTAUR.value(), TFEntities.MIST_WOLF.value(), TFEntities.MOSQUITO_SWARM.value(), TFEntities.NAGA.value(), TFEntities.PENGUIN.value(), TFEntities.PINCH_BEETLE.value(), TFEntities.PLATEAU_BOSS.value(), TFEntities.QUEST_RAM.value(), TFEntities.RAVEN.value(), TFEntities.REDCAP.value(), TFEntities.REDCAP_SAPPER.value(), TFEntities.SKELETON_DRUID.value(), TFEntities.SLIME_BEETLE.value(), TFEntities.SNOW_GUARDIAN.value(), TFEntities.SNOW_QUEEN.value(), TFEntities.SQUIRREL.value(), TFEntities.STABLE_ICE_CORE.value(), TFEntities.SWARM_SPIDER.value(), TFEntities.TINY_BIRD.value(), TFEntities.CARMINITE_BROODLING.value(), TFEntities.CARMINITE_GHASTGUARD.value(), TFEntities.CARMINITE_GOLEM.value(), TFEntities.TOWERWOOD_BORER.value(), TFEntities.TROLL.value(), TFEntities.UNSTABLE_ICE_CORE.value(), TFEntities.UR_GHAST.value(), TFEntities.BOAR.value(), TFEntities.WINTER_WOLF.value(), TFEntities.WRAITH.value(), TFEntities.YETI.value(), TFEntities.ALPHA_YETI.value()};

	private static final ItemLike[] DENDROLOGIST_BLOCKS = new ItemLike[]{
			TFBlocks.TWILIGHT_OAK_LOG.value(), TFBlocks.TWILIGHT_OAK_WOOD.value(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.value(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.value(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.value(), TFBlocks.TWILIGHT_OAK_LEAVES.value(), TFBlocks.TWILIGHT_OAK_SAPLING.value(), TFBlocks.TWILIGHT_OAK_PLANKS.value(), TFBlocks.TWILIGHT_OAK_SLAB.value(), TFBlocks.TWILIGHT_OAK_STAIRS.value(), TFBlocks.TWILIGHT_OAK_BUTTON.value(), TFBlocks.TWILIGHT_OAK_FENCE.value(), TFBlocks.TWILIGHT_OAK_GATE.value(), TFBlocks.TWILIGHT_OAK_PLATE.value(), TFBlocks.TWILIGHT_OAK_DOOR.value(), TFBlocks.TWILIGHT_OAK_TRAPDOOR.value(), TFBlocks.TWILIGHT_OAK_SIGN.value(), TFBlocks.TWILIGHT_OAK_HANGING_SIGN.value(), TFBlocks.TWILIGHT_OAK_CHEST.value(), TFBlocks.TWILIGHT_OAK_BANISTER.value(), TFItems.TWILIGHT_OAK_BOAT.value(), TFItems.TWILIGHT_OAK_CHEST_BOAT.value(),
			TFBlocks.CANOPY_LOG.value(), TFBlocks.CANOPY_WOOD.value(), TFBlocks.STRIPPED_CANOPY_LOG.value(), TFBlocks.STRIPPED_CANOPY_WOOD.value(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.value(), TFBlocks.CANOPY_LEAVES.value(), TFBlocks.CANOPY_SAPLING.value(), TFBlocks.CANOPY_PLANKS.value(), TFBlocks.CANOPY_SLAB.value(), TFBlocks.CANOPY_STAIRS.value(), TFBlocks.CANOPY_BUTTON.value(), TFBlocks.CANOPY_FENCE.value(), TFBlocks.CANOPY_GATE.value(), TFBlocks.CANOPY_PLATE.value(), TFBlocks.CANOPY_DOOR.value(), TFBlocks.CANOPY_TRAPDOOR.value(), TFBlocks.CANOPY_SIGN.value(), TFBlocks.CANOPY_HANGING_SIGN.value(), TFBlocks.CANOPY_CHEST.value(), TFBlocks.CANOPY_BANISTER.value(), TFBlocks.CANOPY_BOOKSHELF.value(), TFBlocks.EMPTY_CANOPY_BOOKSHELF.value(), TFItems.CANOPY_BOAT.value(), TFItems.CANOPY_CHEST_BOAT.value(),
			TFBlocks.MANGROVE_LOG.value(), TFBlocks.MANGROVE_WOOD.value(), TFBlocks.STRIPPED_MANGROVE_LOG.value(), TFBlocks.STRIPPED_MANGROVE_WOOD.value(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.value(), TFBlocks.MANGROVE_LEAVES.value(), TFBlocks.MANGROVE_SAPLING.value(), TFBlocks.MANGROVE_PLANKS.value(), TFBlocks.MANGROVE_SLAB.value(), TFBlocks.MANGROVE_STAIRS.value(), TFBlocks.MANGROVE_BUTTON.value(), TFBlocks.MANGROVE_FENCE.value(), TFBlocks.MANGROVE_GATE.value(), TFBlocks.MANGROVE_PLATE.value(), TFBlocks.MANGROVE_DOOR.value(), TFBlocks.MANGROVE_TRAPDOOR.value(), TFBlocks.MANGROVE_SIGN.value(), TFBlocks.MANGROVE_HANGING_SIGN.value(), TFBlocks.MANGROVE_CHEST.value(), TFBlocks.MANGROVE_BANISTER.value(), TFItems.MANGROVE_BOAT.value(), TFItems.MANGROVE_CHEST_BOAT.value(),
			TFBlocks.DARK_LOG.value(), TFBlocks.DARK_WOOD.value(), TFBlocks.STRIPPED_DARK_LOG.value(), TFBlocks.STRIPPED_DARK_WOOD.value(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.value(), TFBlocks.DARK_LEAVES.value(), TFBlocks.DARKWOOD_SAPLING.value(), TFBlocks.DARK_PLANKS.value(), TFBlocks.DARK_SLAB.value(), TFBlocks.DARK_STAIRS.value(), TFBlocks.DARK_BUTTON.value(), TFBlocks.DARK_FENCE.value(), TFBlocks.DARK_GATE.value(), TFBlocks.DARK_PLATE.value(), TFBlocks.DARK_DOOR.value(), TFBlocks.DARK_TRAPDOOR.value(), TFBlocks.DARK_SIGN.value(), TFBlocks.DARK_HANGING_SIGN.value(), TFBlocks.DARK_CHEST.value(), TFBlocks.DARK_BANISTER.value(), TFItems.DARK_BOAT.value(), TFItems.DARK_CHEST_BOAT.value(),
			TFBlocks.TIME_LOG.value(), TFBlocks.TIME_WOOD.value(), TFBlocks.STRIPPED_TIME_LOG.value(), TFBlocks.STRIPPED_TIME_WOOD.value(), TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.value(), TFBlocks.TIME_LEAVES.value(), TFBlocks.TIME_SAPLING.value(), TFBlocks.TIME_PLANKS.value(), TFBlocks.TIME_SLAB.value(), TFBlocks.TIME_STAIRS.value(), TFBlocks.TIME_BUTTON.value(), TFBlocks.TIME_FENCE.value(), TFBlocks.TIME_GATE.value(), TFBlocks.TIME_PLATE.value(), TFBlocks.TIME_DOOR.value(), TFBlocks.TIME_TRAPDOOR.value(), TFBlocks.TIME_SIGN.value(), TFBlocks.TIME_HANGING_SIGN.value(), TFBlocks.TIME_CHEST.value(), TFBlocks.TIME_BANISTER.value(), TFItems.TIME_BOAT.value(), TFItems.TIME_CHEST_BOAT.value(),
			TFBlocks.TRANSFORMATION_LOG.value(), TFBlocks.TRANSFORMATION_WOOD.value(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.value(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.value(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.value(), TFBlocks.TRANSFORMATION_LEAVES.value(), TFBlocks.TRANSFORMATION_SAPLING.value(), TFBlocks.TRANSFORMATION_PLANKS.value(), TFBlocks.TRANSFORMATION_SLAB.value(), TFBlocks.TRANSFORMATION_STAIRS.value(), TFBlocks.TRANSFORMATION_BUTTON.value(), TFBlocks.TRANSFORMATION_FENCE.value(), TFBlocks.TRANSFORMATION_GATE.value(), TFBlocks.TRANSFORMATION_PLATE.value(), TFBlocks.TRANSFORMATION_DOOR.value(), TFBlocks.TRANSFORMATION_TRAPDOOR.value(), TFBlocks.TRANSFORMATION_SIGN.value(), TFBlocks.TRANSFORMATION_HANGING_SIGN.value(), TFBlocks.TRANSFORMATION_CHEST.value(), TFBlocks.TRANSFORMATION_BANISTER.value(), TFItems.TRANSFORMATION_BOAT.value(), TFItems.TRANSFORMATION_CHEST_BOAT.value(),
			TFBlocks.MINING_LOG.value(), TFBlocks.MINING_WOOD.value(), TFBlocks.STRIPPED_MINING_LOG.value(), TFBlocks.STRIPPED_MINING_WOOD.value(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.value(), TFBlocks.MINING_LEAVES.value(), TFBlocks.MINING_SAPLING.value(), TFBlocks.MINING_PLANKS.value(), TFBlocks.MINING_SLAB.value(), TFBlocks.MINING_STAIRS.value(), TFBlocks.MINING_BUTTON.value(), TFBlocks.MINING_FENCE.value(), TFBlocks.MINING_GATE.value(), TFBlocks.MINING_PLATE.value(), TFBlocks.MINING_DOOR.value(), TFBlocks.MINING_TRAPDOOR.value(), TFBlocks.MINING_SIGN.value(), TFBlocks.MINING_HANGING_SIGN.value(), TFBlocks.MINING_CHEST.value(), TFBlocks.MINING_BANISTER.value(), TFItems.MINING_BOAT.value(), TFItems.MINING_CHEST_BOAT.value(),
			TFBlocks.SORTING_LOG.value(), TFBlocks.SORTING_WOOD.value(), TFBlocks.STRIPPED_SORTING_LOG.value(), TFBlocks.STRIPPED_SORTING_WOOD.value(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.value(), TFBlocks.SORTING_LEAVES.value(), TFBlocks.SORTING_SAPLING.value(), TFBlocks.SORTING_PLANKS.value(), TFBlocks.SORTING_SLAB.value(), TFBlocks.SORTING_STAIRS.value(), TFBlocks.SORTING_BUTTON.value(), TFBlocks.SORTING_FENCE.value(), TFBlocks.SORTING_GATE.value(), TFBlocks.SORTING_PLATE.value(), TFBlocks.SORTING_DOOR.value(), TFBlocks.SORTING_TRAPDOOR.value(), TFBlocks.SORTING_SIGN.value(), TFBlocks.SORTING_HANGING_SIGN.value(), TFBlocks.SORTING_CHEST.value(), TFBlocks.SORTING_BANISTER.value(), TFItems.SORTING_BOAT.value(), TFItems.SORTING_CHEST_BOAT.value(),
			TFBlocks.TOWERWOOD.value(), TFBlocks.CRACKED_TOWERWOOD.value(), TFBlocks.MOSSY_TOWERWOOD.value(), TFBlocks.ENCASED_TOWERWOOD.value(),
			TFBlocks.ROOT_BLOCK.value(), TFBlocks.ROOT_STRAND.value(), TFBlocks.LIVEROOT_BLOCK.value(), TFItems.LIVEROOT.value(), TFBlocks.HOLLOW_OAK_SAPLING.value(), TFBlocks.RAINBOW_OAK_SAPLING.value(), TFBlocks.RAINBOW_OAK_LEAVES.value(), TFBlocks.GIANT_LOG.value(), TFBlocks.GIANT_LEAVES.value(), TFBlocks.HUGE_STALK.value(), TFBlocks.BEANSTALK_LEAVES.value(), TFBlocks.THORN_LEAVES.value(), TFBlocks.THORN_ROSE.value(), TFBlocks.HEDGE.value(), TFBlocks.FALLEN_LEAVES.value(), TFBlocks.MANGROVE_ROOT.value(),
	};

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper helper) {
		AdvancementHolder root = Advancement.Builder.advancement().display(
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.value(),
				Component.translatable("advancement.twilightforest.root"),
				Component.translatable("advancement.twilightforest.root.desc"),
				new ResourceLocation(TwilightForestMod.ID, "textures/block/mazestone_large_brick.png"),
				FrameType.TASK,
				true, false, false)
				.requirements(AdvancementRequirements.Strategy.OR)
				.addCriterion("in_tf",
						PlayerTrigger.TriggerInstance.located(
								LocationPredicate.Builder.inDimension(TFGenerationSettings.DIMENSION_KEY)))
				.addCriterion("make_portal",
						MakePortalTrigger.TriggerInstance.makePortal())
				.save(consumer, "twilightforest:root");

		AdvancementHolder silence = this.addTFKillable(Advancement.Builder.advancement().parent(root).display(
				TFItems.RAVEN_FEATHER.value(),
						Component.translatable("advancement.twilightforest.twilight_hunter"),
						Component.translatable("advancement.twilightforest.twilight_hunter.desc"),
				null, FrameType.TASK, true, true, false)
						.requirements(AdvancementRequirements.Strategy.OR))
				.save(consumer, "twilightforest:twilight_hunter");

		AdvancementHolder naga = Advancement.Builder.advancement().parent(root).display(
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.value(),
				Component.translatable("advancement.twilightforest.kill_naga"),
				Component.translatable("advancement.twilightforest.kill_naga.desc",
						Component.translatable(TFEntities.NAGA.value().getDescriptionId()),
						Component.translatable(TFItems.NAGA_SCALE.value().getDescriptionId())),
				null, FrameType.GOAL, true, true, false)
				.addCriterion("naga", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.NAGA.value())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.NAGA_TROPHY.value()))
				.addCriterion("scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_SCALE.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.NAGA.value())))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, "twilightforest:progress_naga");

		AdvancementHolder lich = Advancement.Builder.advancement().parent(naga).display(
						TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.value(),
						Component.translatable("advancement.twilightforest.kill_lich"),
						Component.translatable("advancement.twilightforest.kill_lich.desc",
								Component.translatable(TFEntities.LICH.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("kill_lich", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.LICH.value())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.LICH_TROPHY.value()))
				.addCriterion("lifedrain_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIFEDRAIN_SCEPTER.value()))
				.addCriterion("twilight_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TWILIGHT_SCEPTER.value()))
				.addCriterion("zombie_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ZOMBIE_SCEPTER.value()))
				.addCriterion("shield_scepter", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FORTIFICATION_SCEPTER.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.LICH.value())))
				.addCriterion("kill_naga", this.advancementTrigger(naga))
				.requirements(new CountRequirementsStrategy(7, 1))
				.save(consumer, "twilightforest:progress_lich");

		AdvancementHolder minoshroom = Advancement.Builder.advancement().parent(lich).display(
						TFItems.MEEF_STROGANOFF.value(),
						Component.translatable("advancement.twilightforest.progress_labyrinth"),
						Component.translatable("advancement.twilightforest.progress_labyrinth.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MEEF_STROGANOFF.value()))
				.addCriterion("kill_lich", this.advancementTrigger(lich))
				.requirements(AdvancementRequirements.Strategy.AND)
				.save(consumer, "twilightforest:progress_labyrinth");

		AdvancementHolder hydra = Advancement.Builder.advancement().parent(minoshroom).display(
						TFBlocks.HYDRA_TROPHY.value(),
						Component.translatable("advancement.twilightforest.kill_hydra"),
						Component.translatable("advancement.twilightforest.kill_hydra.desc",
								Component.translatable(TFEntities.HYDRA.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("kill_hydra", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.HYDRA.value())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.HYDRA_TROPHY.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.HYDRA.value())))
				.addCriterion("stroganoff", this.advancementTrigger(minoshroom))
				.requirements(new CountRequirementsStrategy(3, 1))
				.save(consumer, "twilightforest:progress_hydra");

		AdvancementHolder trophy_pedestal = Advancement.Builder.advancement().parent(lich).display(
						TFBlocks.TROPHY_PEDESTAL.value(),
						Component.translatable("advancement.twilightforest.progress_trophy_pedestal"),
						Component.translatable("advancement.twilightforest.progress_trophy_pedestal.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("trophy_pedestal", TrophyPedestalTrigger.TriggerInstance.activatePedestal())
				.addCriterion("kill_lich", this.advancementTrigger(lich))
				.requirements(AdvancementRequirements.Strategy.AND)
				.save(consumer, "twilightforest:progress_trophy_pedestal");

		AdvancementHolder knights = Advancement.Builder.advancement().parent(trophy_pedestal).display(
						TFBlocks.KNIGHT_PHANTOM_TROPHY.value(),
						Component.translatable("advancement.twilightforest.progress_knights"),
						Component.translatable("advancement.twilightforest.progress_knights.desc"),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("all_knights", KillAllPhantomsTrigger.TriggerInstance.killThemAll())
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.KNIGHT_PHANTOM_TROPHY.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.KNIGHT_PHANTOM.value())))
				.addCriterion("previous_progression", this.advancementTrigger(trophy_pedestal))
				.requirements(new CountRequirementsStrategy(3, 1))
				.save(consumer, "twilightforest:progress_knights");

		AdvancementHolder trap = Advancement.Builder.advancement().parent(knights).display(
				TFBlocks.GHAST_TRAP.value(),
				Component.translatable("advancement.twilightforest.ghast_trap"),
				Component.translatable("advancement.twilightforest.ghast_trap.desc",
						Component.translatable(TFEntities.CARMINITE_GHASTLING.value().getDescriptionId()),
						Component.translatable(TFBlocks.GHAST_TRAP.value().getDescriptionId()),
						Component.translatable(TFEntities.UR_GHAST.value().getDescriptionId())),
				null, FrameType.TASK, true, true, false)
				.addCriterion("activate_ghast_trap", ActivateGhastTrapTrigger.TriggerInstance.activateTrap())
				.save(consumer, "twilightforest:ghast_trap");

		AdvancementHolder ur_ghast = Advancement.Builder.advancement().parent(trap).display(
						TFBlocks.UR_GHAST_TROPHY.value(),
						Component.translatable("advancement.twilightforest.progress_ur_ghast"),
						Component.translatable("advancement.twilightforest.progress_ur_ghast.desc",
								Component.translatable(TFEntities.UR_GHAST.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.UR_GHAST.value())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.UR_GHAST_TROPHY.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.UR_GHAST.value())))
				.addCriterion("previous_progression", this.advancementTrigger(knights))
				.requirements(new CountRequirementsStrategy(3, 1))
				.save(consumer, "twilightforest:progress_ur_ghast");

		AdvancementHolder yeti = Advancement.Builder.advancement().parent(lich).display(
						TFItems.ALPHA_YETI_FUR.value(),
						Component.translatable("advancement.twilightforest.progress_yeti"),
						Component.translatable("advancement.twilightforest.progress_yeti.desc",
								Component.translatable(TFEntities.ALPHA_YETI.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("yeti", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.ALPHA_YETI.value())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ALPHA_YETI_TROPHY.value()))
				.addCriterion("fur", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ALPHA_YETI_FUR.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.ALPHA_YETI.value())))
				.addCriterion("previous_progression", this.advancementTrigger(lich))
				.requirements(new CountRequirementsStrategy(4, 1))
				.save(consumer, "twilightforest:progress_yeti");

		AdvancementHolder snow_queen = Advancement.Builder.advancement().parent(yeti).display(
						TFBlocks.SNOW_QUEEN_TROPHY.value(),
						Component.translatable("advancement.twilightforest.progress_glacier"),
						Component.translatable("advancement.twilightforest.progress_glacier.desc",
								Component.translatable(TFEntities.SNOW_QUEEN.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("queen", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.SNOW_QUEEN.value())))
				.addCriterion("trophy", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SNOW_QUEEN_TROPHY.value()))
				.addCriterion("was_in_fight", HurtBossTrigger.TriggerInstance.hurtBoss(EntityPredicate.Builder.entity().of(TFEntities.SNOW_QUEEN.value())))
				.addCriterion("previous_progression", this.advancementTrigger(yeti))
				.requirements(new CountRequirementsStrategy(3, 1))
				.save(consumer, "twilightforest:progress_glacier");

		AdvancementHolder merge = Advancement.Builder.advancement().parent(lich).display(
						TFBlocks.UBEROUS_SOIL.value(),
						Component.translatable("advancement.twilightforest.progress_merge"),
						Component.translatable("advancement.twilightforest.progress_merge.desc",
								Component.translatable(TFEntities.HYDRA.value().getDescriptionId()),
								Component.translatable(TFEntities.UR_GHAST.value().getDescriptionId()),
								Component.translatable(TFEntities.SNOW_QUEEN.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("hydra", this.advancementTrigger(hydra))
				.addCriterion("ur_ghast", this.advancementTrigger(ur_ghast))
				.addCriterion("snow_queen", this.advancementTrigger(snow_queen))
				.save(consumer, "twilightforest:progress_merge");

		Advancement.Builder.advancement().parent(merge).display(
						TFItems.MAGIC_BEANS.value(),
						Component.translatable("advancement.twilightforest.troll"),
						Component.translatable("advancement.twilightforest.troll.desc",
								Component.translatable(TFEntities.TROLL.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("troll", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.TROLL.value()).located(LocationPredicate.Builder.inStructure(TFStructures.TROLL_CAVE))))
				.save(consumer, "twilightforest:troll");

		AdvancementHolder beanstalk = Advancement.Builder.advancement().parent(merge).display(
						TFBlocks.HUGE_STALK.value(),
						Component.translatable("advancement.twilightforest.beanstalk"),
						Component.translatable("advancement.twilightforest.beanstalk.desc",
								Component.translatable(TFItems.MAGIC_BEANS.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("beans", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.value()))
				.addCriterion("use_beans", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.UBEROUS_SOIL.value())), ItemPredicate.Builder.item().of(TFItems.MAGIC_BEANS.value())))
				.save(consumer, "twilightforest:beanstalk");

		AdvancementHolder giants = Advancement.Builder.advancement().parent(beanstalk).display(
						TFItems.GIANT_PICKAXE.value(),
						Component.translatable("advancement.twilightforest.giants"),
						Component.translatable("advancement.twilightforest.giants.desc",
								Component.translatable(TFEntities.GIANT_MINER.value().getDescriptionId()),
								Component.translatable(TFItems.GIANT_PICKAXE.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("giant", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.GIANT_MINER.value())))
				.addCriterion("pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.value()))
				.save(consumer, "twilightforest:giants");

		AdvancementHolder lamp = Advancement.Builder.advancement().parent(giants).display(
						TFItems.LAMP_OF_CINDERS.value(),
						Component.translatable("advancement.twilightforest.progress_troll"),
						Component.translatable("advancement.twilightforest.progress_troll.desc",
								Component.translatable(TFItems.LAMP_OF_CINDERS.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("lamp", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LAMP_OF_CINDERS.value()))
				.addCriterion("previous_progression", this.advancementTrigger(merge))
				.save(consumer, "twilightforest:progress_troll");

		Advancement.Builder.advancement().parent(lamp).display(
				Items.STRUCTURE_VOID,
				Component.translatable("advancement.twilightforest.progression_end"),
				Component.translatable("advancement.twilightforest.progression_end.desc"),
				null, FrameType.GOAL, true, false, false)
				.addCriterion("previous_progression", this.advancementTrigger(lamp))
				.addCriterion("plateau", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(TFBiomes.FINAL_PLATEAU)))
				.save(consumer, "twilightforest:progression_end");

//		AdvancementHolder thornlands = Advancement.Builder.advancement().parent(lamp).display(
//						TFBlocks.BROWN_THORNS.value(),
//						Component.translatable("advancement.twilightforest.progress_thorns"),
//						Component.translatable("advancement.twilightforest.progress_thorns.desc"),
//						null, FrameType.GOAL, true, true, false)
//				.addCriterion("castle", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(TFBiomes.FINAL_PLATEAU)))
//				.addCriterion("previous_progression", this.advancementTrigger(lamp))
//				.save(consumer, "twilightforest:progress_thorns");
//
//		Advancement.Builder.advancement().parent(thornlands).display(
//						TFBlocks.VIOLET_CASTLE_RUNE_BRICK.value(),
//						Component.translatable("advancement.twilightforest.progress_castle"),
//						Component.translatable("advancement.twilightforest.progress_castle.desc"),
//						null, FrameType.GOAL, true, true, false)
//				.addCriterion("castle", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(TFStructures.FINAL_CASTLE)))
//				.addCriterion("previous_progression", this.advancementTrigger(thornlands))
//				.save(consumer, "twilightforest:progress_castle");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.QUEST_RAM_TROPHY.value(),
						Component.translatable("advancement.twilightforest.quest_ram"),
						Component.translatable("advancement.twilightforest.quest_ram.desc",
								Component.translatable(TFEntities.QUEST_RAM.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("quest_ram_complete", QuestRamCompletionTrigger.TriggerInstance.completeRam())
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "twilightforest:quest_ram");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.CICADA.value(),
						Component.translatable("advancement.twilightforest.kill_cicada"),
						Component.translatable("advancement.twilightforest.kill_cicada.desc"),
						null, FrameType.TASK, true, false, true)
				.addCriterion("kill_cicada", KillBugTrigger.TriggerInstance.killBug(TFBlocks.CICADA.value()))
				.save(consumer, "twilightforest:kill_cicada");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.UNCRAFTING_TABLE.value(),
						Component.translatable("advancement.twilightforest.uncraft_uncrafting_table"),
						Component.translatable("advancement.twilightforest.uncraft_uncrafting_table.desc"),
						null, FrameType.TASK, true, true, true)
				.addCriterion("uncraft_table", UncraftItemTrigger.TriggerInstance.uncraftedItem(TFBlocks.UNCRAFTING_TABLE.value()))
				.save(consumer, "twilightforest:uncraft_uncrafting_table");

		AdvancementHolder focus = Advancement.Builder.advancement().parent(silence).display(
						TFItems.MAGIC_MAP_FOCUS.value(),
						Component.translatable("advancement.twilightforest.magic_map_focus"),
						Component.translatable("advancement.twilightforest.magic_map_focus.desc",
								Component.translatable(TFItems.MAGIC_MAP_FOCUS.value().getDescriptionId()),
								Component.translatable(TFItems.RAVEN_FEATHER.value().getDescriptionId()),
								Component.translatable(Items.GLOWSTONE_DUST.getDescriptionId()),
								Component.translatable(TFItems.TORCHBERRIES.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP_FOCUS.value()))
				.save(consumer, "twilightforest:magic_map_focus");

		AdvancementHolder magic_map = Advancement.Builder.advancement().parent(focus).display(
						TFItems.FILLED_MAGIC_MAP.value(),
						Component.translatable("advancement.twilightforest.magic_map"),
						Component.translatable("advancement.twilightforest.magic_map.desc",
								Component.translatable(TFItems.FILLED_MAGIC_MAP.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("magic_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAGIC_MAP.value()))
				.save(consumer, "twilightforest:magic_map");

		AdvancementHolder maze_map = Advancement.Builder.advancement().parent(magic_map).display(
						TFItems.FILLED_MAZE_MAP.value(),
						Component.translatable("advancement.twilightforest.maze_map"),
						Component.translatable("advancement.twilightforest.maze_map.desc",
								Component.translatable(TFItems.FILLED_MAZE_MAP.value().getDescriptionId())),
						null, FrameType.GOAL, true, true, false)
				.addCriterion("maze_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAZE_MAP.value()))
				.save(consumer, "twilightforest:maze_map");

		Advancement.Builder.advancement().parent(maze_map).display(
						TFItems.FILLED_ORE_MAP.value(),
						Component.translatable("advancement.twilightforest.ore_map"),
						Component.translatable("advancement.twilightforest.ore_map.desc",
								Component.translatable(TFItems.FILLED_ORE_MAP.value().getDescriptionId())),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("ore_map", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_ORE_MAP.value()))
				.save(consumer, "twilightforest:ore_map");

		AdvancementHolder hill1 =  Advancement.Builder.advancement().parent(root).display(
						Items.IRON_BOOTS,
						Component.translatable("advancement.twilightforest.hill1"),
						Component.translatable("advancement.twilightforest.hill1.desc",
								Component.translatable(TFEntities.REDCAP.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.REDCAP.value()).located(LocationPredicate.Builder.inStructure(TFStructures.HOLLOW_HILL_SMALL))))
				.save(consumer, "twilightforest:hill1");

		AdvancementHolder hill2 =  Advancement.Builder.advancement().parent(hill1).display(
						TFItems.IRONWOOD_PICKAXE.value(),
						Component.translatable("advancement.twilightforest.hill2"),
						Component.translatable("advancement.twilightforest.hill2.desc",
								Component.translatable(TFEntities.REDCAP_SAPPER.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.REDCAP_SAPPER.value()).located(LocationPredicate.Builder.inStructure(TFStructures.HOLLOW_HILL_MEDIUM))))
				.save(consumer, "twilightforest:hill2");

		Advancement.Builder.advancement().parent(hill2).display(
						Items.GLOWSTONE_DUST,
						Component.translatable("advancement.twilightforest.hill3"),
						Component.translatable("advancement.twilightforest.hill3.desc",
								Component.translatable(TFEntities.WRAITH.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("redcap", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.WRAITH.value()).located(LocationPredicate.Builder.inStructure(TFStructures.HOLLOW_HILL_LARGE))))
				.save(consumer, "twilightforest:hill3");

		Advancement.Builder.advancement().parent(root).display(
						TFBlocks.HEDGE.value(),
						Component.translatable("advancement.twilightforest.hedge"),
						Component.translatable("advancement.twilightforest.hedge.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("hedge_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.HEDGE_SPIDER.value()).located(LocationPredicate.Builder.inStructure(TFStructures.HEDGE_MAZE))))
				.addCriterion("swarm_spider", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.SWARM_SPIDER.value()).located(LocationPredicate.Builder.inStructure(TFStructures.HEDGE_MAZE))))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, "twilightforest:hedge");

		Advancement.Builder.advancement().parent(root).display(
						Items.BOWL,
						Component.translatable("advancement.twilightforest.twilight_dining"),
						Component.translatable("advancement.twilightforest.twilight_dining.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("raw_venison", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.RAW_VENISON.value()))
				.addCriterion("cooked_venison", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.COOKED_VENISON.value()))
				.addCriterion("raw_meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.RAW_MEEF.value()))
				.addCriterion("cooked_meef", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.COOKED_MEEF.value()))
				.addCriterion("meef_stroganoff", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MEEF_STROGANOFF.value()))
				.addCriterion("hydra_chop", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.HYDRA_CHOP.value()))
				.addCriterion("maze_wafer", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.MAZE_WAFER.value()))
				.addCriterion("experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.EXPERIMENT_115.value()))
				.addCriterion("torchberries", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.TORCHBERRIES.value()))
				.requirements(new CountRequirementsStrategy(2, 2, 1, 1, 1, 1, 1))
				.rewards(AdvancementRewards.Builder.experience(75))
				.save(consumer, "twilightforest:twilight_dinner");

		Advancement.Builder.advancement().parent(naga).display(
						TFItems.NAGA_CHESTPLATE.value(),
						Component.translatable("advancement.twilightforest.naga_armors"),
						Component.translatable("advancement.twilightforest.naga_armors.desc",
								Component.translatable(TFItems.NAGA_SCALE.value().getDescriptionId())),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("armor", InventoryChangeTrigger.TriggerInstance.hasItems(
						TFItems.NAGA_CHESTPLATE.value(), TFItems.NAGA_LEGGINGS.value()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(consumer, "twilightforest:naga_armors");

		Advancement.Builder.advancement().parent(lich).display(
						TFItems.ZOMBIE_SCEPTER.value(),
						Component.translatable("advancement.twilightforest.lich_scepters"),
						Component.translatable("advancement.twilightforest.lich_scepters.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("scepters", InventoryChangeTrigger.TriggerInstance.hasItems(
						TFItems.LIFEDRAIN_SCEPTER.value(), TFItems.TWILIGHT_SCEPTER.value(),
						TFItems.ZOMBIE_SCEPTER.value(), TFItems.FORTIFICATION_SCEPTER.value()))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "twilightforest:lich_scepters");

		Advancement.Builder.advancement().parent(lich).display(
						flaskWithHarming(),
						Component.translatable("advancement.twilightforest.full_mettle_alchemist"),
						Component.translatable("advancement.twilightforest.full_mettle_alchemist.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("drink_4_harming", DrinkFromFlaskTrigger.TriggerInstance.drankPotion(4, Potions.STRONG_HARMING))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, "twilightforest:full_mettle_alchemist");

		Advancement.Builder.advancement().parent(minoshroom).display(
						TFItems.MAZEBREAKER_PICKAXE.value(),
						Component.translatable("advancement.twilightforest.mazebreaker"),
						Component.translatable("advancement.twilightforest.mazebreaker.desc",
								Component.translatable(TFItems.MAZEBREAKER_PICKAXE.value().getDescriptionId())),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZEBREAKER_PICKAXE.value()))
				.rewards(AdvancementRewards.Builder.experience(50))
				.save(consumer, "twilightforest:mazebreaker");

		Advancement.Builder.advancement().parent(hydra).display(
						TFItems.HYDRA_CHOP.value(),
						Component.translatable("advancement.twilightforest.hydra_chop"),
						Component.translatable("advancement.twilightforest.hydra_chop.desc",
								Component.translatable(TFEntities.HYDRA.value().getDescriptionId())),
						null, FrameType.TASK, true, true, false)
				.addCriterion("hydra_chop", HydraChopTrigger.TriggerInstance.eatChop())
				.save(consumer, "twilightforest:hydra_chop");

		Advancement.Builder.advancement().parent(hydra).display(
						TFItems.FIERY_SWORD.value(),
						Component.translatable("advancement.twilightforest.fiery_set"),
						Component.translatable("advancement.twilightforest.fiery_set.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.addCriterion("fiery_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_PICKAXE.value()))
				.addCriterion("fiery_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_SWORD.value()))
				.addCriterion("fiery_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_HELMET.value()))
				.addCriterion("fiery_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_CHESTPLATE.value()))
				.addCriterion("fiery_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_LEGGINGS.value()))
				.addCriterion("fiery_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BOOTS.value()))
				.requirements(new CountRequirementsStrategy(2, 4))
				.rewards(AdvancementRewards.Builder.experience(75))
				.save(consumer, "twilightforest:fiery_set");

		AdvancementHolder e115 = Advancement.Builder.advancement().parent(knights).display(
						TFItems.EXPERIMENT_115.value(),
						Component.translatable("advancement.twilightforest.experiment_115"),
						Component.translatable("advancement.twilightforest.experiment_115.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("eat_experiment_115", ConsumeItemTrigger.TriggerInstance.usedItem(TFItems.EXPERIMENT_115.value()))
				.save(consumer, "twilightforest:experiment_115");

		Advancement.Builder.advancement().parent(e115).display(
						e115Tag("think"),
						Component.translatable("advancement.twilightforest.experiment_115_3"),
						Component.translatable("advancement.twilightforest.experiment_115_3.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("eat_115_e115", PlayerTrigger.TriggerInstance.located(Optional.of(EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().addStat(Stats.CUSTOM, registries.lookupOrThrow(Registries.CUSTOM_STAT).getOrThrow(TFStats.E115_SLICES_EATEN.getKey()), MinMaxBounds.Ints.atLeast(115)).build()).build())))
				.save(consumer, "twilightforest:experiment_115_115");

		Advancement.Builder.advancement().parent(e115).display(
						e115Tag("full"),
						Component.translatable("advancement.twilightforest.experiment_115_2"),
						Component.translatable("advancement.twilightforest.experiment_115_2.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("place_complete_e115", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.EXPERIMENT_115.value()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Experiment115Block.REGENERATE, true))), ItemPredicate.Builder.item().of(Items.REDSTONE)))
				.save(consumer, "twilightforest:experiment_115_self_replenishing");

		Advancement.Builder.advancement().parent(yeti).display(
						TFItems.ARCTIC_CHESTPLATE.value(),
						Component.translatable("advancement.twilightforest.arctic_dyed"),
						Component.translatable("advancement.twilightforest.arctic_dyed.desc"),
						null, FrameType.TASK, true, true, false)
				.addCriterion("helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_HELMET.value()).hasNbt(arcticDye(TFItems.ARCTIC_HELMET.value())).build()))
				.addCriterion("chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_CHESTPLATE.value()).hasNbt(arcticDye(TFItems.ARCTIC_CHESTPLATE.value())).build()))
				.addCriterion("leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_LEGGINGS.value()).hasNbt(arcticDye(TFItems.ARCTIC_LEGGINGS.value())).build()))
				.addCriterion("boots", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TFItems.ARCTIC_BOOTS.value()).hasNbt(arcticDye(TFItems.ARCTIC_BOOTS.value())).build()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(consumer, "twilightforest:arctic_armor_dyed");

		Advancement.Builder.advancement().parent(yeti).display(
						TFItems.GLASS_SWORD.value(),
						Component.translatable("advancement.twilightforest.glass_sword"),
						Component.translatable("advancement.twilightforest.glass_sword.desc"),
						null, FrameType.CHALLENGE, true, true, true)
				.addCriterion("broken_sword", ItemDurabilityTrigger.TriggerInstance.changedDurability(Optional.of(ItemPredicate.Builder.item().of(TFItems.GLASS_SWORD.value()).build()), MinMaxBounds.Ints.exactly(-1)))
				.rewards(AdvancementRewards.Builder.experience(42))
				.save(consumer, "twilightforest:break_glass_sword");

		this.addDendrologistBlock(Advancement.Builder.advancement().parent(root)
				.display(TFBlocks.TWILIGHT_OAK_FENCE.value(),
						Component.translatable("advancement.twilightforest.arborist"),
						Component.translatable("advancement.twilightforest.arborist.desc"),
						null, FrameType.CHALLENGE, true, true, false)
				.requirements(AdvancementRequirements.Strategy.AND))
				.rewards(AdvancementRewards.Builder.experience(1000))
				.save(consumer, "twilightforest:arborist");

	}

	private ItemStack e115Tag(String nbt) {
		ItemStack itemstack = new ItemStack(TFItems.EXPERIMENT_115.value());
		CompoundTag compoundtag = itemstack.getOrCreateTagElement(nbt);
		compoundtag.putInt(nbt, 1);
		return itemstack;
	}

	private ItemStack flaskWithHarming() {
		ItemStack itemstack = new ItemStack(TFItems.GREATER_FLASK.value());
		CompoundTag compoundtag = itemstack.getOrCreateTag();
		compoundtag.putInt("Uses", 4);
		compoundtag.putString("Potion", BuiltInRegistries.POTION.getKey(Potions.STRONG_HARMING).toString());
		return itemstack;
	}

	private CompoundTag arcticDye(Item item) {
		ItemStack itemstack = new ItemStack(item);
		CompoundTag compoundtag = itemstack.getOrCreateTagElement("display");
		CompoundTag color = itemstack.getOrCreateTagElement("hasColor");
		color.putBoolean("hasColor", true);
		compoundtag.put("display", color);
		return compoundtag;
	}

	private Advancement.Builder addTFKillable(Advancement.Builder builder) {
		for (EntityType<?> entity : TF_KILLABLE) {
			builder.addCriterion(EntityType.getKey(entity).getPath(),
					KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity)
							.located(LocationPredicate.Builder.inDimension(TFGenerationSettings.DIMENSION_KEY))));
		}
		return builder;
	}

	private Advancement.Builder addDendrologistBlock(Advancement.Builder builder) {
		for (ItemLike dendrologistBlock : DENDROLOGIST_BLOCKS) {
			builder.addCriterion(BuiltInRegistries.ITEM.getKey(dendrologistBlock.asItem()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(dendrologistBlock));
		}
		return builder;
	}

	private Criterion<PlayerTrigger.TriggerInstance> advancementTrigger(AdvancementHolder advancement) {
		return this.advancementTrigger(advancement.id().getPath());
	}

	private Criterion<PlayerTrigger.TriggerInstance> advancementTrigger(String name) {
		return CriteriaTriggers.TICK.createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().checkAdvancementDone(TwilightForestMod.prefix(name), true).build())).build()))));
	}
}
