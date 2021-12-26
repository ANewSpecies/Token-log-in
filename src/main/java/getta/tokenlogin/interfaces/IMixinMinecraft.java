package getta.tokenlogin.interfaces;

import net.minecraft.client.util.Session;

public interface IMixinMinecraft {
    Session getSession();

    void setSession(Session paramSession);
}
