package live.lingting.minecraft.security.properties;

import live.lingting.minecraft.security.domain.NetworkControl;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lingting 2024-06-28 14:42
 */
@Getter
@Setter
public class NetworkProperties {

    private Set<String> white;

    private Set<String> black;

    /**
     * 是否允许任意形式的IP请求. 可以把IP配置在white中以放行指定IP
     */
    private Boolean allowIp = true;

    public static NetworkProperties defaultProperties() {
        NetworkProperties properties = new NetworkProperties();
        properties.white = new HashSet<>();
        properties.white.add("minecraft.net");
        properties.white.add("mojang.com");
        properties.white.add("skin.prinzeugen.net");
        properties.white.add("api.mcmod.cn");
        properties.white.add("downloader.meitangdehulu.com");
        properties.white.add("plushie.moe");
        properties.white.add("login.live.com");
        properties.white.add("localhost");
        properties.white.add("xboxlive.com");
        properties.white.add("minecraftservices.com");
        properties.black = Collections.emptySet();
        properties.allowIp = true;
        return properties;
    }

    void merge(NetworkProperties other) {
        if (other == null) {
            return;
        }
        if (other.white != null) {
            this.white = other.white;
        }
        if (other.black != null) {
            this.black = other.black;
        }
        if (other.allowIp != null) {
            this.allowIp = other.allowIp;
        }
    }

    public NetworkControl control() {
        return new NetworkControl(this);
    }
}
