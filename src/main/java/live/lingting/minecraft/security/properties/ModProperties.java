package live.lingting.minecraft.security.properties;

import live.lingting.minecraft.security.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author lingting 2024-06-28 14:06
 */
@Getter
@Setter
public class ModProperties {

    public static final String id = "lingting.minecraft.security";
    public static final String name = "LingtingMinecraftSecurity";

    private NetworkProperties network;

    public static ModProperties defaultProperties() {
        ModProperties properties = new ModProperties();
        properties.network = NetworkProperties.defaultProperties();
        return properties;
    }

    public static ModProperties loadByGamePath(Path gamePath) throws IOException {
        Path configDir = gamePath.resolve("config").resolve("lingting");
        if (!configDir.toFile().exists()) {
            configDir.toFile().mkdirs();
        }
        Path configFile = configDir.resolve(id + ".json");
        File file = configFile.toFile();
        ModProperties defaulted = defaultProperties();
        if (!file.exists()) {
            file.createNewFile();
        } else {
            try (FileInputStream stream = new FileInputStream(file)) {
                ModProperties config = JsonUtils.toObj(stream, ModProperties.class);
                defaulted.merge(config);
            } catch (Exception ignore) {
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            String json = JsonUtils.toJson(defaulted);
            writer.write(json);
        }
        return defaulted;
    }

    void merge(ModProperties other) {
        if (other == null) {
            return;
        }
        this.network.merge(other.network);
    }
}
