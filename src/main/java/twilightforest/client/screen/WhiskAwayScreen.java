package twilightforest.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.function.BooleanSupplier;

public class WhiskAwayScreen extends ReceivingLevelScreen {
	public static final ResourceLocation MENU_BACKGROUND = TwilightForestMod.prefix("textures/block/mazestone_brick.png");
	private static final Component ENTERING = Component.translatable("twilightforest.loading.title.enter");
	private static final Component LEAVING = Component.translatable("twilightforest.loading.title.leave");

	private final boolean whiskingAway;

	public WhiskAwayScreen(BooleanSupplier levelReceived, boolean whiskingAway) {
		super(levelReceived, Reason.OTHER);
		this.whiskingAway = whiskingAway;
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		for (Renderable renderable : this.renderables) renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

		pGuiGraphics.drawCenteredString(this.font, whiskingAway ? ENTERING : LEAVING, this.width / 2, this.height / 2 - 50, 16777215);
	}

	@Override
	public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBlurredBackground(pPartialTick);
		renderMenuBackgroundTexture(pGuiGraphics,  MENU_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.height);
		this.renderTransparentBackground(pGuiGraphics);
	}
}
