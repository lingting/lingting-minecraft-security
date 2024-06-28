package live.lingting.minecraft.security;

import live.lingting.minecraft.security.domain.NetworkControl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.security.Permission;

/**
 * @author lingting 2024-06-28 14:58
 */
@RequiredArgsConstructor
public class MixinSecurityManager extends SecurityManager {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final SecurityManager current;
    private final NetworkControl network;

    @Override
    public void checkPermission(Permission perm) {
        if (current != null) {
            current.checkPermission(perm);
        }
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        if (current != null) {
            current.checkPermission(perm, context);
        }
    }

    @Override
    public void checkConnect(String host, int port) {
        checkNetwork(host, port);
        super.checkConnect(host, port);
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        checkNetwork(host, port);
        super.checkConnect(host, port, context);
    }

    @SneakyThrows
    void checkNetwork(String host, int port) {
        if (network.isReject(host)) {
            log.debug("reject access to {}:{}", host, port);
            // 抛出IO异常而不是权限控制异常避免有些mod仅处理IO异常然后崩溃
            throw new MalformedURLException("Deny access to " + host + ":" + port);
        }
    }

}
