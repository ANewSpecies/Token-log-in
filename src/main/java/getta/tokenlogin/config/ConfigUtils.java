package getta.tokenlogin.config;

import net.minecraft.client.MinecraftClient;

import java.io.File;

public class ConfigUtils {

    public static File getConfigDirectory() {

        return new File(MinecraftClient.getInstance().runDirectory, "config");
    }
}
