package tk.doorfail.firstplugin.shopguiplus;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tk.doorfail.firstplugin.ShopGUIEditor;
import tk.doorfail.firstplugin.gui.ShopEditorGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shop{
    public String name;
    //public ItemStack fillItem;
    public ArrayList<ShopItem> items = new ArrayList<>();

    private ItemStack fillItem = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15);

    public Shop() {}
    public Shop(String _displayName) {
        name = _displayName;
    }

    public int getInventorySize(){
        return (((int)Math.ceil(items.size()/9))+2)*9;
    }

    private Map<String, Object> getFillItem(Material item){
        HashMap<String, Object> map = new HashMap<>();
        map.put("material",Material.STAINED_GLASS_PANE.name());
        map.put("damage",15);
        map.put("name"," ");
        return map;
    }
    private Map<String,Object> getSpecialBalance(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("type","special");
        map.put("special","BALANCE");
        map.put("slot",getInventorySize()-1);//last slot
        return map;
    }
    private Map<String, Object> getButtons(){
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> goBack = new HashMap<>();
        goBack.put("slot",getInventorySize()-5);
        map.put("goBack",goBack);
        return map;
    }


    public Map<String,Object> toDictionary(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("size",getInventorySize());
        ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info("Slot Max: "+getInventorySize()+", Count: "+items.size());
        map.put("fillItem",getFillItem(Material.SPRUCE_DOOR));//filler item, need to change
        map.put("buttons",getButtons());

        HashMap<Integer, Object> itemMap = new HashMap<Integer, Object>();
        for (int i = 0; i < items.size(); i++) {
            //ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info("Item #"+i+": "+items.get(i).item.itemStack.getType().name());
            itemMap.put((int) (i + 1), items.get(i).toDictionary(i));
            //ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info("Item Map #"+i+": "+itemMap.get(i+1).toString());
        }
        //itemMap.put((int) 99, items.get(items.size()-1).toDictionary(items.size()-1));
        itemMap.put(items.size()+16,getSpecialBalance());
        map.put("items",(Map<Integer,Object>)itemMap);
        //ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info("Item Map #"+itemMap.size()+": "+itemMap.get(itemMap.size()-1).toString());
        ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().finest("Map: "+map.get("items").toString());

        return map;
    }
}
