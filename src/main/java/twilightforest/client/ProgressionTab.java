package twilightforest.client;

import com.google.common.collect.Maps;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.util.Progression;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ProgressionTab {
    private final Minecraft minecraft;
    private final ProgressionScreen screen;
    private final AdvancementTabType type;
    private final int index;
    private final AdvancementNode rootNode;
    private final ItemStack icon;
    private final Component title;
    private final ProgressionWidget root;
    public final Map<Progression, ProgressionWidget> widgets = Maps.newLinkedHashMap();
    private double scrollX;
    private double scrollY;
    public int minX = Integer.MAX_VALUE;
    public int minY = Integer.MAX_VALUE;
    public int maxX = Integer.MIN_VALUE;
    public int maxY = Integer.MIN_VALUE;
    private float fade;
    private boolean centered;
    private int page;

    public ProgressionTab(Minecraft mc, ProgressionScreen screen, AdvancementTabType tabType, int index, AdvancementNode adv, Progression progression) {
        this.minecraft = mc;
        this.screen = screen;
        this.type = tabType;
        this.index = index;
        this.rootNode = adv;
        this.icon = progression.icon();
        this.title = progression.title();
        this.root = new ProgressionWidget(this, mc, progression);
        this.addWidget(this.root, progression);
    }

    public ProgressionTab(Minecraft mc, ProgressionScreen screen, AdvancementTabType type, int index, int page, AdvancementNode adv, Progression progression) {
        this(mc, screen, type, index, adv, progression);
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public AdvancementTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public AdvancementNode getRootNode() {
        return this.rootNode;
    }

    public Component getTitle() {
        return this.title;
    }

    public void drawTab(GuiGraphics graphics, int width, int height, boolean selected) {
        this.type.draw(graphics, width, height, selected, this.index);
    }

    public void drawIcon(GuiGraphics graphics, int width, int height) {
        this.type.drawIcon(graphics, width, height, this.index, this.icon);
    }

    public void drawContents(GuiGraphics graphics, int width, int height) {
        if (!this.centered) {
            for (ProgressionWidget widget : this.widgets.values()) {
                if (widget.isVisible()) {
                    int i = widget.getX();
                    int j = i + 28;
                    int k = widget.getY();
                    int l = k + 27;
                    this.minX = Math.min(this.minX, i);
                    this.maxX = Math.max(this.maxX, j);
                    this.minY = Math.min(this.minY, k);
                    this.maxY = Math.max(this.maxY, l);
                }

                this.scrollX = (ProgressionScreen.WINDOW_INSIDE_WIDTH * 0.5D) - (double) (this.maxX + this.minX) / 2.0D;
                this.scrollY = (ProgressionScreen.WINDOW_INSIDE_HEIGHT * 0.5D) - (double) (this.maxY + this.minY) / 2.0D;
                this.centered = true;
            }
        }

        graphics.enableScissor(width, height, width + ProgressionScreen.WINDOW_INSIDE_WIDTH, height + ProgressionScreen.WINDOW_INSIDE_HEIGHT);
        graphics.pose().pushPose();
        graphics.pose().translate((float)width, (float)height, 0.0F);
        ResourceLocation resourcelocation = new ResourceLocation(TwilightForestMod.ID, "textures/block/mazestone_large_brick.png"); // FIXME
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for(int i1 = -1; i1 <= ProgressionScreen.BACKGROUND_TILE_COUNT_X + 1; ++i1) {
            for(int j1 = -1; j1 <= ProgressionScreen.BACKGROUND_TILE_COUNT_Y + 1; ++j1) {
                graphics.blit(resourcelocation, k + ProgressionScreen.BACKGROUND_TILE_WIDTH * i1, l + ProgressionScreen.BACKGROUND_TILE_HEIGHT * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        this.root.drawConnectivity(graphics, i, j, true);
        this.root.drawConnectivity(graphics, i, j, false);
        this.root.draw(graphics, i, j);
        graphics.pose().popPose();
        graphics.disableScissor();
    }

    public void drawTooltips(GuiGraphics graphics, int mouseX, int mouseY, int width, int height) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, -200.0F);
        graphics.fill(0, 0, ProgressionScreen.WINDOW_INSIDE_WIDTH, ProgressionScreen.WINDOW_INSIDE_HEIGHT, Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < ProgressionScreen.WINDOW_INSIDE_WIDTH && mouseY > 0 && mouseY < ProgressionScreen.WINDOW_INSIDE_HEIGHT) {
            for(ProgressionWidget advancementwidget : this.widgets.values()) {
                if (advancementwidget.isMouseOver(i, j, mouseX, mouseY)) {
                    flag = true;
                    advancementwidget.drawHover(graphics, i, j, this.fade, width, height);
                    break;
                }
            }
        }

        graphics.pose().popPose();
        if (flag) {
            this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }
    }

    public boolean isMouseOver(int width, int height, double mouseX, double mouseY) {
        return this.type.isMouseOver(width, height, this.index, mouseX, mouseY);
    }

    @Nullable
    public static ProgressionTab create(Minecraft minecraft, ProgressionScreen screen, int index, AdvancementNode adv, Progression progression) {
        for (AdvancementTabType advancementtabtype : AdvancementTabType.values()) {
            if ((index % AdvancementTabType.MAX_TABS) < advancementtabtype.getMax()) {
                return new ProgressionTab(minecraft, screen, advancementtabtype, index % AdvancementTabType.MAX_TABS, index / AdvancementTabType.MAX_TABS, adv, progression);
            }
            index -= advancementtabtype.getMax();
        }
        return null;
    }

    public void scroll(double scrollX, double scrollY) {
        if (this.maxX - this.minX > ProgressionScreen.WINDOW_INSIDE_WIDTH) {
            this.scrollX = Mth.clamp(this.scrollX + scrollX, -(this.maxX - ProgressionScreen.WINDOW_INSIDE_WIDTH), 0.0);
        }

        if (this.maxY - this.minY > ProgressionScreen.WINDOW_INSIDE_HEIGHT) {
            this.scrollY = Mth.clamp(this.scrollY + scrollY, -(this.maxY - ProgressionScreen.WINDOW_INSIDE_HEIGHT), 0.0);
        }
    }

    public void addProgression(Progression progression) {
        this.addWidget(new ProgressionWidget(this, this.minecraft, progression), progression);
    }

    private void addWidget(ProgressionWidget widget, Progression progression) {
        this.widgets.put(progression, widget);
        for(ProgressionWidget progressionWidget : this.widgets.values()) progressionWidget.attachToParent();
    }

    @Nullable
    public ProgressionWidget getWidget(Progression progression) {
        return this.widgets.get(progression);
    }

    public ProgressionScreen getScreen() {
        return this.screen;
    }
}
