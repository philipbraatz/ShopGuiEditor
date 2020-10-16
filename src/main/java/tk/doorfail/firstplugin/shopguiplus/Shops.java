package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class Shops extends ArrayList<Shop> {
    public ConfigurationSection toDictionary(YamlConfiguration config){
        HashMap map = new HashMap();
        for (int i = size()-1; i >= 0; i--) {//prints in reverse
            map.put("shop"+(i+1),this.get(i).toDictionary());
        }
        return config.createSection("shops",map);
    }


}

