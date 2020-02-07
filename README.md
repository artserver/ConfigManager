# ConfigManager
플러그인의 설정 파일을 쉽게 관리해줍니다.

### 주의!
본 플러그인은 많은 양의 데이터를 저장하는데에 최적화되지 않았습니다.\
플레이어 별 데이터를 관리하고 싶으시다면 다른 플러그인을 이용해주세요.

이 플러그인은 플러그인 상에서 어떤 값을 저장하거나 불러오고 싶거나,\
외부에서 쉽게 변경 가능한 상수값(하드코딩 방지)을 이용하고 싶을 때 사용하면 좋습니다.

##사용 방법
먼저, ```ConfigManager.jar```를 프로젝트에 연결해주세요.

```plugin.yml```에 다음 종속성을 추가해주세요.\
(테스트 결과, 굳이 추가하지 않아도 잘 동작하는 것으로 보여짐. 확인 바람.)
```
depend: [ConfigManager]
```

이제 아래 코드 한 줄을 통해 설정 파일을 불러올 수 있습니다.\
생성 / 자동 저장은 자동으로 관리되므로 걱정하지 마세요.
```java
import aren227.configmanager.ConfigManager;
import aren227.configmanager.ConfigSession;

ConfigSession config = ConfigManager.getConfigSession(this);
```

ConfigSession 객체를 생성하면 다음과 같이 값을 불러오거나 저장할 수 있습니다.\
사용 가능한 메서드들은 다음을 참고하세요.
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/configuration/file/FileConfiguration.html
```java
config.set("name", "Steven01");
config.set("age", 20);

int age = config.getInt("age", 0); //기본값 0으로 설정 (이전에 set이 안됐다면 0을 반환)
```

어떤 값에 대해서 설명을 추가하고 싶으시면 다음과 같이 설명을 등록할 수 있습니다.\
설명을 추가하면 나중에 외부에서 값을 관리할 때 도움이 됩니다. (아직 미구현)\
onEnable에 배치하는 것을 권장합니다.
```java
config.setDesc("ticks", "서버가 몇 틱 동안 켜져있었는지를 나타냅니다.", 0); //기본값 설정 (권장)
config.setDesc("ticks", "서버가 몇 틱 동안 켜져있었는지를 나타냅니다."); //기본값 설정 X
```

##예제
```java
package aren227.configmanagertest;

import aren227.configmanager.ConfigManager;
import aren227.configmanager.ConfigSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManagerTest extends JavaPlugin implements Listener {

    public ConfigSession config;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        config = ConfigManager.getConfigSession(this); //설정 파일 세션을 불러옵니다.

        config.setDesc("ticks", "서버가 몇 틱 동안 켜져있었는지를 나타냅니다.", 0); //설명 추가

        getServer().getScheduler().scheduleSyncRepeatingTask(this, this::everyTick, 0, 1); //람..다..식....
    }

    public void everyTick(){
        int t = config.getInt("ticks"); //따로 기본값(0)을 설정하지 않아도 됩니다. 이미 위에서 설정했으니까요.
        config.set("ticks", t + 1);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        config.set("lastJoinTime", System.currentTimeMillis()); //외부에 노출될 필요가 없다면 굳이 설명을 등록할 필요는 없습니다.
    }

    @Override
    public void onDisable() {
        getLogger().info("마지막 플레이어 접속 시간 : " + config.getLong("lastJoinTime", 0));
    }
}
```

##Jar 다운로드
```out/artifacts/ConfigManager/ConfigManager.jar```

##추가할 기능
- 웹 페이지 또는 인게임 다이얼로그로 값을 변경할 수 있도록 합니다.
- 값에 대해 외부에서 변경할 수 없도록 하는 옵션을 추가합니다.
- 등등..