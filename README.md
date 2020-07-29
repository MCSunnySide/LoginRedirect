# LoginRedirect
A BungeeCord plugin to redirect player logged in to different server with different IP. Also provide different IP jump different server when trasfering player to others server.

## Download
https://ci.codemc.io/job/Ghost-chu/job/LoginRedirect/

## Configuration
```YAML
rules:
  directConnectToSunnySideWithLogin: #BungeeCode set login is default server
    from: login
    to: sunnyside
    host:
      - play.mcsunnyside.com
      - game.mcsunnyside.com
      - mc.mcsunnyside.com
      - dx.mcsunnyside.com
      - lt.mcsunnyside.com
      - cn.mcsunnyside.com
      - global.mcsunnyside.com
      - fallback.mcsunnyside.com
  directConnectToSunnySideWithoutLogin: #BungeeCode set random server is default server
    from: "#" # '#' = Enter point
    to: sunnyside
    host:
      - direct.mcsunnyside.com
```
