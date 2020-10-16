package tk.doorfail.firstplugin;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import tk.doorfail.firstplugin.shopguiplus.Shop;
import tk.doorfail.firstplugin.shopguiplus.ShopItem;
import tk.doorfail.firstplugin.shopguiplus.Shops;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class shopyml {
    private ShopGUIEditor shopPlugin;
    private static Logger logger;

    public static Shops shopList = new Shops();

    public static void setLogger(Logger l){
        logger = l;
    }

    static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static Shop getLastShop()
    {
        return shopList.get(shopList.size()-1);
    }
    private static void setLastShop(Shop shop)
    {
        shopList.set(shopList.size()-1,shop);
    }

    public static Shops readShopyml(boolean debug)
    {
        File pluginfolder = ShopGUIEditor.getPlugin(ShopGUIEditor.class).getDataFolder().getParentFile();//new File(System.getProperty("user.dir")+"\\plugins\\ShopGUIPlus\\shops.yml").getAbsoluteFile();
        File shopguifolder = new File(pluginfolder,"ShopGUIPlus\\shops.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(shopguifolder);

        Map<String, Object> values = config.getValues(false);
        values.forEach((name,value) ->{
            if ((value instanceof MemorySection)){
                loadMemorySection((MemorySection) value,debug);
                logger.finer("End of memory section" +name+"!");
            }
        });
        logger.info("Found "+shopList.size()+" shops");
        logger.finest("Last shop item count: "+ shopList.get(shopList.size()-1).items.size());
        //logger.info("Items in first shop: "+shopList.shops.get(0).items.size());
        return shopList;
    }

    private static void loadMemorySection(MemorySection ms, boolean debug)
    {
        /*values that will break for Shop Names:
            shops
            name
            fillItem
            items
            type
            item
            material
            quantity
            buyPrice
            slot
        DO NOT use those values for SHOP NAMES
        */
        final boolean[] skipMemSec = {false};
        ms.getValues(false).forEach((name,value)->{
            if(skipMemSec[0])
                return;

            if(value instanceof MemorySection) {
                switch (name){
                    case "fillItem":
                        logger.finest("Has fill Item");
                        return;//do not store this data
                    case "goBack":
                    case "previousPage":
                    case "nextPage":
                        break;//ignore pages, do not store data
                    case "items":
                        logger.finest("ITEMS: ");
                        break;
                    case "item":
                        logger.finest("ITEM DATA: ");
                        break;
                    case "shops":
                        logger.finest("ALL Shops");
                    case "buttons":
                        logger.finest("BUTTONS");
                        break;
                    default:
                        if(tryParseInt(name)){
                            logger.finer("--item #" + name);
                        }
                        else{
                            if(name != null){
                                logger.info("SHOP: "+name);
                                shopList.add(new Shop());
                            }
                            else
                                logger.warning("Name is NULL! for "+ms);
                        }
                        break;
                }
                loadMemorySection((MemorySection) value, debug);
            }
            else{
                Shop lastShop = getLastShop();//get shop to be used
                ShopItem shopItem = new ShopItem(Material.AIR);//Bad value
                if(!lastShop.items.isEmpty())
                    shopItem= lastShop.items.get(lastShop.items.size()-1);

                logger.finer(name+": "+value);

                switch (name) {
                    case "name":
                        lastShop.name =((String)value).split(" ")[0];
                        break;
                    case "material":
                        lastShop.items.add(new ShopItem(Material.valueOf((String)value)));
                        break;
                    case "quantity":
                        shopItem.item.itemStack.setAmount((int)value);
                        break;
                    case "damage":
                        shopItem.item.itemStack = new ItemStack(
                                shopItem.item.itemStack.getType(),
                                shopItem.item.itemStack.getAmount(),
                                ((Integer)value).shortValue());
                        break;
                    case "buyPrice":
                        shopItem.buyprice=(int)value;
                        break;
                    case "sellPrice":
                        shopItem.sellprice=(int)value;
                        break;
                    case "slot":
                        try {
                            shopItem.item.slot=(int)value;
                        } catch (ClassCastException e) {
                            shopItem.item.slot = Integer.parseInt((String) value);
                        }
                        break;
                    case "special":
                        skipMemSec[0] = true;//GUI button, do not save data
                        break;
                    default:
                        break;
                }
                setLastShop(lastShop);//update shop from loop
            }
        });
    }

    public static void writeShops(Shops shops) {
        File pluginfolder = ShopGUIEditor.getPlugin(ShopGUIEditor.class).getDataFolder().getParentFile();//new File(System.getProperty("user.dir")+"\\plugins\\ShopGUIPlus\\shops.yml").getAbsoluteFile();
        File shopguifolder = new File(pluginfolder,"ShopGUIPlus\\shops.yml");
        try {
            if(!shopguifolder.exists()) {
                shopguifolder.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(""));

            //Map test = shops.toDictionary(config);
            config.set("shops",shops.toDictionary(config));
            //config.set("shops", config.createSection("shops",test));
            config.save(shopguifolder);
            logger.info("Saved to: "+shopguifolder.getAbsolutePath());
            logger.info("data: "+shops.toDictionary(config).get("shops"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
