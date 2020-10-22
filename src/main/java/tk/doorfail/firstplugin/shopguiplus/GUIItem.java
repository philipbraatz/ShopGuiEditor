package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GUIItem{
    public ItemStack itemStack;
    public int slot;

    public GUIItem(Material value) {
        itemStack = new ItemStack(value);
    }

    public GUIItem(@NotNull MemorySection value) {
        //ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info("Damage: "+value.get("damage"));
        if(value.get("damage")  == null)
            itemStack = new ItemStack(Material.getMaterial(
                    (String)value.get("material")),
                    (int)value.get("quantity"));
        else
            itemStack = new ItemStack(Material.getMaterial(
                    (String)value.get("material")),
                    (int)value.get("quantity"),
                    (short)((int)(value.get("damage"))));
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
