package live.lingting.minecraft.security.domain;

import live.lingting.minecraft.security.properties.NetworkProperties;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author lingting 2024-06-28 16:28
 */
public class NetworkControl {
    static final Map<String, Boolean> CACHE = new ConcurrentHashMap<>();
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(?:[A-Fa-f0-9]{1,4}:){7}[A-Fa-f0-9]{1,4}|(?:[A-Fa-f0-9]{1,4}:){1,7}:|(?:[A-Fa-f0-9]{1,4}:){1,6}:[A-Fa-f0-9]{1,4}|(?:[A-Fa-f0-9]{1,4}:){1,5}(?::[A-Fa-f0-9]{1,4}){1,2}|(?:[A-Fa-f0-9]{1,4}:){1,4}(?::[A-Fa-f0-9]{1,4}){1,3}|(?:[A-Fa-f0-9]{1,4}:){1,3}(?::[A-Fa-f0-9]{1,4}){1,4}|(?:[A-Fa-f0-9]{1,4}:){1,2}(?::[A-Fa-f0-9]{1,4}){1,5}|[A-Fa-f0-9]{1,4}:(?::[A-Fa-f0-9]{1,4}){1,6}|:(?::[A-Fa-f0-9]{1,4}){1,7}|::(?:[A-Fa-f0-9]{1,4}:){1,5}|::(?:[A-Fa-f0-9]{1,4}:){1,4}|::(?:[A-Fa-f0-9]{1,4}:){1,3}|::(?:[A-Fa-f0-9]{1,4}:){1,2}|::[A-Fa-f0-9]{1,4}|::$");

    /**
     * 是否白名单模式
     */
    private final boolean isWhite;
    /**
     * 配置
     */
    private final Set<String> list = new HashSet<>();

    private final boolean allowIp;

    public NetworkControl(NetworkProperties properties) {
        Set<String> white = properties.getWhite();
        Set<String> black = properties.getBlack();
        this.isWhite = black == null || black.isEmpty();
        this.list.addAll(isWhite ? white : black);
        this.allowIp = Boolean.TRUE.equals(properties.getAllowIp());
    }

    /**
     * 是否拒绝此请求
     *
     * @param host target
     * @return true 拒绝请求
     */
    public boolean isReject(String host) {
        return CACHE.computeIfAbsent(host, k -> {
            // 在配置中
            if (isContains(host)) {
                // 白名单放行, 黑名单拒绝
                return !isWhite;
            }
            // 如果是IP, 走IP策略
            if (isIp(host)) {
                return !allowIp;
            }
            // 不在配置策略: 白名单拒绝, 黑名单放行
            return isWhite;
        });
    }

    public boolean isContains(String host) {
        for (String raw : list) {
            if (Objects.equals(raw, host) || host.endsWith(raw)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIp(String host) {
        if (IPV4_PATTERN.matcher(host).matches()) {
            return true;
        }
        if (IPV6_PATTERN.matcher(host).matches()) {
            return true;
        }
        return false;
    }
}
