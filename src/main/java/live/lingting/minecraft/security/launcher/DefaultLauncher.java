package live.lingting.minecraft.security.launcher;

import live.lingting.minecraft.security.MixinSecurityManager;
import live.lingting.minecraft.security.domain.NetworkControl;
import live.lingting.minecraft.security.enums.LaunchType;
import live.lingting.minecraft.security.properties.ModProperties;
import live.lingting.minecraft.security.properties.NetworkProperties;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * @author lingting 2024-06-28 11:39
 */
@SuppressWarnings("deprecation")
public abstract class DefaultLauncher {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 初始化
     *
     * @param path    游戏路径
     * @param version 游戏版本
     * @param type    启动类型
     */
    @SneakyThrows
    public void init(Path path, String version, LaunchType type) {
        ModProperties properties = ModProperties.loadByGamePath(path);
        try {
            NetworkProperties network = properties.getNetwork();
            NetworkControl control = network.control();
            SecurityManager current = System.getSecurityManager();
            MixinSecurityManager mixin = new MixinSecurityManager(current, control);
            System.setSecurityManager(mixin);
        } catch (Exception e) {
            log.error("Failed to set security manager", e);
        }
    }
}
