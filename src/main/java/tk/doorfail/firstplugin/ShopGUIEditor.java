package tk.doorfail.firstplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tk.doorfail.firstplugin.commands.ShopEdit;
import tk.doorfail.firstplugin.shopguiplus.Shops;

public final class ShopGUIEditor extends JavaPlugin implements Listener {
    private ShopEdit shopEdit;
    private Shops shopList;

    @Override
    public void onEnable() {

        // Plugin startup logic
        getLogger().info("ShopGUIeditor Starting...");
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        shopyml.setLogger(getLogger());
        shopList = shopyml.readShopyml(false);
        getLogger().info("Setting GUI");
        shopEdit = new ShopEdit(this,shopList);

        getLogger().info("Adding Command");
        getCommand("shopedit").setExecutor(shopEdit);

        //Plugin plugin = ShopGUIEditor.getPlugin(ShopGUIEditor.class);
        //for @EventHandler onSomeThing(SomeThingEvent e)
       //getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
