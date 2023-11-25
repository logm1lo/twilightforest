package twilightforest.data;

import net.minecraft.Util;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;
import twilightforest.loot.conditions.IsMinionCondition;

import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class EntityLootTables extends EntityLootSubProvider {

	protected EntityLootTables() {
		super(FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	public void generate() {
		add(TFEntities.ADHERENT.value(), emptyLootTable());
		add(TFEntities.LICH_MINION.value(), emptyLootTable());
		add(TFEntities.LOYAL_ZOMBIE.value(), emptyLootTable());
		add(TFEntities.KNIGHT_PHANTOM.value(), emptyLootTable());
		//haha no loot for you
		add(TFEntities.PLATEAU_BOSS.value(), emptyLootTable());
		add(TFEntities.HARBINGER_CUBE.value(), emptyLootTable());
		add(TFEntities.MOSQUITO_SWARM.value(), emptyLootTable());
		add(TFEntities.PINCH_BEETLE.value(), emptyLootTable());
		add(TFEntities.QUEST_RAM.value(), emptyLootTable());
		add(TFEntities.ROVING_CUBE.value(), emptyLootTable());
		add(TFEntities.SQUIRREL.value(), emptyLootTable());
		add(TFEntities.DWARF_RABBIT.value(), fromEntityLootTable(EntityType.RABBIT));
		add(TFEntities.HEDGE_SPIDER.value(), fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.FIRE_BEETLE.value(), fromEntityLootTable(EntityType.CREEPER));
		add(TFEntities.HOSTILE_WOLF.value(), fromEntityLootTable(EntityType.WOLF));
		add(TFEntities.KING_SPIDER.value(), fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.MIST_WOLF.value(), fromEntityLootTable(EntityType.WOLF));
		add(TFEntities.REDCAP_SAPPER.value(), fromEntityLootTable(TFEntities.REDCAP.value()));
		add(TFEntities.SWARM_SPIDER.value(), fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.CARMINITE_BROODLING.value(), fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.CARMINITE_GHASTGUARD.value(), fromEntityLootTable(EntityType.GHAST));
		add(TFEntities.BIGHORN_SHEEP.value(), fromEntityLootTable(EntityType.SHEEP));
		add(TFEntities.RISING_ZOMBIE.value(), fromEntityLootTable(EntityType.ZOMBIE));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_BLACK, sheepLootTableBuilderWithDrop(Blocks.BLACK_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_BLUE, sheepLootTableBuilderWithDrop(Blocks.BLUE_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_BROWN, sheepLootTableBuilderWithDrop(Blocks.BROWN_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_CYAN, sheepLootTableBuilderWithDrop(Blocks.CYAN_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_GRAY, sheepLootTableBuilderWithDrop(Blocks.GRAY_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_GREEN, sheepLootTableBuilderWithDrop(Blocks.GREEN_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_LIGHT_BLUE, sheepLootTableBuilderWithDrop(Blocks.LIGHT_BLUE_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_LIGHT_GRAY, sheepLootTableBuilderWithDrop(Blocks.LIGHT_GRAY_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_LIME, sheepLootTableBuilderWithDrop(Blocks.LIME_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_MAGENTA, sheepLootTableBuilderWithDrop(Blocks.MAGENTA_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_ORANGE, sheepLootTableBuilderWithDrop(Blocks.ORANGE_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_PINK, sheepLootTableBuilderWithDrop(Blocks.PINK_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_PURPLE, sheepLootTableBuilderWithDrop(Blocks.PURPLE_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_RED, sheepLootTableBuilderWithDrop(Blocks.RED_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_WHITE, sheepLootTableBuilderWithDrop(Blocks.WHITE_WOOL));
		add(TFEntities.BIGHORN_SHEEP.value(), TFLootTables.BIGHORN_SHEEP_YELLOW, sheepLootTableBuilderWithDrop(Blocks.YELLOW_WOOL));

		add(TFEntities.ARMORED_GIANT.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.GIANT_SWORD.value()))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())));

		add(TFEntities.GIANT_MINER.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.GIANT_PICKAXE.value()))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())));

		add(TFEntities.BLOCKCHAIN_GOBLIN.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.CARMINITE_GHASTLING.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootTableReference.lootTableReference(EntityType.GHAST.getDefaultLootTable()))
								.when(IsMinionCondition.builder(true))));

		/*registerLootTable(TFEntities.BOGGARD.value(),
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.MAZE_MAP_FOCUS.value())
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.2F)))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_BOOTS)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.1666F)))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_PICKAXE)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.1111F))));*/

		add(TFEntities.BOAR.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.PORKCHOP)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
										.apply(SmeltItemFunction.smelted()
												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
										.apply(SetNameFunction.setName(Component.translatable("item.twilightforest.boarkchop").withStyle(Style.EMPTY.withItalic(false)))
												.when(LootItemRandomChanceCondition.randomChance(0.002F))
												.when(LootItemKilledByPlayerCondition.killedByPlayer())))));

		add(TFEntities.HELMET_CRAB.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.COD)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
										.apply(SmeltItemFunction.smelted()
												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.when((LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.1F)))));

		add(TFEntities.UPPER_GOBLIN_KNIGHT.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.LOWER_GOBLIN_KNIGHT.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.WRAITH.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.REDCAP.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.COAL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.YETI.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.WINTER_WOLF.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.TINY_BIRD.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.FEATHER)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.PENGUIN.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.FEATHER)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.ICE_CRYSTAL.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.SNOWBALL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.UNSTABLE_ICE_CORE.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.SNOWBALL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.STABLE_ICE_CORE.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.SNOWBALL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.SNOW_GUARDIAN.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.SNOWBALL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.RAVEN.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.RAVEN_FEATHER.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.TOWERWOOD_BORER.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.BORER_ESSENCE.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.SKELETON_DRUID.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.BONE)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.DEER.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.LEATHER)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.RAW_VENISON.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
										.apply(SmeltItemFunction.smelted()
												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.KOBOLD.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.WHEAT)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.GOLD_NUGGET)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())));

		add(TFEntities.MAZE_SLIME.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.SLIME_BALL)
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.value()))
								.when((LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.015F, 0.005F)))));

		add(TFEntities.MINOTAUR.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.RAW_MEEF.value())
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
										.apply(SmeltItemFunction.smelted()
												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.MAZE_MAP_FOCUS.value()))
								.when((LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F)))));

		add(TFEntities.CARMINITE_GOLEM.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.IRON_INGOT)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.TOWERWOOD.value()))
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))));

		add(TFEntities.SLIME_BEETLE.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(Items.SLIME_BALL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.TROLL.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.MAGIC_BEANS.value()))
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));

		add(TFEntities.DEATH_TOME.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.PAPER))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1))))
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(2).setQuality(3))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(19))
								.add(LootTableReference.lootTableReference(TFLootTables.DEATH_TOME_BOOKS).setWeight(1)))
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.005F))
								.add(LootItem.lootTableItem(TFItems.MAGIC_MAP_FOCUS.value()))));

		add(TFEntities.DEATH_TOME.value(), TFLootTables.DEATH_TOME_HURT,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootTableReference.lootTableReference(BuiltInLootTables.EMPTY))
								.add(LootItem.lootTableItem(Items.PAPER))));

		add(TFEntities.DEATH_TOME.value(), TFLootTables.DEATH_TOME_BOOKS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(32)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(1, 10))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(8)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(11, 20))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(4)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(21, 30))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(1)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(31, 40))))));

		add(TFEntities.NAGA.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.NAGA_SCALE.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 11)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.NAGA_TROPHY.value().asItem()))));

		add(TFEntities.LICH.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.TWILIGHT_SCEPTER.value()))
								.add(LootItem.lootTableItem(TFItems.LIFEDRAIN_SCEPTER.value()))
								.add(LootItem.lootTableItem(TFItems.ZOMBIE_SCEPTER.value()))
								.add(LootItem.lootTableItem(TFItems.FORTIFICATION_SCEPTER.value())))
						.withPool(LootPool.lootPool()
								.setRolls(UniformGenerator.between(2, 4))
								.add(LootItem.lootTableItem(Items.GOLDEN_SWORD)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
								.add(LootItem.lootTableItem(Items.GOLDEN_HELMET)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
								.add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
								.add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
								.add(LootItem.lootTableItem(Items.GOLDEN_BOOTS)
										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.ENDER_PEARL)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.BONE)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 9)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.LICH_TROPHY.value().asItem()))));

		add(TFEntities.MINOSHROOM.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(UniformGenerator.between(2, 5))
								.add(LootItem.lootTableItem(TFItems.MEEF_STROGANOFF.value())
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.MINOSHROOM_TROPHY.value().asItem())))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.DIAMOND_MINOTAUR_AXE.value()))));

		add(TFEntities.HYDRA.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.HYDRA_CHOP.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 35)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.FIERY_BLOOD.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 2)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.HYDRA_TROPHY.value().asItem()))));

		add(TFEntities.UR_GHAST.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(4))
								.add(LootItem.lootTableItem(TFItems.CARMINITE.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(2))
								.add(LootItem.lootTableItem(TFItems.FIERY_TEARS.value())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.UR_GHAST_TROPHY.value().asItem()))));

		add(TFEntities.ALPHA_YETI.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ALPHA_YETI_FUR.value())
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.ICE_BOMB.value())
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.ALPHA_YETI_TROPHY.value().asItem()))));

		add(TFEntities.SNOW_QUEEN.value(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.TRIPLE_BOW.value()))
								.add(LootItem.lootTableItem(TFItems.SEEKER_BOW.value())))
						.withPool(LootPool.lootPool()
								.setRolls(UniformGenerator.between(1, 4))
								.add(LootItem.lootTableItem(Blocks.PACKED_ICE.asItem())
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(7)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
						.withPool(LootPool.lootPool()
								.setRolls(UniformGenerator.between(2, 5))
								.add(LootItem.lootTableItem(Items.SNOWBALL)
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(16)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFBlocks.SNOW_QUEEN_TROPHY.value().asItem()))));

		add(TFEntities.QUEST_RAM.value(), TFLootTables.QUESTING_RAM_REWARDS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TFItems.CRUMBLE_HORN.value())))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(Items.BUNDLE)
										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
											ListTag items = new ListTag();

											// Do NOT overstuff the bag.
											items.add(new ItemStack(TFBlocks.QUEST_RAM_TROPHY.value()).serializeNBT());
											items.add(new ItemStack(Blocks.COAL_BLOCK).serializeNBT());
											items.add(new ItemStack(Blocks.IRON_BLOCK).serializeNBT());
											items.add(new ItemStack(Blocks.COPPER_BLOCK).serializeNBT());
											items.add(new ItemStack(Blocks.LAPIS_BLOCK).serializeNBT());
											items.add(new ItemStack(Blocks.GOLD_BLOCK).serializeNBT());
											items.add(new ItemStack(Blocks.DIAMOND_BLOCK).serializeNBT());
											items.add(new ItemStack(Blocks.EMERALD_BLOCK).serializeNBT());

											nbt.put("Items", items);
										}))))));
	}

	public LootTable.Builder emptyLootTable() {
		return LootTable.lootTable();
	}

	public LootTable.Builder fromEntityLootTable(EntityType<?> parent) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootTableReference.lootTableReference(parent.getDefaultLootTable())));
	}

	private static LootTable.Builder sheepLootTableBuilderWithDrop(ItemLike wool) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(wool))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
	}

	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes() {
		return TFEntities.ENTITIES.getEntries().stream().map(DeferredHolder::value);
	}
}
