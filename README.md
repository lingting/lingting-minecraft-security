#### 公共信息

- `配置文件: config/lingting/lingting.minecraft.security.json`

#### 网络控制

##### 配置文件

> 如果同时配置白名单和黑名单则仅黑名单生效

- `white` 白名单, 可以是域名或者具体IP, 如果填写主域名则允许该主域名下所有子域名
- `black` 黑名单, 可以是域名或者具体IP, 如果填写主域名则拒绝该主域名下所有子域名
- `allowIp` 是否直接放行IP. 为true时 如果IP不在黑名单则直接放行.

```json
{
  "network": {
    "white": [
      "localhost",
      "downloader.meitangdehulu.com",
      "mojang.com",
      "login.live.com",
      "minecraftservices.com",
      "skin.prinzeugen.net",
      "minecraft.net",
      "api.mcmod.cn",
      "plushie.moe",
      "xboxlive.com"
    ],
    "black": [],
    "allowIp": true
  }
}
```