package tk.doorfail.firstplugin.commands;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import tk.doorfail.firstplugin.ShopGUIEditor;
import tk.doorfail.firstplugin.gui.MainShopGui;
import tk.doorfail.firstplugin.shopguiplus.Shops;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ShopEdit implements CommandExecutor {
    private ShopGUIEditor shopPlugin;
    private Logger logger;

    private MainShopGui mainShopGui;

    public ShopEdit(Plugin shopGUIPlugin, ShopGUIEditor plugin, Shops shopList)
    {

        shopPlugin =plugin;
        logger = plugin.getLogger();

        mainShopGui = new MainShopGui(shopGUIPlugin,shopList,logger);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("shopedit"))
            if(sender instanceof Player)
               return onPlayerCommand((Player) sender);
        return false;
    }

    private boolean onPlayerCommand(Player player){
        if(!player.hasPermission("shopguieditor.edit")) {
            player.sendMessage("You do not have permission to do that");
            return false;
        }
        logger.info("showing GUI...");
        mainShopGui.show(player);
        logger.info("Closing GUI...");
        return true;
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
