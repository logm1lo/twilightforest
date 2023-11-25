package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.entity.newmodels.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.newmodels.*;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMenuTypes;
import twilightforest.util.TFWoodTypes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = TwilightForestMod.ID)
public class TFClientSetup {

	public static boolean optifinePresent = false;

	public static void init() {
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		TFShaders.init(busMod);
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TwilightForestMod.ID)
	public static class ForgeEvents {

		private static boolean firstTitleScreenShown = false;

		@SubscribeEvent
		public static void showOptifineWarning(ScreenEvent.Init.Post event) {
			if (firstTitleScreenShown || !(event.getScreen() instanceof TitleScreen)) return;

			// Registering this resource listener earlier than the main screen will cause a crash
			// Yes, crashing happens if registered to RegisterClientReloadListenersEvent
			if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager resourceManager) {
				resourceManager.registerReloadListener(ISTER.INSTANCE.get());
				TwilightForestMod.LOGGER.debug("Registered ISTER listener");
			}

			if (optifinePresent && !TFConfig.CLIENT_CONFIG.disableOptifineNagScreen.get()) {
				Minecraft.getInstance().setScreen(new OptifineWarningScreen(event.getScreen()));
			}

			firstTitleScreenShown = true;
		}
	}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}

        TFBlockEntities.registerTileEntityRenders();
        TFMenuTypes.renderScreens();

        evt.enqueueWork(() -> {
            Sheets.addWoodType(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.CANOPY_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.MANGROVE_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.DARK_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.TIME_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.TRANSFORMATION_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.MINING_WOOD_TYPE);
            Sheets.addWoodType(TFWoodTypes.SORTING_WOOD_TYPE);
        });
    }

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(JappaPackReloadListener.INSTANCE);
		MagicPaintingTextureManager.instance = new MagicPaintingTextureManager(Minecraft.getInstance().getTextureManager());
		event.registerReloadListener(MagicPaintingTextureManager.instance);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		BooleanSupplier jappa = JappaPackReloadListener.INSTANCE.uncachedJappaPackCheck();
		event.registerEntityRenderer(TFEntities.BOAT.value(), m -> new TwilightBoatRenderer(m, false));
		event.registerEntityRenderer(TFEntities.CHEST_BOAT.value(), m -> new TwilightBoatRenderer(m, true));
		event.registerEntityRenderer(TFEntities.BOAR.value(), m -> !jappa.getAsBoolean() ? new BoarRenderer(m, new BoarModel<>(m.bakeLayer(TFModelLayers.NEW_BOAR))) : new NewBoarRenderer(m, new NewBoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		event.registerEntityRenderer(TFEntities.BIGHORN_SHEEP.value(), m -> new BighornRenderer(m, new NewBighornModel<>(m.bakeLayer(!jappa.getAsBoolean() ? TFModelLayers.NEW_BIGHORN_SHEEP : TFModelLayers.BIGHORN_SHEEP)), new BighornFurLayer(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP_FUR)), 0.7F));
		event.registerEntityRenderer(TFEntities.DEER.value(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new DeerModel(m.bakeLayer(TFModelLayers.NEW_DEER)) : new NewDeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		event.registerEntityRenderer(TFEntities.REDCAP.value(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new RedcapModel<>(m.bakeLayer(TFModelLayers.NEW_REDCAP)) : new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		event.registerEntityRenderer(TFEntities.SKELETON_DRUID.value(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), 0.5F, "skeletondruid.png"));
		event.registerEntityRenderer(TFEntities.HOSTILE_WOLF.value(), HostileWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.WRAITH.value(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		event.registerEntityRenderer(TFEntities.HYDRA.value(), m -> !jappa.getAsBoolean() ? new HydraRenderer(m, new HydraModel(m.bakeLayer(TFModelLayers.NEW_HYDRA)), 4.0F) : new NewHydraRenderer(m, new NewHydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		event.registerEntityRenderer(TFEntities.LICH.value(), m -> new LichRenderer(m, new LichModel(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		event.registerEntityRenderer(TFEntities.PENGUIN.value(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		event.registerEntityRenderer(TFEntities.LICH_MINION.value(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TFEntities.LOYAL_ZOMBIE.value(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TFEntities.TINY_BIRD.value(), m -> !jappa.getAsBoolean() ? new TinyBirdRenderer(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.NEW_TINY_BIRD)), 0.3F) : new NewTinyBirdRenderer(m, new NewTinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		event.registerEntityRenderer(TFEntities.SQUIRREL.value(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new SquirrelModel(m.bakeLayer(TFModelLayers.NEW_SQUIRREL)) : new NewSquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		event.registerEntityRenderer(TFEntities.DWARF_RABBIT.value(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		event.registerEntityRenderer(TFEntities.RAVEN.value(), m -> new BirdRenderer<>(m, !jappa.getAsBoolean() ? new RavenModel(m.bakeLayer(TFModelLayers.NEW_RAVEN)) : new NewRavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		event.registerEntityRenderer(TFEntities.QUEST_RAM.value(), m -> !jappa.getAsBoolean() ? new QuestRamRenderer(m, new QuestRamModel(m.bakeLayer(TFModelLayers.NEW_QUEST_RAM))) : new NewQuestRamRenderer(m, new NewQuestRamModel(m.bakeLayer(TFModelLayers.QUEST_RAM))));
		event.registerEntityRenderer(TFEntities.KOBOLD.value(), m -> !jappa.getAsBoolean() ? new KoboldRenderer(m, new KoboldModel(m.bakeLayer(TFModelLayers.NEW_KOBOLD)), 0.4F, "kobold.png") : new NewKoboldRenderer(m, new NewKoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		//event.registerEntityRenderer(TFEntities.BOGGARD.value(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		event.registerEntityRenderer(TFEntities.MOSQUITO_SWARM.value(), MosquitoSwarmRenderer::new);
		event.registerEntityRenderer(TFEntities.DEATH_TOME.value(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		event.registerEntityRenderer(TFEntities.MINOTAUR.value(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new MinotaurModel(m.bakeLayer(TFModelLayers.NEW_MINOTAUR)) : new NewMinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		event.registerEntityRenderer(TFEntities.MINOSHROOM.value(), m -> !jappa.getAsBoolean() ? new MinoshroomRenderer(m, new MinoshroomModel(m.bakeLayer(TFModelLayers.NEW_MINOSHROOM)), 0.625F) : new NewMinoshroomRenderer(m, new NewMinoshroomModel(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		event.registerEntityRenderer(TFEntities.FIRE_BEETLE.value(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new FireBeetleModel(m.bakeLayer(TFModelLayers.NEW_FIRE_BEETLE)) : new NewFireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		event.registerEntityRenderer(TFEntities.SLIME_BEETLE.value(), m -> !jappa.getAsBoolean() ? new SlimeBeetleRenderer(m, new SlimeBeetleModel(m.bakeLayer(TFModelLayers.NEW_SLIME_BEETLE)), 0.6F) : new NewSlimeBeetleRenderer(m, new NewSlimeBeetleModel(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), 0.6F));
		event.registerEntityRenderer(TFEntities.PINCH_BEETLE.value(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new PinchBeetleModel(m.bakeLayer(TFModelLayers.NEW_PINCH_BEETLE)) : new NewPinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		event.registerEntityRenderer(TFEntities.MIST_WOLF.value(), MistWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTLING.value(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		event.registerEntityRenderer(TFEntities.CARMINITE_GOLEM.value(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		event.registerEntityRenderer(TFEntities.TOWERWOOD_BORER.value(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTGUARD.value(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		event.registerEntityRenderer(TFEntities.UR_GHAST.value(), m -> !jappa.getAsBoolean() ? new UrGhastRenderer(m, new UrGhastModel(m.bakeLayer(TFModelLayers.NEW_UR_GHAST)), 8.0F, 24F) : new NewUrGhastRenderer(m, new NewUrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24F));
		event.registerEntityRenderer(TFEntities.BLOCKCHAIN_GOBLIN.value(), m -> !jappa.getAsBoolean() ? new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.NEW_BLOCKCHAIN_GOBLIN)), 0.4F) : new NewBlockChainGoblinRenderer<>(m, new NewBlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		event.registerEntityRenderer(TFEntities.UPPER_GOBLIN_KNIGHT.value(), m -> !jappa.getAsBoolean() ? new UpperGoblinKnightRenderer(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.NEW_UPPER_GOBLIN_KNIGHT)), 0.625F) : new NewUpperGoblinKnightRenderer(m, new NewUpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		event.registerEntityRenderer(TFEntities.LOWER_GOBLIN_KNIGHT.value(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.NEW_LOWER_GOBLIN_KNIGHT)) : new NewLowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		event.registerEntityRenderer(TFEntities.HELMET_CRAB.value(), m -> new TFGenericMobRenderer<>(m, !jappa.getAsBoolean() ? new HelmetCrabModel(m.bakeLayer(TFModelLayers.NEW_HELMET_CRAB)) : new NewHelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
		event.registerEntityRenderer(TFEntities.KNIGHT_PHANTOM.value(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		event.registerEntityRenderer(TFEntities.NAGA.value(), m -> !jappa.getAsBoolean() ? new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NEW_NAGA)), 1.45F) : new NewNagaRenderer<>(m, new NewNagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		event.registerEntityRenderer(TFEntities.SWARM_SPIDER.value(), SwarmSpiderRenderer::new);
		event.registerEntityRenderer(TFEntities.KING_SPIDER.value(), KingSpiderRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_BROODLING.value(), CarminiteBroodlingRenderer::new);
		event.registerEntityRenderer(TFEntities.HEDGE_SPIDER.value(), HedgeSpiderRenderer::new);
		event.registerEntityRenderer(TFEntities.REDCAP_SAPPER.value(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new RedcapModel<>(m.bakeLayer(TFModelLayers.NEW_REDCAP)) : new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new NewRedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		event.registerEntityRenderer(TFEntities.MAZE_SLIME.value(), m -> new MazeSlimeRenderer(m, 0.625F));
		event.registerEntityRenderer(TFEntities.YETI.value(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		event.registerEntityRenderer(TFEntities.PROTECTION_BOX.value(), ProtectionBoxRenderer::new);
		event.registerEntityRenderer(TFEntities.MAGIC_PAINTING.value(), MagicPaintingRenderer::new);
		event.registerEntityRenderer(TFEntities.ALPHA_YETI.value(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		event.registerEntityRenderer(TFEntities.WINTER_WOLF.value(), WinterWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.SNOW_GUARDIAN.value(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		event.registerEntityRenderer(TFEntities.STABLE_ICE_CORE.value(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		event.registerEntityRenderer(TFEntities.UNSTABLE_ICE_CORE.value(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		event.registerEntityRenderer(TFEntities.SNOW_QUEEN.value(), m -> !jappa.getAsBoolean() ? new SnowQueenRenderer(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.NEW_SNOW_QUEEN))) : new NewSnowQueenRenderer(m, new NewSnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		event.registerEntityRenderer(TFEntities.TROLL.value(), m -> new TFBipedRenderer<>(m, !jappa.getAsBoolean() ? new TrollModel(m.bakeLayer(TFModelLayers.NEW_TROLL)) : new NewTrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		event.registerEntityRenderer(TFEntities.GIANT_MINER.value(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ARMORED_GIANT.value(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_CRYSTAL.value(), IceCrystalRenderer::new);
		event.registerEntityRenderer(TFEntities.CHAIN_BLOCK.value(), BlockChainRenderer::new);
		event.registerEntityRenderer(TFEntities.CUBE_OF_ANNIHILATION.value(), CubeOfAnnihilationRenderer::new);
		event.registerEntityRenderer(TFEntities.HARBINGER_CUBE.value(), HarbingerCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.ADHERENT.value(), AdherentRenderer::new);
		event.registerEntityRenderer(TFEntities.ROVING_CUBE.value(), RovingCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.RISING_ZOMBIE.value(), m -> new TFBipedRenderer<>(m, new RisingZombieModel(m.bakeLayer(TFModelLayers.RISING_ZOMBIE)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		event.registerEntityRenderer(TFEntities.PLATEAU_BOSS.value(), NoopRenderer::new);

		// projectiles
		event.registerEntityRenderer(TFEntities.NATURE_BOLT.value(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH_BOLT.value(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/item/twilight_orb.png")));
		event.registerEntityRenderer(TFEntities.WAND_BOLT.value(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/item/twilight_orb.png")));
		event.registerEntityRenderer(TFEntities.TOME_BOLT.value(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA_MORTAR.value(), HydraMortarRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIME_BLOB.value(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.MOONWORM_SHOT.value(), MoonwormShotRenderer::new);
		event.registerEntityRenderer(TFEntities.CHARM_EFFECT.value(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH_BOMB.value(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_WEP.value(), ThrownWepRenderer::new);
		event.registerEntityRenderer(TFEntities.FALLING_ICE.value(), FallingIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_ICE.value(), ThrownIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_BLOCK.value(), ThrownBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_SNOWBALL.value(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIDER.value(), SlideBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.SEEKER_ARROW.value(), DefaultArrowRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_ARROW.value(), DefaultArrowRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("deprecation")
	public static class BakedMultiPartRenderers {

		private static final Map<ResourceLocation, LazyLoadedValue<EntityRenderer<?>>> renderers = new HashMap<>();

		public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
			BooleanSupplier jappa = JappaPackReloadListener.INSTANCE.uncachedJappaPackCheck();
			renderers.put(TFPart.RENDERER, new LazyLoadedValue<>(() -> new NoopRenderer<>(context)));
			renderers.put(HydraHead.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new HydraHeadRenderer(context) : new NewHydraHeadRenderer(context)));
			renderers.put(HydraNeck.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new HydraNeckRenderer(context) : new NewHydraNeckRenderer(context)));
			renderers.put(SnowQueenIceShield.RENDERER, new LazyLoadedValue<>(() -> new SnowQueenIceShieldLayer<>(context)));
			renderers.put(NagaSegment.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new NagaSegmentRenderer<>(context) : new NewNagaSegmentRenderer<>(context)));
		}

		public static EntityRenderer<?> lookup(ResourceLocation location) {
			return renderers.get(location).get();
		}
	}

	@Nullable
	private static Field field_EntityRenderersEvent$AddLayers_renderers;

	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
		if (field_EntityRenderersEvent$AddLayers_renderers == null) {
			try {
				field_EntityRenderersEvent$AddLayers_renderers = EntityRenderersEvent.AddLayers.class.getDeclaredField("renderers");
				field_EntityRenderersEvent$AddLayers_renderers.setAccessible(true);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		if (field_EntityRenderersEvent$AddLayers_renderers != null) {
			event.getSkins().forEach(renderer -> {
				LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
				attachRenderLayers(Objects.requireNonNull(skin));
			});
			try {
				((Map<EntityType<?>, EntityRenderer<?>>) field_EntityRenderersEvent$AddLayers_renderers.get(event)).values().stream().
						filter(LivingEntityRenderer.class::isInstance).map(LivingEntityRenderer.class::cast).forEach(TFClientSetup::attachRenderLayers);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}
}
