package aren227.configmanager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigSession extends YamlConfiguration {

    private File file;

    public ConfigSession(File file){
        super();

        this.file = file;

        if(file.exists()){
            try {
                load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(){
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile(){
        return file;
    }

    public void setDesc(String path, String desc){
        set("!desc." + path, desc);
    }

    public void setDesc(String path, String desc, Object def){
        if(!isSet(path)) set(path, def);
        setDesc(path, desc);
    }

}
