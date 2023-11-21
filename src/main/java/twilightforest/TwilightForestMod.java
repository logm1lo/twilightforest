package twilightforest;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.advancements.TFAdvancements;
import twilightforest.capabilities.CapabilityList;
import twilightforest.client.ClientInitiator;
import twilightforest.command.TFCommand;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.dispenser.TFDispenserBehaviors;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateGamerulePacket;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.util.Restriction;
import twilightforest.util.TFRemapper;
import twilightforest.util.WoodPalette;
import twilightforest.world.components.BiomeGrassColors;
import twilightforest.world.components.biomesources.LandmarkBiomeSource;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkgenerators.TwilightChunkGenerator;
import twilightforest.world.components.chunkgenerators.ControlledSpawnsCache;

import java.util.Locale;
import java.util.function.Consumer;

@Mod(TwilightForestMod.ID)
public class TwilightForestMod {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/model/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";
	// odd one out, as armor textures are a stringy mess at present
	public static final String ARMOR_DIR = ID + ":textures/armor/";

	public static final GameRules.Key<GameRules.BooleanValue> ENFORCED_PROGRESSION_RULE = GameRules.register("tfEnforcedProgression", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true, (server, enforced) -> TFPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateGamerulePacket(enforced.get())))); //Putting it in UPDATES since other world stuff is here

	public static final Logger LOGGER = LogManager.getLogger(ID);

	private static final Rarity rarity = Rarity.create("TWILIGHT", ChatFormatting.DARK_GREEN);

	@SuppressWarnings("removal") // addGenericListener is still required for AttachCapabilitiesEvent according to https://neoforged.net/news/20.2eventbus-changes/
	public TwilightForestMod(IEventBus bus, Dist dist) {
		{
			final Pair<TFConfig.Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFConfig.Common::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
			TFConfig.COMMON_CONFIG = specPair.getLeft();
		}
		{
			final Pair<TFConfig.Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(TFConfig.Client::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
			TFConfig.CLIENT_CONFIG = specPair.getLeft();
		}

		if (dist.isClient()) {
			ClientInitiator.call();
		}
		NeoForge.EVENT_BUS.addListener(this::registerCommands);
		NeoForge.EVENT_BUS.addGenericListener(Level.class, CapabilityList::attachLevelCapability);
		NeoForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityList::attachEntityCapability);
		NeoForge.EVENT_BUS.addListener(Stalactite::reloadStalactites);
		NeoForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) ControlledSpawnsCache::reload);

		TFItems.ITEMS.register(bus);
		TFStats.STATS.register(bus);
		TFBlocks.BLOCKS.register(bus);
		TFPOITypes.POIS.register(bus);
		TFSounds.SOUNDS.register(bus);
		TFLoot.FUNCTIONS.register(bus);
		TFLoot.CONDITIONS.register(bus);
		TFEntities.ENTITIES.register(bus);
		TFFeatures.FEATURES.register(bus);
		TFCreativeTabs.TABS.register(bus);
		TFEntities.SPAWN_EGGS.register(bus);
		TFMenuTypes.CONTAINERS.register(bus);
		TFRecipes.RECIPE_TYPES.register(bus);
		TFMobEffects.MOB_EFFECTS.register(bus);
		Enforcements.ENFORCEMENTS.register(bus);
		TFEnchantments.ENCHANTMENTS.register(bus);
		TFRecipes.RECIPE_SERIALIZERS.register(bus);
		TFParticleType.PARTICLE_TYPES.register(bus);
		TFBlockEntities.BLOCK_ENTITIES.register(bus);
		TFLootModifiers.LOOT_MODIFIERS.register(bus);
		TFBannerPatterns.BANNER_PATTERNS.register(bus);
		TFStructureTypes.STRUCTURE_TYPES.register(bus);
		TFFeatureModifiers.TRUNK_PLACERS.register(bus);
		BiomeLayerTypes.BIOME_LAYER_TYPES.register(bus);
		TFFeatureModifiers.FOLIAGE_PLACERS.register(bus);
		TFFeatureModifiers.TREE_DECORATORS.register(bus);
		TinyBirdVariants.TINY_BIRD_VARIANTS.register(bus);
		TFFeatureModifiers.PLACEMENT_MODIFIERS.register(bus);
		DwarfRabbitVariants.DWARF_RABBIT_VARIANTS.register(bus);
		TFStructureProcessors.STRUCTURE_PROCESSORS.register(bus);
		TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(bus);

		TFRemapper.addRegistryAliases();

		bus.addListener(this::init);
		bus.addListener(this::sendIMCs);
		bus.addListener(this::registerExtraStuff);
		bus.addListener(this::createNewRegistries);
		bus.addListener(this::setRegistriesForDatapack);
		bus.addListener(CapabilityList::registerCapabilities);

		if (ModList.get().isLoaded("curios")) {
//			Bindings.getForgeBus().value().addListener(CuriosCompat::keepCurios);
//			bus.addListener(CuriosCompat::registerCurioRenderers);
//			bus.addListener(CuriosCompat::registerCurioLayers);
		}

		BiomeGrassColors.init();
	}

	public void createNewRegistries(NewRegistryEvent event) {
		event.register(TFRegistries.BIOME_LAYER_TYPE);
		event.register(TFRegistries.DWARF_RABBIT_VARIANT);
		event.register(TFRegistries.ENFORCEMENT);
		event.register(TFRegistries.TINY_BIRD_VARIANT);
	}

	public void setRegistriesForDatapack(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC);
		event.dataPackRegistry(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC, Restriction.CODEC);
		event.dataPackRegistry(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC, MagicPaintingVariant.CODEC);
	}

	public void registerExtraStuff(RegisterEvent evt) {
		if (evt.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
			Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);
			Registry.register(BuiltInRegistries.BIOME_SOURCE, TwilightForestMod.prefix("landmarks"), LandmarkBiomeSource.CODEC);
		} else if (evt.getRegistryKey().equals(Registries.CHUNK_GENERATOR)) {
			Registry.register(BuiltInRegistries.CHUNK_GENERATOR, TwilightForestMod.prefix("structure_locating_wrapper"), TwilightChunkGenerator.CODEC);
		}
	}

	public void sendIMCs(InterModEnqueueEvent evt) {
		if (ModList.get().isLoaded("theoneprobe")) {
			//InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopCompat::new);
		}
	}

	public void init(FMLCommonSetupEvent evt) {
		TFPacketHandler.init();
		TFAdvancements.init();

		evt.enqueueWork(() -> {
			TFSounds.registerParrotSounds();
			TFDispenserBehaviors.init();
			TFStats.init();

			CauldronInteraction.WATER.put(TFItems.ARCTIC_HELMET.value(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_CHESTPLATE.value(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_LEGGINGS.value(), CauldronInteraction.DYED_ITEM);
			CauldronInteraction.WATER.put(TFItems.ARCTIC_BOOTS.value(), CauldronInteraction.DYED_ITEM);

			AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
			AxeItem.STRIPPABLES.put(TFBlocks.TWILIGHT_OAK_LOG.value(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.CANOPY_LOG.value(), TFBlocks.STRIPPED_CANOPY_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.MANGROVE_LOG.value(), TFBlocks.STRIPPED_MANGROVE_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.DARK_LOG.value(), TFBlocks.STRIPPED_DARK_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.TIME_LOG.value(), TFBlocks.STRIPPED_TIME_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.TRANSFORMATION_LOG.value(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.MINING_LOG.value(), TFBlocks.STRIPPED_MINING_LOG.value());
			AxeItem.STRIPPABLES.put(TFBlocks.SORTING_LOG.value(), TFBlocks.STRIPPED_SORTING_LOG.value());

			AxeItem.STRIPPABLES.put(TFBlocks.TWILIGHT_OAK_WOOD.value(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.CANOPY_WOOD.value(), TFBlocks.STRIPPED_CANOPY_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.MANGROVE_WOOD.value(), TFBlocks.STRIPPED_MANGROVE_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.DARK_WOOD.value(), TFBlocks.STRIPPED_DARK_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.TIME_WOOD.value(), TFBlocks.STRIPPED_TIME_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.TRANSFORMATION_WOOD.value(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.MINING_WOOD.value(), TFBlocks.STRIPPED_MINING_WOOD.value());
			AxeItem.STRIPPABLES.put(TFBlocks.SORTING_WOOD.value(), TFBlocks.STRIPPED_SORTING_WOOD.value());

			FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;

			pot.addPlant(TFBlocks.TWILIGHT_OAK_SAPLING.getId(), TFBlocks.POTTED_TWILIGHT_OAK_SAPLING);
			pot.addPlant(TFBlocks.CANOPY_SAPLING.getId(), TFBlocks.POTTED_CANOPY_SAPLING);
			pot.addPlant(TFBlocks.MANGROVE_SAPLING.getId(), TFBlocks.POTTED_MANGROVE_SAPLING);
			pot.addPlant(TFBlocks.DARKWOOD_SAPLING.getId(), TFBlocks.POTTED_DARKWOOD_SAPLING);
			pot.addPlant(TFBlocks.HOLLOW_OAK_SAPLING.getId(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING);
			pot.addPlant(TFBlocks.RAINBOW_OAK_SAPLING.getId(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING);
			pot.addPlant(TFBlocks.TIME_SAPLING.getId(), TFBlocks.POTTED_TIME_SAPLING);
			pot.addPlant(TFBlocks.TRANSFORMATION_SAPLING.getId(), TFBlocks.POTTED_TRANSFORMATION_SAPLING);
			pot.addPlant(TFBlocks.MINING_SAPLING.getId(), TFBlocks.POTTED_MINING_SAPLING);
			pot.addPlant(TFBlocks.SORTING_SAPLING.getId(), TFBlocks.POTTED_SORTING_SAPLING);
			pot.addPlant(TFBlocks.MAYAPPLE.getId(), TFBlocks.POTTED_MAYAPPLE);
			pot.addPlant(TFBlocks.FIDDLEHEAD.getId(), TFBlocks.POTTED_FIDDLEHEAD);
			pot.addPlant(TFBlocks.MUSHGLOOM.getId(), TFBlocks.POTTED_MUSHGLOOM);
			pot.addPlant(TFBlocks.BROWN_THORNS.getId(), TFBlocks.POTTED_THORN);
			pot.addPlant(TFBlocks.GREEN_THORNS.getId(), TFBlocks.POTTED_GREEN_THORN);
			pot.addPlant(TFBlocks.BURNT_THORNS.getId(), TFBlocks.POTTED_DEAD_THORN);

			FireBlock fireblock = (FireBlock) Blocks.FIRE;
			fireblock.setFlammable(TFBlocks.ROOT_BLOCK.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.ARCTIC_FUR_BLOCK.value(), 20, 20);
			fireblock.setFlammable(TFBlocks.LIVEROOT_BLOCK.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.EMPTY_CANOPY_BOOKSHELF.value(), 30, 20);
			fireblock.setFlammable(TFBlocks.DEATH_TOME_SPAWNER.value(), 30, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TWILIGHT_OAK_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.CANOPY_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.CANOPY_BOOKSHELF.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.MANGROVE_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MANGROVE_ROOT.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.DARK_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.DARK_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.TIME_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TIME_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.TRANSFORMATION_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.MINING_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.MINING_GATE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_WOOD.value(), 5, 5);
			fireblock.setFlammable(TFBlocks.SORTING_PLANKS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_SLAB.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_STAIRS.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_FENCE.value(), 5, 20);
			fireblock.setFlammable(TFBlocks.SORTING_GATE.value(), 5, 20);

			ComposterBlock.add(0.1F, TFBlocks.FALLEN_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.CANOPY_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.CLOVER_PATCH.value());
			ComposterBlock.add(0.3F, TFBlocks.DARK_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.FIDDLEHEAD.value());
			ComposterBlock.add(0.3F, TFBlocks.HEDGE.value());
			ComposterBlock.add(0.3F, TFBlocks.MANGROVE_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.MAYAPPLE.value());
			ComposterBlock.add(0.3F, TFBlocks.MINING_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.TWILIGHT_OAK_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.RAINBOW_OAK_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.ROOT_STRAND.value());
			ComposterBlock.add(0.3F, TFBlocks.SORTING_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.THORN_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.TIME_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.TRANSFORMATION_LEAVES.value());
			ComposterBlock.add(0.3F, TFBlocks.TWILIGHT_OAK_SAPLING.value());
			ComposterBlock.add(0.3F, TFBlocks.CANOPY_SAPLING.value());
			ComposterBlock.add(0.3F, TFBlocks.MANGROVE_SAPLING.value());
			ComposterBlock.add(0.3F, TFBlocks.DARKWOOD_SAPLING.value());
			ComposterBlock.add(0.3F, TFBlocks.RAINBOW_OAK_SAPLING.value());
			ComposterBlock.add(0.5F, TFBlocks.BEANSTALK_LEAVES.value());
			ComposterBlock.add(0.5F, TFBlocks.MOSS_PATCH.value());
			ComposterBlock.add(0.5F, TFBlocks.ROOT_BLOCK.value());
			ComposterBlock.add(0.5F, TFBlocks.THORN_ROSE.value());
			ComposterBlock.add(0.5F, TFBlocks.TROLLVIDR.value());
			ComposterBlock.add(0.5F, TFBlocks.HOLLOW_OAK_SAPLING.value());
			ComposterBlock.add(0.5F, TFBlocks.TIME_SAPLING.value());
			ComposterBlock.add(0.5F, TFBlocks.TRANSFORMATION_SAPLING.value());
			ComposterBlock.add(0.5F, TFBlocks.MINING_SAPLING.value());
			ComposterBlock.add(0.5F, TFBlocks.SORTING_SAPLING.value());
			ComposterBlock.add(0.5F, TFBlocks.TORCHBERRY_PLANT.value());
			ComposterBlock.add(0.65F, TFBlocks.HUGE_MUSHGLOOM_STEM.value());
			ComposterBlock.add(0.65F, TFBlocks.HUGE_WATER_LILY.value());
			ComposterBlock.add(0.65F, TFBlocks.LIVEROOT_BLOCK.value());
			ComposterBlock.add(0.65F, TFBlocks.MUSHGLOOM.value());
			ComposterBlock.add(0.65F, TFBlocks.UBEROUS_SOIL.value());
			ComposterBlock.add(0.65F, TFBlocks.HUGE_STALK.value());
			ComposterBlock.add(0.65F, TFBlocks.UNRIPE_TROLLBER.value());
			ComposterBlock.add(0.65F, TFBlocks.TROLLBER.value());
			ComposterBlock.add(0.85F, TFBlocks.HUGE_LILY_PAD.value());
			ComposterBlock.add(0.85F, TFBlocks.HUGE_MUSHGLOOM.value());

			//eh, we'll do items here too
			ComposterBlock.add(0.3F, TFItems.TORCHBERRIES.value());
			ComposterBlock.add(0.5F, TFItems.LIVEROOT.value());
			ComposterBlock.add(0.65F, TFItems.MAZE_WAFER.value());
			ComposterBlock.add(0.85F, TFItems.EXPERIMENT_115.value());
			ComposterBlock.add(0.85F, TFItems.MAGIC_BEANS.value());

			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.COBBLESTONE, TFBlocks.GIANT_COBBLESTONE.value().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LOG, TFBlocks.GIANT_LOG.value().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OAK_LEAVES, TFBlocks.GIANT_LEAVES.value().asItem());
			GiantToolGroupingModifier.CONVERSIONS.put(Blocks.OBSIDIAN, TFBlocks.GIANT_OBSIDIAN.value().asItem());
		});
	}

	public void registerCommands(RegisterCommandsEvent event) {
		TFCommand.register(event.getDispatcher());
	}

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(ID, name.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation getModelTexture(String name) {
		return new ResourceLocation(ID, MODEL_DIR + name);
	}

	public static ResourceLocation getGuiTexture(String name) {
		return new ResourceLocation(ID, GUI_DIR + name);
	}

	public static ResourceLocation getEnvTexture(String name) {
		return new ResourceLocation(ID, ENVIRO_DIR + name);
	}

	public static Rarity getRarity() {
		return rarity;
	}
}
