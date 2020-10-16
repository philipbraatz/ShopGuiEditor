package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.Material;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ShopItem{
    public GUIItem item;
    public int buyprice;
    public int sellprice;
    public int page;

    public ShopItem(Material value) {
        item = new GUIItem(value);
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
