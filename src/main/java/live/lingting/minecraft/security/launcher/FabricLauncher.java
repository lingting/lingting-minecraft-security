package live.lingting.minecraft.security.launcher;

import live.lingting.minecraft.security.Reflection;
import live.lingting.minecraft.security.enums.LaunchType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

/**
 * @author lingting 2024-06-28 14:17
 */
public class FabricLauncher extends DefaultLauncher implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Path gameDir = FabricLoader.getInstance().getGameDir();
        String mcVersion = getMcVersion();
        if (mcVersion == null) {
            log.warn("Minecraft version not found");
            return;
        }
        init(gameDir, mcVersion, LaunchType.FABRIC);
    }

    private String getMcVersion() {
        Object instance = null;
        try {
            // Fabric
            instance = Reflection.get("net.fabricmc.loader.impl.FabricLoaderImpl", "INSTANCE");
        } catch (Exception ignored) {

        }
        try {
            // Quilt
            instance = Reflection.get("org.quiltmc.loader.impl.QuiltLoaderImpl", "INSTANCE");
        } catch (Exception ignored) {

        }
        if (instance != null) {
            try {
                Object provider = Reflection.method(instance, "getGameProvider");
                return Reflection.method(provider, "getNormalizedGameVersion");
            } catch (Exception ignore) {
            }
        }
        return null;
    }
}
