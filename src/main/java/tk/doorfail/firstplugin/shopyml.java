package tk.doorfail.firstplugin;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import tk.doorfail.firstplugin.shopguiplus.Shop;
import tk.doorfail.firstplugin.shopguiplus.ShopItem;
import tk.doorfail.firstplugin.shopguiplus.Shops;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public static Shops readShopyml(File configFile)
    {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        Map<String, Object> values = config.getValues(false);
        //logger.info("Information: "+values.toString());
        MemorySection shops =(MemorySection)values.get("shops");
        if(shops==null) {
            logger.severe("Could not find shops in: " + configFile.getAbsolutePath());
            return new Shops();//Return no information
        }
        shopList = new Shops(shops);

        logger.info("Found "+shopList.size()+" shops");
        logger.finest("Last shop item count: "+ shopList.get(shopList.size()-1).items.size());

        return shopList;
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
            //logger.info("data: "+shops.toDictionary(config).get("shops"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
