package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import tk.doorfail.firstplugin.ShopGUIEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shops extends ArrayList<Shop> {
    public Shops() {}
    public Shops(@NotNull MemorySection values) {
        this.clear();
        //ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info(values.getValues(false).toString());
        values.getValues(false).forEach((key,value) ->{
            this.add(new Shop((MemorySection)value));
        });
    }

    public ConfigurationSection toDictionary(YamlConfiguration config){
        HashMap map = new HashMap();
        for (int i = 0; i < size(); i++)//prints in reverse
            map.put("shop"+(i+1),this.get(i).toDictionary());
        return config.createSection("shops",map);
    }
}

