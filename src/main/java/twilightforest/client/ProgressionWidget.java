package twilightforest.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.util.Progression;
import twilightforest.util.Vec2i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ProgressionWidget {
    private static final ResourceLocation BAR_DOABLE_SPRITE = TwilightForestMod.prefix("bar_doable");
    private static final ResourceLocation BAR_FADED_SPRITE = TwilightForestMod.prefix("bar_faded");
    private static final ResourceLocation BAR_COMPLETE_SPRITE = TwilightForestMod.prefix("bar_complete");
    private static final ResourceLocation TITLE_BOX_SPRITE = new ResourceLocation("advancements/title_box");
    private static final int TEXTURE_SIZE = 256;
    private static final int OUTLINE_COLOR = FastColor.ARGB32.color(255, 0, 0, 0);
    private static final int OUTLINE_COLOR_FADED = FastColor.ARGB32.color(255, 100, 105, 100);
    private static final int INLINE_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
    private static final int INLINE_COLOR_FADED = FastColor.ARGB32.color(255, 174, 180, 174);
    private static final int INLINE_COLOR_COMPLETE = FastColor.ARGB32.color(255, 219, 162, 19);
    private static final int FONT_FADED = FastColor.ARGB32.color(255, 174, 180, 174);

    private static final int HEIGHT = 26;
    private static final int BOX_X = 0;
    private static final int BOX_WIDTH = 200;
    private static final int ICON_X = 8;
    private static final int ICON_Y = 5;
    private static final int ICON_SIZE = 26;
    private static final int GRID_SIZE = 26;
    private static final int X_ARROW_OFFSET = 18;
    private static final int Y_ARROW_OFFSET = 19;
    private static final int TITLE_PADDING_LEFT = 3;
    private static final int TITLE_PADDING_RIGHT = 5;
    private static final int TITLE_X = 32;
    private static final int TITLE_Y = 9;
    private static final int TITLE_MAX_WIDTH = 163;
    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
    private final ProgressionTab tab;
    private final FormattedCharSequence title;
    private final int width;
    private final int widthLocked;
    private final List<FormattedCharSequence> description;
    private final List<FormattedCharSequence> descriptionLocked;
    private final Minecraft minecraft;
    private final Progression progression;
    private final List<ProgressionWidget> parents = Lists.newArrayList();
    private final List<ProgressionWidget> children = Lists.newArrayList();
    @Nullable
    private AdvancementProgress progress;
    private final int x;
    private final int y;

    public ProgressionWidget(ProgressionTab tab, Minecraft mc, Progression progression) {
        this.tab = tab;
        this.minecraft = mc;
        this.title = Language.getInstance().getVisualOrder(mc.font.substrByWidth(progression.title(), 163));
        this.x = Mth.floor(progression.coordinates().x * GRID_SIZE);
        this.y = Mth.floor(progression.coordinates().z * GRID_SIZE);

        int width = 29 + mc.font.width(this.title);
        this.description = Language.getInstance()
                .getVisualOrder(this.findOptimalLines(ComponentUtils.mergeStyles(progression.description().copy(), Style.EMPTY.withColor(ChatFormatting.GREEN)), width));
        for(FormattedCharSequence formattedcharsequence : this.description) {
            width = Math.max(width, mc.font.width(formattedcharsequence));
        }
        this.width = width + 3 + 5;

        width = 29 + mc.font.width(this.title);
        this.descriptionLocked = Language.getInstance()
                .getVisualOrder(this.findOptimalLines(ComponentUtils.mergeStyles(progression.lockedDescription().copy(), Style.EMPTY.withColor(ChatFormatting.GRAY)), width));
        for(FormattedCharSequence formattedcharsequence : this.descriptionLocked) {
            width = Math.max(width, mc.font.width(formattedcharsequence));
        }
        this.widthLocked = width + 3 + 5;

        this.progression = progression;
    }

    private static float getMaxWidth(StringSplitter splitter, List<FormattedText> formattedTexts) {
        return (float)formattedTexts.stream().mapToDouble(splitter::stringWidth).max().orElse(0.0);
    }

    private List<FormattedText> findOptimalLines(Component component, int width) {
        StringSplitter stringsplitter = this.minecraft.font.getSplitter();
        List<FormattedText> list = null;
        float f = Float.MAX_VALUE;

        for(int i : TEST_SPLIT_OFFSETS) {
            List<FormattedText> list1 = stringsplitter.splitLines(component, width - i, Style.EMPTY);
            float f1 = Math.abs(getMaxWidth(stringsplitter, list1) - (float)width);
            if (f1 <= 10.0F) {
                return list1;
            }

            if (f1 < f) {
                f = f1;
                list = list1;
            }
        }

        return list;
    }

    public void drawConnectivity(GuiGraphics graphics, int x, int y, boolean dropShadow) {
        if (this.isVisible()) {
            for (ProgressionWidget widget : this.parents) {
                if (widget.isVisible()) {
                    List<Vec2i> points = new ArrayList<>();
                    points.add(this.progression.coordinates());
                    points.addAll(widget.progression.points());
                    points.add(widget.progression.coordinates());

                    for (int p = 1; p < points.size(); p++) {
                        int firstX = x + (points.get(p).x * GRID_SIZE) + (ICON_SIZE / 2) + 2;
                        int firstY = y + (points.get(p).z * GRID_SIZE) + (ICON_SIZE / 2);
                        int secondX = x + (points.get(p - 1).x * GRID_SIZE) + (ICON_SIZE / 2) + 2;
                        int secondY = y + (points.get(p - 1).z * GRID_SIZE) + (ICON_SIZE / 2);
                        int color = OUTLINE_COLOR;
                        if (dropShadow) {
                            if (!widget.isDone()) color = OUTLINE_COLOR_FADED;

                            if (firstX == secondX) {
                                if (firstY > secondY) {
                                    graphics.vLine(firstX - 1, secondY + 18, firstY, color);
                                    graphics.vLine(firstX + 1, secondY + 18, firstY, color);
                                } else {
                                    graphics.vLine(firstX - 1, secondY - 18, firstY, color);
                                    graphics.vLine(firstX + 1, secondY - 18, firstY, color);
                                }
                            } else {
                                graphics.hLine(secondX + 1 - 18, firstX - 1, secondY - 1, color);
                                graphics.hLine(secondX + 1 - 18, firstX - 1, secondY, color);
                                graphics.hLine(secondX + 1 - 18, firstX - 1, secondY + 1, color);
                                graphics.vLine(firstX - 1, secondY, firstY, color);
                                graphics.vLine(firstX + 1, secondY, firstY, color);
                            }
                        } else {
                            int offset = 0;
                            if (this.isDone()) {
                                color = INLINE_COLOR_COMPLETE;
                                offset = 14;
                            } else if (!widget.isDone()) {
                                color = INLINE_COLOR_FADED;
                                offset = 28;
                            } else color = INLINE_COLOR;

                            if (firstX == secondX) {
                                if (firstY > secondY) {
                                    graphics.vLine(firstX, secondY + 18, firstY, color);
                                    graphics.blit(ProgressionScreen.WINDOW_LOCATION, secondX - 5, secondY + Y_ARROW_OFFSET - 7, 0, 202 + offset + 7, 11, 7);
                                } else {
                                    graphics.vLine(firstX, secondY - 18, firstY, color);
                                    graphics.blit(ProgressionScreen.WINDOW_LOCATION, secondX - 5, secondY - Y_ARROW_OFFSET, 0, 202 + offset, 11, 7);
                                }
                            } else {
                                graphics.hLine(secondX - 18, firstX, secondY, color);
                                graphics.vLine(firstX, secondY, firstY, color);
                                graphics.blit(ProgressionScreen.WINDOW_LOCATION, secondX - X_ARROW_OFFSET, secondY - 5, 18, 202 + offset, 7, 11);
                            }
                        }
                    }
                }
            }

            for (ProgressionWidget widget : this.children) {
                widget.drawConnectivity(graphics, x, y, dropShadow);
            }
        }
    }

    public void draw(GuiGraphics graphics, int x, int y) {
        if (this.isVisible()) this.iconBlit(graphics, x, y, 0, this.isDone(), this.isDoable());
        for(ProgressionWidget widget : this.children) widget.draw(graphics, x, y);
    }

    public int getWidth() {
        return this.isDoable() ? this.width : this.widthLocked;
    }

    public void setProgress(AdvancementProgress progress) {
        this.progress = progress;
    }

    public boolean isDone() {
        return this.progress != null && this.progress.isDone();
    }

    public boolean isVisible() {
        if (this.isDone() || this.parents.isEmpty()) return true;
        for (ProgressionWidget widget : this.parents) {
            if (widget.isDoable()) return true;
        }
        return false;
    }

    public void addChild(ProgressionWidget widget) {
        this.children.add(widget);
    }

    public void drawHover(GuiGraphics graphics, int x, int y, float fade, int width, int height) {
        int textWidth = this.getWidth();
        List<FormattedCharSequence> description = this.isDoable() ? this.description : this.descriptionLocked;

        boolean flag = width + x + this.x + textWidth + 26 >= this.tab.getScreen().width;
        boolean flag1 = 113 - y - this.y - 26 <= 6 + description.size() * 9;
        RenderSystem.enableBlend();
        int l = y + this.y;
        int i1;
        if (flag) {
            i1 = x + this.x - textWidth + 26 + 6;
        } else {
            i1 = x + this.x;
        }

        int j1 = 32 + description.size() * 9;
        if (!description.isEmpty()) {
            if (flag1) {
                graphics.blitSprite(TITLE_BOX_SPRITE, i1, l + 26 - j1, textWidth, j1);
            } else {
                graphics.blitSprite(TITLE_BOX_SPRITE, i1, l, textWidth, j1);
            }
        }

        ResourceLocation bar = this.isDone() ? BAR_COMPLETE_SPRITE : this.isDoable() ? BAR_DOABLE_SPRITE : BAR_FADED_SPRITE;
        graphics.blitSprite(bar, 200, 26, 0, 0, i1, l, textWidth / 2, 26);
        graphics.blitSprite(bar, 200, 26, 200 - (textWidth / 2), 0, i1 + (textWidth / 2), l, textWidth / 2, 26);
        if (flag) {
            graphics.drawString(this.minecraft.font, this.title, i1 + 5, y + this.y + 9, -1);
        } else {
            graphics.drawString(this.minecraft.font, this.title, x + this.x + 32, y + this.y + 9, -1);
        }

        if (flag1) {
            for(int k1 = 0; k1 < description.size(); ++k1) {
                graphics.drawString(this.minecraft.font, description.get(k1), i1 + 5, l + 26 - j1 + 7 + k1 * 9, -5592406, false);
            }
        } else {
            for(int l1 = 0; l1 < description.size(); ++l1) {
                graphics.drawString(this.minecraft.font, description.get(l1), i1 + 5, y + this.y + 9 + 17 + l1 * 9, -5592406, false);
            }
        }

        this.iconBlit(graphics, x, y, 0, this.isDone(), this.isDoable());
    }

    public boolean isMouseOver(int x, int y, int width, int height) {
        if (this.isVisible()) {
            int i = x + this.x;
            int j = i + 26;
            int k = y + this.y;
            int l = k + 26;
            return width >= i && width <= j && height >= k && height <= l;
        } else {
            return false;
        }
    }

    public void attachToParent() {
        if (this.parents.isEmpty() && !this.progression.parents().isEmpty() && this.minecraft.level != null) {
            this.minecraft.level.registryAccess().registry(TFRegistries.Keys.PROGRESSIONS).ifPresent(progressions -> {
                List<ProgressionWidget> newParents = new ArrayList<>();
                for (ResourceKey<Progression> parent : this.progression.parents()) {
                    Progression parentProgression = progressions.get(parent);
                    if (parentProgression != null) {
                        ProgressionWidget widget = this.tab.getWidget(parentProgression);
                        if (widget == null) {
                            newParents.clear();
                            break;
                        } else newParents.add(widget);
                    }
                }
                if (!newParents.isEmpty()) {
                    this.parents.addAll(newParents);
                    for (ProgressionWidget parent : this.parents) parent.addChild(this);
                }
            });
        }
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public boolean isDoable() {
        if (this.isDone() || this.parents.isEmpty()) return true;
        for (ProgressionWidget widget : this.parents) {
            if (widget.isDone()) return true;
        }
        return false;
    }

    public void iconBlit(GuiGraphics graphics, int x, int y, int z, boolean done, boolean doable) {
        graphics.blit(ProgressionScreen.WINDOW_LOCATION, x + this.x + 3, y + this.y, z, done ? 25 : doable ? 51 : 77, 202, ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
        if (!this.isDoable()) {
            ResourceLocation restriction = this.progression.restriction();
            if (restriction != null) {
                TextureAtlasSprite sprite = ProgressionTextureManager.instance.getLayerSprite(restriction);
                graphics.blit(x + this.x + 3, y + this.y, 0, 26, 26, sprite);
            }
            graphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        }
        graphics.renderItem(this.progression.icon(), x + this.x + 8, y + this.y + 5);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
