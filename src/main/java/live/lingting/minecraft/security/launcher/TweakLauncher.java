package live.lingting.minecraft.security.launcher;

import live.lingting.minecraft.security.Reflection;
import live.lingting.minecraft.security.enums.LaunchType;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

/**
 * @author lingting 2024-06-28 11:39
 */
public class TweakLauncher extends DefaultLauncher implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        String mcVersion = getMcVersion();
        if (mcVersion == null) {
            log.warn("Failed to get minecraft version.");
            return;
        }
        init(gameDir.toPath(), mcVersion, LaunchType.FORGE);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {

    }

    @Override
    public String getLaunchTarget() {
        return "";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

    String getMcVersion() {
        try {
            // 1.6~1.7.10
            // 1.6: https://github.com/MinecraftForge/FML/blob/16launch/common/cpw/mods/fml/relauncher/FMLInjectionData.java#L32
            // 1.7.10: https://github.com/MinecraftForge/MinecraftForge/blob/1.7.10/fml/src/main/java/cpw/mods/fml/relauncher/FMLInjectionData.java#L32
            return Reflection.get(
                    "cpw.mods.fml.relauncher.FMLInjectionData",
                    "mccversion"
            );
        } catch (Exception ignored) {
        }

        try {
            // 1.8
            // https://github.com/MinecraftForge/FML/blob/1.8/src/main/java/net/minecraftforge/fml/relauncher/FMLInjectionData.java#L32
            return Reflection.get(
                    "net.minecraftforge.fml.relauncher.FMLInjectionData",
                    "mccversion"
            );
        } catch (Exception ignored) {
        }

        try {
            // 1.8.8~1.12.2
            // 1.8.8: https://github.com/MinecraftForge/MinecraftForge/blob/1.8.8/src/main/java/net/minecraftforge/common/ForgeVersion.java#L42
            // 1.12.2: https://github.com/MinecraftForge/MinecraftForge/blob/1.12.x/src/main/java/net/minecraftforge/common/ForgeVersion.java#L64
            return Reflection.get(
                    "net.minecraftforge.common.ForgeVersion",
                    "mcVersion");
        } catch (Exception ignored) {
        }
        return null;
    }
}
