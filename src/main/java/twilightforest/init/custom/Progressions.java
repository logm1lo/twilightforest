package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.util.Progression;
import twilightforest.util.TFAdvancements;

public class Progressions {
    public static final Codec<Holder<Progression>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.PROGRESSIONS, Progression.CODEC, false);

    public static final ResourceKey<Progression> PROGRESS_ROOT = makeKey(TwilightForestMod.prefix("progress_root"));
    public static final ResourceKey<Progression> PROGRESS_NAGA = makeKey(TwilightForestMod.prefix("progress_naga"));
    public static final ResourceKey<Progression> PROGRESS_LICH = makeKey(TwilightForestMod.prefix("progress_lich"));
    public static final ResourceKey<Progression> PROGRESS_TROPHY_PEDESTAL = makeKey(TwilightForestMod.prefix("progress_trophy_pedestal"));
    public static final ResourceKey<Progression> PROGRESS_PHANTOMS = makeKey(TwilightForestMod.prefix("progress_phantoms"));
    public static final ResourceKey<Progression> PROGRESS_GHAST_TRAP = makeKey(TwilightForestMod.prefix("progress_ghast_trap"));
    public static final ResourceKey<Progression> PROGRESS_UR_GHAST = makeKey(TwilightForestMod.prefix("progress_ur_ghast"));
    public static final ResourceKey<Progression> PROGRESS_MINOSHROOM = makeKey(TwilightForestMod.prefix("progress_minoshroom"));
    public static final ResourceKey<Progression> PROGRESS_HYDRA = makeKey(TwilightForestMod.prefix("progress_hydra"));
    public static final ResourceKey<Progression> PROGRESS_YETI = makeKey(TwilightForestMod.prefix("progress_yeti"));
    public static final ResourceKey<Progression> PROGRESS_SNOW_QUEEN = makeKey(TwilightForestMod.prefix("progress_snow_queen"));
    public static final ResourceKey<Progression> PROGRESS_MERGE = makeKey(TwilightForestMod.prefix("progress_merge"));
    public static final ResourceKey<Progression> BEANSTALK = makeKey(TwilightForestMod.prefix("beanstalk"));
    public static final ResourceKey<Progression> GIANTS = makeKey(TwilightForestMod.prefix("giants"));
    public static final ResourceKey<Progression> PROGRESS_TROLL = makeKey(TwilightForestMod.prefix("progress_troll"));
    public static final ResourceKey<Progression> PROGRESS_FINAL = makeKey(TwilightForestMod.prefix("progress_final"));

    private static ResourceKey<Progression> makeKey(ResourceLocation name) {
        return ResourceKey.create(TFRegistries.Keys.PROGRESSIONS, name);
    }

    public static void bootstrap(BootstapContext<Progression> context) {
        register(context, PROGRESS_ROOT, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_ROOT,
                        TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE,
                        Component.translatable("advancement.twilightforest.progress_root"),
                        Component.translatable("advancement.twilightforest.progress_root.desc"),
                        Component.empty())
                .coordinates(0, 0)
                .build());

        register(context, PROGRESS_NAGA, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_NAGA,
                        TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(),
                        Component.translatable("advancement.twilightforest.kill_naga"),
                        Component.translatable("advancement.twilightforest.kill_naga.desc",
                                Component.translatable(TFEntities.NAGA.get().getDescriptionId()),
                                Component.translatable(TFItems.NAGA_SCALE.get().getDescriptionId())),
                        Component.empty())
                .coordinates(2, 0)
                .addParent(PROGRESS_ROOT)
                .build());

        register(context, PROGRESS_LICH, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_LICH,
                        TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(),
                        Component.translatable("advancement.twilightforest.kill_lich"),
                        Component.translatable("advancement.twilightforest.kill_lich.desc",
                                Component.translatable(TFEntities.LICH.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.kill_lich.desc.locked"))
                .coordinates(4, 0)
                .restriction(TwilightForestMod.prefix("structure_lock"))
                .addParent(PROGRESS_NAGA)
                .build());

        register(context, PROGRESS_TROPHY_PEDESTAL, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_TROPHY_PEDESTAL,
                        TFBlocks.TROPHY_PEDESTAL.get(),
                        Component.translatable("advancement.twilightforest.progress_trophy_pedestal"),
                        Component.translatable("advancement.twilightforest.progress_trophy_pedestal.desc"),
                        Component.translatable("advancement.twilightforest.progress_trophy_pedestal.desc.locked"))
                .coordinates(4, 2)
                .restriction(TwilightForestMod.prefix("darkness"))
                .addParent(PROGRESS_LICH)
                .build());

        register(context, PROGRESS_PHANTOMS, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_KNIGHTS,
                        TFBlocks.KNIGHT_PHANTOM_TROPHY.get(),
                        Component.translatable("advancement.twilightforest.progress_knights"),
                        Component.translatable("advancement.twilightforest.progress_knights.desc"),
                        Component.translatable("advancement.twilightforest.progress_knights.desc.locked"))
                .coordinates(6, 2)
                .restriction(TwilightForestMod.prefix("structure_lock"))
                .addParent(PROGRESS_TROPHY_PEDESTAL)
                .build());

        register(context, PROGRESS_GHAST_TRAP, Progression.Builder
                .progression(
                        TFAdvancements.GHAST_TRAP,
                        TFBlocks.GHAST_TRAP.get(),
                        Component.translatable("advancement.twilightforest.ghast_trap"),
                        Component.translatable("advancement.twilightforest.ghast_trap.desc",
                                Component.translatable(TFEntities.CARMINITE_GHASTLING.get().getDescriptionId()),
                                Component.translatable(TFBlocks.GHAST_TRAP.get().getDescriptionId()),
                                Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.ghast_trap.desc.locked"))
                .coordinates(8, 2)
                .restriction(TwilightForestMod.prefix("darkness"))
                .addParent(PROGRESS_PHANTOMS)
                .build());

        register(context, PROGRESS_UR_GHAST, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_UR_GHAST,
                        TFBlocks.UR_GHAST_TROPHY.get(),
                        Component.translatable("advancement.twilightforest.progress_ur_ghast"),
                        Component.translatable("advancement.twilightforest.progress_ur_ghast.desc",
                                Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.progress_ur_ghast.desc.locked"))
                .coordinates(10, 2)
                .addParent(PROGRESS_GHAST_TRAP)
                .build());

        register(context, PROGRESS_MINOSHROOM, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_LABYRINTH,
                        TFItems.MEEF_STROGANOFF.get(),
                        Component.translatable("advancement.twilightforest.progress_labyrinth"),
                        Component.translatable("advancement.twilightforest.progress_labyrinth.desc"),
                        Component.translatable("advancement.twilightforest.progress_labyrinth.desc.locked"))
                .coordinates(6, -2)
                .restriction(TwilightForestMod.prefix("hunger"))
                .addParent(PROGRESS_LICH)
                .build());

        register(context, PROGRESS_HYDRA, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_HYDRA,
                        TFBlocks.HYDRA_TROPHY.get(),
                        Component.translatable("advancement.twilightforest.kill_hydra"),
                        Component.translatable("advancement.twilightforest.kill_hydra.desc",
                                Component.translatable(TFEntities.HYDRA.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.kill_hydra.desc.locked"))
                .coordinates(10, -2)
                .restriction(TwilightForestMod.prefix("fire"))
                .addParent(PROGRESS_MINOSHROOM)
                .build());

        register(context, PROGRESS_YETI, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_YETI,TFItems.ALPHA_YETI_FUR.get(),
                        Component.translatable("advancement.twilightforest.progress_yeti"),
                        Component.translatable("advancement.twilightforest.progress_yeti.desc",
                                Component.translatable(TFEntities.ALPHA_YETI.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.progress_yeti.desc.locked"))
                .coordinates(6, 0)
                .restriction(TwilightForestMod.prefix("frost"))
                .addParent(PROGRESS_LICH)
                .build());

        register(context, PROGRESS_SNOW_QUEEN, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_GLACIER, TFBlocks.SNOW_QUEEN_TROPHY.get(),
                        Component.translatable("advancement.twilightforest.progress_glacier"),
                        Component.translatable("advancement.twilightforest.progress_glacier.desc",
                                Component.translatable(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.progress_glacier.desc.locked"))
                .coordinates(8, 0)
                .restriction(TwilightForestMod.prefix("frost"))
                .addParent(PROGRESS_YETI)
                .build());

        register(context, PROGRESS_MERGE, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_MERGE,
                        TFBlocks.UBEROUS_SOIL.get(),
                        Component.translatable("advancement.twilightforest.progress_merge"),
                        Component.translatable("advancement.twilightforest.progress_merge.desc",
                                Component.translatable(TFEntities.HYDRA.get().getDescriptionId()),
                                Component.translatable(TFEntities.UR_GHAST.get().getDescriptionId()),
                                Component.translatable(TFEntities.SNOW_QUEEN.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.progress_merge.desc.locked"))
                .coordinates(10, 0)
                .restriction(TwilightForestMod.prefix("big_rain"))
                .addParent(PROGRESS_SNOW_QUEEN)
                .addParent(PROGRESS_HYDRA)
                .addParent(PROGRESS_UR_GHAST)
                .build());

        register(context, BEANSTALK, Progression.Builder
                .progression(
                        TFAdvancements.BEANSTALK,
                        TFBlocks.HUGE_STALK.get(),
                        Component.translatable("advancement.twilightforest.beanstalk"),
                        Component.translatable("advancement.twilightforest.beanstalk.desc",
                                Component.translatable(TFItems.MAGIC_BEANS.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.beanstalk.desc.locked"))
                .coordinates(12, 0)
                .addParent(PROGRESS_MERGE)
                .build());

        register(context, GIANTS, Progression.Builder
                .progression(
                        TFAdvancements.GIANTS,
                        TFItems.GIANT_PICKAXE.get(),
                        Component.translatable("advancement.twilightforest.giants"),
                        Component.translatable("advancement.twilightforest.giants.desc",
                                Component.translatable(TFEntities.GIANT_MINER.get().getDescriptionId()),
                                Component.translatable(TFItems.GIANT_PICKAXE.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.giants.desc.locked"))
                .coordinates(14, 0)
                .addParent(BEANSTALK)
                .build());

        register(context, PROGRESS_TROLL, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESS_TROLL,
                        TFItems.LAMP_OF_CINDERS.get(),
                        Component.translatable("advancement.twilightforest.progress_troll"),
                        Component.translatable("advancement.twilightforest.progress_troll.desc",
                                Component.translatable(TFItems.LAMP_OF_CINDERS.get().getDescriptionId())),
                        Component.translatable("advancement.twilightforest.progress_troll.desc.locked"))
                .coordinates(16, 0)
                .addParent(GIANTS)
                .build());

        register(context, PROGRESS_FINAL, Progression.Builder
                .progression(
                        TFAdvancements.PROGRESSION_END,
                        Items.STRUCTURE_VOID,
                        Component.translatable("advancement.twilightforest.progression_end"),
                        Component.translatable("advancement.twilightforest.progression_end.desc"),
                        Component.translatable("advancement.twilightforest.progression_end.desc.locked"))
                .coordinates(18, 0)
                .restriction(TwilightForestMod.prefix("big_rain"))
                .addParent(PROGRESS_TROLL)
                .build());
    }

    private static void register(BootstapContext<Progression> context, ResourceKey<Progression> key, Progression progression) {
        context.register(key, progression);
    }
}
