package live.lingting.minecraft.security.launcher;

import com.google.gson.JsonObject;
import cpw.mods.modlauncher.ArgumentHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import live.lingting.minecraft.security.Reflection;
import live.lingting.minecraft.security.enums.LaunchType;
import live.lingting.minecraft.security.properties.ModProperties;
import live.lingting.minecraft.security.util.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author lingting 2024-06-28 14:05
 */
public class ModLauncher extends DefaultLauncher implements ITransformationService {

    @Override
    public @NotNull String name() {
        return ModProperties.name;
    }

    @Override
    public void initialize(IEnvironment environment) {
        log.debug("initialize by ModLauncher");
        Optional<Path> minecraftPath = environment.getProperty(IEnvironment.Keys.GAMEDIR.get());
        if (!minecraftPath.isPresent()) {
            log.warn("Minecraft path not found");
            return;
        }
        String minecraftVersion = getMinecraftVersion();
        if (minecraftVersion == null) {
            log.warn("Minecraft version not found");
            return;
        }
        init(minecraftPath.get(), minecraftVersion, LaunchType.FORGE);
    }

    @Override
    public void beginScanning(IEnvironment environment) {

    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return Collections.emptyList();
    }

    private String getMinecraftVersion() {
        // MinecraftForge 1.13~1.20.2
        // NeoForge 1.20.1~
        try {
            ArgumentHandler handler = Reflection.get(Launcher.INSTANCE, "argumentHandler");
            String[] args = Reflection.get(handler, "args");
            Optional<String> optional = Arrays.stream(args).filter(arg -> arg.equalsIgnoreCase("--fml.mcversion")).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }
        } catch (Exception e) {
            log.warn("Error getting minecraft version: %s", e);
        }

        // MinecraftForge 1.20.3~
        // 1.20.3: https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/fmlloader/src/main/java/net/minecraftforge/fml/loading/VersionInfo.java
        try {
            Class<?> clazz = Class.forName("net.minecraftforge.fml.loading.FMLLoader");
            try (InputStream is = clazz.getResourceAsStream("/forge_version.json")) {
                JsonObject object = JsonUtils.toObj(is, JsonObject.class);
                return object.get("mc").getAsString();
            }
        } catch (Exception e) {
            log.warn("Error getting minecraft version: %s", e);
        }
        return null;
    }
}
