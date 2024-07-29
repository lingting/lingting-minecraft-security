package live.lingting.minecraft.security.domain;

import live.lingting.minecraft.security.properties.NetworkProperties;


/**
 * @author lingting 2024-07-29 13:53
 */
class NetworkControlTest {

    public static void main(String[] args) {
        NetworkControl control = new NetworkControl(NetworkProperties.defaultProperties());
        assert (control.isReject("www.baidu.com"));
        assert !(control.isReject("244.178.44.111"));
        assert (control.isReject("www.baidu.com."));
        assert !(control.isReject("mojang.com"));
        assert !(control.isReject("mojang.com."));
    }
}