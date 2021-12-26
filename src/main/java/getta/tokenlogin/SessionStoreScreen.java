package getta.tokenlogin;

import getta.tokenlogin.interfaces.IMixinMinecraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SessionStoreScreen extends Screen {

    private Screen screen;
    private ButtonWidget buttonBack;
    private ArrayList<Session> sessions;

    public SessionStoreScreen(Screen screen, ArrayList<Session> sessions) {
        super(new TranslatableText("Session Store Screen"));
        this.screen = screen;
        this.sessions = sessions;
    }

    protected  void init() {
        super.init();

        this.buttonBack = (ButtonWidget) this.addButton(new ButtonWidget(5, this.height - 30, 70, 20, Text.of("Back"), (buttonWidget) -> {

            MinecraftClient.getInstance().openScreen(new MultiplayerScreen(this.screen));
        }));

        if(this.sessions != null && !this.sessions.isEmpty()) {

            int xQuantity = 0;
            int yQuantity = 0;

            for(Session session : this.sessions) {

                this.addButton(new ButtonWidget(30 + xQuantity, 50 + yQuantity, 75, 20, Text.of(session.getUsername()), (buttonWidget) -> {

                    ((IMixinMinecraft)this.client).setSession(session);
                }));
                yQuantity+=25;

                if( 30 + yQuantity >= this.height - 100) {
                    yQuantity = 0;
                    xQuantity += 93;
                }
            }
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
        this.textRenderer.drawWithShadow(matrices, String.format("User: %s", new Object[] { this.client.getSession().getUsername() }), 5, 19, -1);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
