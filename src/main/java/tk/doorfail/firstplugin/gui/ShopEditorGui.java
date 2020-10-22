package tk.doorfail.firstplugin.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tk.doorfail.firstplugin.ShopGUIEditor;
import tk.doorfail.firstplugin.shopguiplus.Shop;
import tk.doorfail.firstplugin.shopguiplus.ShopItem;
import tk.doorfail.firstplugin.shopguiplus.Shops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Logger;

public class ShopEditorGui extends InventoryGui {
    private static Logger logger;
    public static void setLogger(Logger log){ logger= log;}

    private static int shopRows =0;
    private static int shopColumns =0;
    public static Shop curShop;
    public static ShopEditorGui shopEditorGui;

    private static ArrayList<String> guiSetup(Shop shop){
        ArrayList<String> setup = new ArrayList<>();
        String line = "";
        for (int i = 33; i-33 < shop.items.size(); i++) {
            line += (char)i;//creates ascii from '!' onwards
            if(line.length()==9) {
                setup.add(line);
                line ="";
            }
        }
        if(line.length()!=0){
            shopColumns =line.length()-1;
            setup.add(String.format("%1$-9s", line));//.replace(' ', '0');'3        '
            //logger.warning("Fixing last row with '"+String.format("%1$-9s", line)+"' from length of "+shopColumns);
        }
        shopRows = setup.size();

        setup.add(String.format("%1$-9s", ""));//empty row
        setup.add("~   {   }");//new item, delete all, back
        return setup;
    }
    private static ArrayList<GuiElement> guiElements(Shop shop){
        ArrayList<GuiElement> gui = new ArrayList<>();
        ArrayList<String> setup = guiSetup(shop);

        for (int j = 0; j <shopRows; j++) {
            String line =setup.get(j);
            for (int i = 0; i < 9; i++) {
                if(j > shopRows-1 && i >= shopColumns){
                    logger.info(shopRows+ " is max rows. "+shopColumns +" is max col");
                    logger.finest("Last Element: "+shop.items.get(shop.items.size()-1).item.itemStack.toString());
                    break;
                }

                if(j*9+i < shop.items.size()) {
                    gui.add(new DynamicGuiElement(((char) (j * 9 + i + 33)),
                            getNextElement(shop.items.get(j * 9 + i))));
                }
            }
        }
        gui.add(new StaticGuiElement('}',new ItemStack(Material.BARRIER), click -> {
            click.getEvent().getWhoClicked().closeInventory();
            return true;
        },"Close"));
        gui.add(new StaticGuiElement('{',new ItemStack(Material.LAVA_BUCKET), click -> {
            //MainShopGui.shopList.shops.indexOf(shop);
            InventoryGui ig = new InventoryGui(ShopGUIEditor.getPlugin(ShopGUIEditor.class),null,
                    "Are you sure you want to delete these items from "+shop.name,
                    new String[]{"y   n"},
                    new StaticGuiElement('y',new ItemStack(Material.EMERALD_BLOCK),click1 -> {
                        MainShopGui.shopList.get(MainShopGui.shopList.indexOf(shop)).items.clear();
                        click1.getEvent().getWhoClicked().closeInventory();
                        click.getEvent().getWhoClicked().closeInventory();
                        return true;
                    },"Yes","(THIS CANNOT BE UNDONE)"),
                    new StaticGuiElement('n',new ItemStack(Material.FIREBALL),click1 -> {
                        click1.getEvent().getWhoClicked().closeInventory();
                        return true;
                    },"No"));
            ig.show(click.getEvent().getWhoClicked());
            return true;
        },"Delete All Items"));
        gui.add(new StaticGuiElement('~', new ItemStack(Material.SAPLING), click -> {
            ShopItem newItem =new ShopItem(Material.AIR);
            shop.items.add(newItem);
            EditGui editGui = new EditGui(newItem,logger);
            editGui.show(click.getEvent().getWhoClicked());
            editGui.setCloseAction(close -> {
                editGui.close();
                ShopEditorGui editor = new ShopEditorGui(shop,logger);
                shopEditorGui = editor;
                editor.show(click.getEvent().getWhoClicked());
                return true;
            });

            return true;
        },"New item"));

        return gui;
    }

    private static Function getNextElement(ShopItem si){
        return (Function) -> {
            ItemStack sItem = si.item.itemStack;
            return new StaticGuiElement('(', sItem,
                    click -> {
                        EditGui editGui = new EditGui(si,logger);
                        editGui.show(click.getEvent().getWhoClicked());
                        return true;
                    },
                    sItem.getType().name(),
                    "Buy: "+si.buyprice,
                    "Sell: "+si.sellprice);
        };
    }

    public ShopEditorGui(Shop shop, Logger logger) {
        super(
                ShopGUIEditor.getPlugin(ShopGUIEditor.class),
                null,
                "Shop Editor",
                guiSetup(shop).toArray(new String[guiSetup(shop).size()]),
                    guiElements(shop));
        shopEditorGui = this;
        this.logger = logger;
        curShop =shop;
    }
}
