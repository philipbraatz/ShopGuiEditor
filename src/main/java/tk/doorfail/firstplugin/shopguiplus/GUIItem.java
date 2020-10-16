package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUIItem{
    public ItemStack itemStack;
    public int slot;

    public GUIItem(Material value) {
        itemStack = new ItemStack(value);
    }

    public Map<String,Object> toDictionary() {
        HashMap map = new HashMap();
        map.put("material",itemStack.getType().name());
        map.put("quantity",itemStack.getAmount());
        if(itemStack.getDurability() != 0)
            map.put("damage",itemStack.getDurability());
        return map;
    }
}
