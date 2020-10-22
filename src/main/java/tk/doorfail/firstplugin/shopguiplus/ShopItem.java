package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.jetbrains.annotations.NotNull;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ShopItem{
    public GUIItem item;
    public int buyprice;
    public int sellprice;

    public ShopItem(Material value) {
        item = new GUIItem(value);
    }

    public ShopItem(@NotNull MemorySection values) {
        if(values.get("type").equals("item"))
        {
            buyprice = (int)values.get("buyPrice");
            sellprice = (int)values.get("sellPrice");
            item = new GUIItem((MemorySection)(values.get("item")));
        }
    }

    public static boolean isTypeItem(@NotNull Map<String, Object> value) {
        return value.get("type").equals("item");
    }

    public Map<String, Object> toDictionary(int slot){
        HashMap map = new HashMap();
        map.put("type","item");
        map.put("item",item.toDictionary());
        map.put("buyPrice",buyprice);
        map.put("sellPrice",sellprice);
        map.put("slot",slot);
        return map;
    }
}
