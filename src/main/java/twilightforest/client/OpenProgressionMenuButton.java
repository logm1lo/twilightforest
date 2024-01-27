package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import twilightforest.init.TFItems;

@SuppressWarnings("unused")
public class OpenProgressionMenuButton extends Button {
    public static final ItemStack ICON = TFItems.MAGIC_MAP_FOCUS.asItem().getDefaultInstance();
    private static final int WIDTH = 20;

    public OpenProgressionMenuButton() {
        super(0, 0, WIDTH, WIDTH, Component.empty(), button -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) mc.setScreen(new ProgressionScreen(mc.player.connection.getAdvancements()));
        }, DEFAULT_NARRATION);
    }

    @Override
    public void renderString(GuiGraphics graphics, Font font, int color) {
        graphics.renderItem(ICON, getX() + 2, getY() + 2);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class OpenConfigButtonHandler {
        @SubscribeEvent
        public static void onGuiInit(ScreenEvent.Init.Pre event) {
            if (event.getScreen() instanceof PauseScreen screen) {
                Button button = new OpenProgressionMenuButton();
                button.setPosition(screen.width / 2 - 104 - WIDTH, screen.height / 4 + 26);
                event.addListener(button);
            }
        }
    }
}