package tk.doorfail.firstplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tk.doorfail.firstplugin.commands.ShopEdit;
import tk.doorfail.firstplugin.shopguiplus.Shops;

import java.io.File;

public final class ShopGUIEditor extends JavaPlugin implements Listener {
    private ShopEdit shopEdit;
    private Shops shopList;
    public  Plugin shopguiplus;

    @Override
    public void onEnable() {

        // Plugin startup logic
        getLogger().info("ShopGUIeditor Starting...");
        shopguiplus =Bukkit.getServer().getPluginManager().getPlugin("ShopGUIPlus");
        if(shopguiplus == null) {
            getLogger().severe("ShopGUIPlus plugin does not exist. Aborting");
            return;
        }  else{
            getLogger().info("grabbing information from ShopGUIPlus");
            shopyml.setLogger(getLogger());
            shopList = shopyml.readShopyml(new File(shopguiplus.getDataFolder(),"shops.yml"));
        }

        //getLogger().info("Setting GUI");
        shopEdit = new ShopEdit(shopguiplus,this,shopList);

        //getLogger().info("Adding Command");
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
