package twilightforest.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.util.Progression;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgressionScreen extends Screen implements ClientAdvancements.Listener {
    public static final ResourceLocation WINDOW_LOCATION = TwilightForestMod.getGuiTexture("progression_background.png");
    public static final int WINDOW_WIDTH = 256;
    public static final int WINDOW_HEIGHT = 202;
    private static final int WINDOW_INSIDE_X = 9;
    private static final int WINDOW_INSIDE_Y = 18;
    public static final int WINDOW_INSIDE_WIDTH = 238;
    public static final int WINDOW_INSIDE_HEIGHT = 152;
    private static final int WINDOW_TITLE_X = 8;
    private static final int WINDOW_TITLE_Y = 6;
    public static final int BACKGROUND_TILE_WIDTH = 16;
    public static final int BACKGROUND_TILE_HEIGHT = 16;
    public static final int BACKGROUND_TILE_COUNT_X = 14;
    public static final int BACKGROUND_TILE_COUNT_Y = 9;
    private static final double SCROLL_SPEED = 16.0;
    private static final Component VERY_SAD_LABEL = Component.translatable("advancements.sad_label");
    private static final Component NO_ADVANCEMENTS_LABEL = Component.translatable("advancements.empty");
    private static final Component TITLE = /*Component.translatable("gui.advancements")*/ Component.literal("Progression");//FIXME
    private final ClientAdvancements advancements;
    private final Map<Progression, ProgressionTab> tabs = Maps.newLinkedHashMap();
    @Nullable
    private Registry<Progression> registry = null;
    @Nullable
    private ProgressionTab selectedTab;
    private boolean isScrolling;
    private static int tabPage, maxPages;

    public ProgressionScreen(ClientAdvancements advancements) {
        super(GameNarrator.NO_TITLE);
        this.advancements = advancements;
    }

    @Override
    protected void init() {
        if (this.minecraft != null && this.minecraft.level != null) {
            this.registry = this.minecraft.level.registryAccess().registry(TFRegistries.Keys.PROGRESSIONS).orElse(null);
        } else this.registry = null;

        this.tabs.clear();
        this.selectedTab = null;

        if (this.registry != null && this.minecraft != null) {
            List<Progression> children = new ArrayList<>();
            this.registry.entrySet().forEach(resourceKeyProgressionEntry -> {
                Progression progression = resourceKeyProgressionEntry.getValue();
                if (progression.parents().isEmpty()) {
                    AdvancementHolder holder = this.advancements.get(progression.advancement());
                    if (holder != null) {
                        ProgressionTab tab = ProgressionTab.create(this.minecraft, this, this.tabs.size(), new AdvancementNode(holder, null), progression);
                        if (tab != null) this.tabs.put(progression, tab);
                    }
                } else children.add(progression);
            });
            for (Progression progression : children) {
                ProgressionTab tab = this.getTab(progression);
                if (tab != null) tab.addProgression(progression);
            }
        }

        this.advancements.setListener(this);

        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            ProgressionTab tab = this.tabs.values().iterator().next();
            this.advancements.setSelectedTab(tab.getRootNode().holder(), true);
        } else {
            this.advancements.setSelectedTab(this.selectedTab == null ? null : this.selectedTab.getRootNode().holder(), true);
        }

        if (this.tabs.size() > AdvancementTabType.MAX_TABS) {
            int guiLeft = (this.width - WINDOW_WIDTH) / 2;
            int guiTop = (this.height - WINDOW_HEIGHT) / 2;
            addRenderableWidget(Button.builder(Component.literal("<"), b -> tabPage = Math.max(tabPage - 1, 0         ))
                    .pos(guiLeft, guiTop - 50).size(20, 20).build());
            addRenderableWidget(Button.builder(Component.literal(">"), b -> tabPage = Math.min(tabPage + 1, maxPages))
                    .pos(guiLeft + WINDOW_WIDTH - 20, guiTop - 50).size(20, 20).build());
            maxPages = this.tabs.size() / AdvancementTabType.MAX_TABS;
        }
    }

    @Override
    public void removed() {
        this.advancements.setListener(null);
        ClientPacketListener clientpacketlistener = null;
        if (this.minecraft != null) clientpacketlistener = this.minecraft.getConnection();
        if (clientpacketlistener != null) clientpacketlistener.send(ServerboundSeenAdvancementsPacket.closedScreen());
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == 0) {
            int i = (this.width - WINDOW_WIDTH) / 2;
            int j = (this.height - WINDOW_HEIGHT) / 2;

            for (ProgressionTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(i, j, x, y)) {
                    this.advancements.setSelectedTab(tab.getRootNode().holder(), true);
                    break;
                }
            }
        }

        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean keyPressed(int x, int y, int button) {
        if (this.minecraft != null) {
            if (this.minecraft.options.keyAdvancements.matches(x, y)) {
                this.minecraft.setScreen(null);
                this.minecraft.mouseHandler.grabMouse();
                return true;
            }
        }
        return super.keyPressed(x, y, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int i = (this.width - WINDOW_WIDTH) / 2;
        int j = (this.height - WINDOW_HEIGHT) / 2;
        if (maxPages != 0) {
            Component page = Component.literal(String.format("%d / %d", tabPage + 1, maxPages + 1));
            int width = this.font.width(page);
            guiGraphics.drawString(this.font, page.getVisualOrderText(), i + (252 / 2) - (width / 2), j - 44, -1);
        }
        this.renderInside(guiGraphics, i, j);
        this.renderWindow(guiGraphics, i, j);
        this.renderTooltips(guiGraphics, mouseX, mouseY, i, j);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double xDistance, double yDistance) {
        if (button != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(xDistance, yDistance);
            }

            return true;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double xScroll, double yScroll) {
        if (this.selectedTab != null) {
            this.selectedTab.scroll(xScroll * SCROLL_SPEED, yScroll * SCROLL_SPEED);
            return true;
        } else {
            return false;
        }
    }

    private void renderInside(GuiGraphics guiGraphics, int width, int height) {
        if (this.selectedTab == null) {
            guiGraphics.fill(width + WINDOW_INSIDE_X, height + WINDOW_INSIDE_Y, width + WINDOW_INSIDE_X + WINDOW_INSIDE_WIDTH, height + WINDOW_INSIDE_Y + WINDOW_INSIDE_HEIGHT, -16777216);
            int i = width + 9 + 117;
            guiGraphics.drawCenteredString(this.font, NO_ADVANCEMENTS_LABEL, i, height + WINDOW_INSIDE_Y + 56 - WINDOW_INSIDE_X / 2, -1);
            guiGraphics.drawCenteredString(this.font, VERY_SAD_LABEL, i, height + WINDOW_INSIDE_Y + 56 + WINDOW_INSIDE_X * 3, -1);
        } else {
            this.selectedTab.drawContents(guiGraphics, width + WINDOW_INSIDE_X, height + WINDOW_INSIDE_Y);
        }
    }

    public void renderWindow(GuiGraphics guiGraphics, int width, int height) {
        RenderSystem.enableBlend();
        guiGraphics.blit(WINDOW_LOCATION, width, height, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (this.tabs.size() > 1) {
            for(ProgressionTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage) tab.drawTab(guiGraphics, width, height, tab == this.selectedTab);
            }

            for(ProgressionTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage) tab.drawIcon(guiGraphics, width, height);
            }
        }

        guiGraphics.drawString(this.font, TITLE, width + WINDOW_TITLE_X, height + WINDOW_TITLE_Y, 4210752, false);
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
        if (this.selectedTab != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)(width + WINDOW_INSIDE_X), (float)(height + WINDOW_INSIDE_Y), 400.0F);
            RenderSystem.enableDepthTest();
            this.selectedTab.drawTooltips(guiGraphics, mouseX - width - WINDOW_INSIDE_X, mouseY - height - WINDOW_INSIDE_Y, width, height);
            RenderSystem.disableDepthTest();
            guiGraphics.pose().popPose();
        }

        if (this.tabs.size() > 1) {
            for(ProgressionTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(width, height, mouseX, mouseY)) {
                    guiGraphics.renderTooltip(this.font, tab.getTitle(), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onAddAdvancementRoot(AdvancementNode node) {
    }

    @Override
    public void onRemoveAdvancementRoot(AdvancementNode node) {
    }

    @Override
    public void onAddAdvancementTask(AdvancementNode node) {
    }

    @Override
    public void onRemoveAdvancementTask(AdvancementNode node) {
    }

    @Override
    public void onUpdateAdvancementProgress(AdvancementNode node, AdvancementProgress progress) {
        ProgressionWidget widget = this.getAdvancementWidget(node);
        if (widget != null) widget.setProgress(progress);
    }

    @Override
    public void onSelectedTabChanged(@Nullable AdvancementHolder advancementHolder) {
        if (advancementHolder != null) {
            Progression progression = this.getProgression(advancementHolder);
            if (progression != null) this.selectedTab = this.tabs.get(progression);
        } else {
            if (this.tabs.size() > 0) this.selectedTab = this.tabs.values().iterator().next();
            else this.selectedTab = null;
        }
    }

    @Override
    public void onAdvancementsCleared() {
        this.tabs.clear();
        this.selectedTab = null;
    }

    @Nullable
    public ProgressionWidget getAdvancementWidget(AdvancementNode node) {
        Progression progression = this.getProgression(node);
        Progression tabProgression = this.getProgression(node.root());
        if (tabProgression != null && progression != null) {
            ProgressionTab tab = this.getTab(tabProgression);
            return tab == null ? null : tab.getWidget(progression);
        }
        return null;
    }

    @Nullable
    private ProgressionTab getTab(@Nullable Progression progression) {
        if (this.registry != null && progression != null) {
            while (progression != null) {
                if (progression.parents().isEmpty()) return this.tabs.get(progression);
                progression = this.registry.get(progression.parents().get(0));
            }
        }
        return this.tabs.get(progression);
    }

    @Nullable
    private Progression getProgression(AdvancementNode node) {
        return getProgression(node.holder());
    }

    @Nullable
    private Progression getProgression(AdvancementHolder holder) {
        if (this.registry == null) return null;
        for (Map.Entry<ResourceKey<Progression>, Progression> entry : registry.entrySet()) {
            if (entry.getValue().advancement().equals(holder.id())) return entry.getValue();
        }
        return null;
    }
}
