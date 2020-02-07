package aren227.configmanager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.HashMap;

public final class ConfigManager extends JavaPlugin {

    public static ConfigManager instance;

    public ConfigSession sessionData;
    public HashMap<String, ConfigSession> sessions = new HashMap<>();

    @Override
    public void onLoad(){
        instance = this;
        sessionData = getConfigSession(this);

        getLogger().info("ConfigManager Loaded.");
    }

    @Override
    public void onEnable(){
        sessionData.setDesc("saveDelay", "자동 저장 주기를 틱 단위로 설정합니다.", 20 * 60 * 10); // 10분

        int saveDelay = sessionData.getInt("saveDelay");

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, this::saveAll, saveDelay, saveDelay);
    }

    public static ConfigSession getConfigSession(JavaPlugin plugin){
        if(instance.sessions.containsKey(plugin.getName())){
            return instance.sessions.get(plugin.getName());
        }

        plugin.getDataFolder().mkdir();

        ConfigSession config = new ConfigSession(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + plugin.getName() + ".yml"));

        instance.sessions.put(plugin.getName(), config);

        if(plugin != instance){ //ConfigManager 플러그인은 제외
            instance.sessionData.set(plugin.getName(), config.getFile().getAbsolutePath());
            instance.sessionData.save();
        }

        plugin.getLogger().info("Config Session for " + plugin.getName() + " was created.");

        return config;
    }

    public void saveAll(){
        for(ConfigSession cs : sessions.values()){
            cs.save();
        }
        getLogger().info("Saved " + sessions.size() + " sessions.");
    }

    @Override
    public void onDisable() {
        saveAll();
    }
}
