package getta.tokenlogin.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import getta.tokenlogin.SessionStoreScreen;
import getta.tokenlogin.config.Config;
import getta.tokenlogin.config.ConfigUtils;
import getta.tokenlogin.interfaces.IMixinMinecraft;
import getta.tokenlogin.utils.SessionUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(MultiplayerScreen.class)
public class MultiplayerGuiMixin extends Screen {

    protected MultiplayerGuiMixin(Text title) {
        super(title);
    }

    private TextFieldWidget tokenText;

    @Inject(method = "init", at = @At("RETURN"))
    private void createButtons(CallbackInfo ci) {

        this.tokenText = new TextFieldWidget(this.textRenderer, 5 + textRenderer.getWidth("User: WWWWWWWWWWWWWWWW"), 15, this.width - 500 + textRenderer.getWidth("User: WWWWWWWWWWWWWWWW"), textRenderer.fontHeight + 5, Text.of(""));
        this.tokenText.setMaxLength(1200);
        this.children.add(tokenText);

        this.addButton(new ButtonWidget(this.width - 170, 12, 80, 20, Text.of("Login"), (buttonWidget) -> {

        actionPerformed(1);
        }));

        this.addButton(new ButtonWidget(this.width - 85, 12, 80, 20, Text.of("Restore"), (buttonWidget) -> {

            if(SessionUtils.oldSession != null) {
                ((IMixinMinecraft)this.client).setSession(SessionUtils.oldSession);
            }
        }));

        this.addButton(new ButtonWidget(this.width / 2 + 4 + 151, this.height - 28, 75, 20, Text.of("Sessions"), (buttonWidget) -> {

            actionPerformed(2);
        }));
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.tokenText.render(matrices, mouseX, mouseY, delta);
        this.textRenderer.drawWithShadow(matrices, String.format("User: %s", new Object[] { this.client.getSession().getUsername() }), 5, 19, -1);
    }

    private void actionPerformed(int button){

        String sessionId;
        String[] token;
        String uuid;
        JsonElement rawJson;
        JsonArray json;
        String name;
        Session session;


        switch (button) {

            case  1:

                if(SessionUtils.oldSession == null) SessionUtils.oldSession = ((IMixinMinecraft)this.client).getSession();

                sessionId = this.tokenText.getText();
                if(sessionId.length() <= 10) break;

                token = sessionId.split(":");
                if(token.length != 2) break;

                uuid = token[1];
                rawJson = null;

                try {

                    rawJson = (new JsonParser()).parse(new InputStreamReader((new URL("https://api.mojang.com/user/profiles/" + uuid + "/names")).openConnection().getInputStream()));
                } catch (IOException e) {

                    e.printStackTrace();
                }

                if(rawJson == null || !rawJson.isJsonArray()) break;

                json = rawJson.getAsJsonArray();
                name = json.get(json.size() - 1).getAsJsonObject().get("name").getAsString();
                session = new Session(name, uuid, token[0], "mojang");

                ((IMixinMinecraft)this.client).setSession(session);

                Config.saveSession(session);

                break;

            case 2:

                ArrayList<Session> sessions;

                try {

                    client.openScreen(new SessionStoreScreen(client.currentScreen, Config.loadSessions().getSessions()));
                } catch (NullPointerException e){

                    sessions = new ArrayList<>();
                    client.openScreen(new SessionStoreScreen(client.currentScreen, sessions));
                }
        }

    }
}