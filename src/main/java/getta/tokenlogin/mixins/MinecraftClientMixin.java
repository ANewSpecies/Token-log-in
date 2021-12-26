package getta.tokenlogin.mixins;

import getta.tokenlogin.interfaces.IMixinMinecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IMixinMinecraft {

    @Mutable
    @Shadow @Final private Session session;

    public Session getSession() {
        return this.session;
    }

    @Override
    public void setSession(Session paramSession) {
        this.session = paramSession;
    }
}
