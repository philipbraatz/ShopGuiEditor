package tk.doorfail.firstplugin.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import tk.doorfail.firstplugin.ShopGUIEditor;
import tk.doorfail.firstplugin.shopguiplus.Shop;
import tk.doorfail.firstplugin.shopguiplus.Shops;
import tk.doorfail.firstplugin.shopyml;


import java.text.SimpleDateFormat;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

public class MainShopGui extends InventoryGui {
    private static Logger logger;

    private static int shopRows =0;
    private static int shopColumns =0;
    public static Shops shopList;

    private static ArrayList<String> guiSetup(Shops shopList){
        MainShopGui.shopList= shopList;
        ArrayList<String> setup = new ArrayList<>();
        String line = "";
        for (int i = 33; i-33 < shopList.size(); i++) {
            line += (char)i;//creates ascii from '!' onwards
            if(line.length()==9) {
                setup.add(line);
                line ="";
            }
        }
        if(line.length()!=0){
            shopColumns =line.length();
            setup.add(String.format("%1$-9s", line));//.replace(' ', '0');
        }
        shopRows = setup.size();

        setup.add(String.format("%1$-9s", ""));//empty row
        setup.add("~       }");//new shop, exit
        return setup;
    }
    private static ArrayList<GuiElement> guiElements(Shops shopList){
        ArrayList<GuiElement> gui = new ArrayList<>();
        ArrayList<String> setup = guiSetup(shopList);

        for (int j = 0; j <shopRows; j++) {
            String line =setup.get(j);
            for (int i = 0; i < 9; i++) {
                if(j > shopRows-1 && i >= shopColumns){
                    logger.warning("Shop Row,Col MAX:"+shopRows+", "+shopColumns);
                    break;
                }
                try {
                    DynamicGuiElement element = new DynamicGuiElement(((char) (j * 9 + i + 33)),
                            getNextElement(shopList.get(j * 9 + i)));
                    gui.add(element);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        gui.add(new StaticGuiElement('}',new ItemStack(Material.BARRIER),click -> {
            click.getEvent().getWhoClicked().closeInventory();
            return true;
        },"Close"));
        gui.add(new StaticGuiElement('~', new ItemStack(Material.SAPLING), click -> {
            return true;
        }));

        return gui;
    }

    private static Function getNextElement(Shop si){
        return (Function) -> {
            ItemStack sItem = si.items.get(0).item.itemStack;
            sItem.setAmount(1);
            return new StaticGuiElement('(', sItem,
                    click -> {
                        click.getGui().draw(); // Update the GUI
                        ShopEditorGui.setLogger(logger);
                        ShopEditorGui editor = new ShopEditorGui(si,logger);
                        editor.show(click.getEvent().getWhoClicked());
                        return true;
                    },
                    si.name);
        };
    }

    public MainShopGui(Shops _shopList, Logger logger) {
        super(
                ShopGUIEditor.getPlugin(ShopGUIEditor.class),
                null,
                "Shop Editor",
                guiSetup(_shopList).toArray(new String[guiSetup(_shopList).size()]),
                guiElements(_shopList));
        this.logger =logger;
        this.setCloseAction(close -> {
            shopyml.writeShops(shopList);
            return true;
        });
        logger.info("Rows "+guiSetup(_shopList).size());
    }
}
