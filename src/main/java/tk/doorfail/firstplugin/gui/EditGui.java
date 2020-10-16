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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Logger;

public class EditGui extends InventoryGui {
    private static Logger logger;
    public static void setLogger(Logger log){ logger= log;}

    public static ShopItem item;

    private static int shopRows =0;
    private static int shopColumns =0;

    private static ArrayList<String> guiSetup(ShopItem item){
        EditGui.item =item;
        ArrayList<String> setup = new ArrayList<>();
        setup.add("qrt1s2uvw");//buy decrease price, decor, buy increase price
        setup.add("abcsisefg");//stack decrease,decor, item,decor, stack increase
        setup.add("klm3s4nop");//sell decrease price, decor, sell increase price
        setup.add("         ");
        setup.add("}   z   ~");//delete,set item, back
        return setup;
    }
    private static ArrayList<GuiElement> guiElements(ShopItem item){
        EditGui.item =item;

        ArrayList<GuiElement> gui = new ArrayList<>();
        ArrayList<String> setup = guiSetup(item);


        //close inventory
        gui.add(new StaticGuiElement('~',new ItemStack(Material.BARRIER), click -> {
            click.getEvent().getWhoClicked().closeInventory();
            return true;
        },"Save & Close"));

        //delete item
        gui.add(new StaticGuiElement('}', new ItemStack(Material.LAVA_BUCKET), click -> {
            //Verify to delete item
            InventoryGui ig = new InventoryGui(ShopGUIEditor.getPlugin(ShopGUIEditor.class),null,
                    "Are you sure you want to delete this item: "+item.item.itemStack.getType().name(),
                    new String[]{"y   n"},
                    //Button for Yes
                    new StaticGuiElement('y',new ItemStack(Material.EMERALD_BLOCK),click1 -> {
                        ShopEditorGui.curShop.items.remove(ShopEditorGui.curShop.items.indexOf(item));//remove item
                        logger.info("closing Y/N");
                        click1.getEvent().getWhoClicked().closeInventory();//close y/n prompt
                        logger.info("closing Edit");
                        click.getGui().close();//close item edit

                        Shop s = ShopEditorGui.shopEditorGui.curShop;//grab current shop
                        logger.info("preping CloseAction");
                        ShopEditorGui.shopEditorGui.setCloseAction(close -> {//on close of shop view
                            ShopEditorGui editor = new ShopEditorGui(s,logger);//create updated shop
                            editor.show(click.getEvent().getWhoClicked());//reopen shop view
                            ShopEditorGui.shopEditorGui.setCloseAction(close1 -> {return true;});
                            return true;
                        });
                        logger.info("closing Shop view");
                        ShopEditorGui.shopEditorGui.close();//actually close shop
                        return true;
                    },"Yes"),
                    //Button for No
                    new StaticGuiElement('n',new ItemStack(Material.FIREBALL),click1 -> {
                        click1.getEvent().getWhoClicked().closeInventory();
                        return true;
                    },"No"));
            ig.show(click.getEvent().getWhoClicked());
            return true;
        },"Delete"));

        //update item
        gui.add(new StaticGuiElement('z', new ItemStack(Material.GLASS), click -> {

            return true;
        },"To Change Item",
                "click on center item with",
                "what you want to change it to"));

        //blank
        gui.add(new StaticGuiElement('s', new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15),
                click -> {return true;},null));

        //Stack size
        gui.addAll(getStackButtons("abcefg",Material.STONE,new ArrayList<Integer>(Arrays.asList(-1,-8,-32,1,8,32))));
        gui.addAll(getBuyButtons("qrt12uvw",Material.EMERALD,new ArrayList<Integer>(Arrays.asList(-1,-15,-100,-500,1,15,100,500))));
        gui.addAll(getSellButtons("klm34nop",Material.REDSTONE,new ArrayList<Integer>(Arrays.asList(-1,-15,-100,-500,1,15,100,500))));

        //item change
        gui.add(new DynamicGuiElement('i', () -> {
            return new StaticGuiElement('i', EditGui.item.item.itemStack,
                    click -> {
                        if(click.getEvent().getCursor().getType() != Material.AIR)
                            EditGui.item.item.itemStack = click.getEvent().getCursor();
                        logger.info(click.getEvent().getCursor().toString());
                        click.getGui().draw();
                        return true;
                    },
                    EditGui.item.item.itemStack.getType().name(),
                    "Buy: "+EditGui.item.buyprice,
                    "Sell: "+EditGui.item.sellprice);
        }));

        return gui;
    }

    private static Function getNextElement(){
        return (Function) -> {
            ItemStack sItem = item.item.itemStack;
            return new StaticGuiElement('(', sItem,
                    click -> {
                        click.getGui().draw(); // Update the GUI
                        //ShopGUIEditor.getPlugin(ShopGUIEditor.class).getLogger().info("index: "+jj*9+ii+" | "+s.shops.get(j*9+i).items.get(0).item.itemStack.toString());
                        return true;
                    },
                    sItem.toString(),
                    "Buy: "+item.buyprice,
                    "Sell: "+item.sellprice);
        };
    }

    public EditGui(ShopItem item, Logger logger) {
        super(
                ShopGUIEditor.getPlugin(ShopGUIEditor.class),
                null,
                "Shop Editor",
                guiSetup(item).toArray(new String[guiSetup(item).size()]),
                guiElements(item));
        this.logger = logger;
        EditGui.logger = logger;
    }

    public static ArrayList<StaticGuiElement> getSellButtons(String idSet, Material item, ArrayList<Integer> price){
        if(price.size() != idSet.length()){
            EditGui.logger.severe("Cannot create sell buttons with PriceSet size: "+price.size()+" and idSet size: "+ idSet.length());
            return null;
        }
        ArrayList<StaticGuiElement> ret = new ArrayList<>();

        ItemStack scalar = new ItemStack(item);
        for (int i = 0; i < idSet.length(); i++) {
            ret.add(new StaticGuiElement(idSet.charAt(i),
                    scalar,
                    getSellElement(price.get(i)),
                    price.get(i)>0?
                            "+"+price.get(i):
                            "-"+price.get(i),"Sell Price"));
        }
        return ret;
    }

    public static ArrayList<StaticGuiElement> getBuyButtons(String idSet, Material item, ArrayList<Integer> price){
        if(price.size() != idSet.length()){
            EditGui.logger.severe("Cannot create buy buttons with PriceSet size: "+price.size()+" and idSet size: "+ idSet.length());
            return null;
        }
        ArrayList<StaticGuiElement> ret = new ArrayList<>();

        ItemStack scalar = new ItemStack(item);
        for (int i = 0; i < idSet.length(); i++) {
            ret.add(new StaticGuiElement(idSet.charAt(i),
                    scalar,
                    getBuyElement(price.get(i)),
                    price.get(i)>0?
                            "+"+price.get(i):
                            "-"+price.get(i),"Buy Price"));
        }
        return ret;
    }

    public static ArrayList<StaticGuiElement> getStackButtons(String idSet, Material item, ArrayList<Integer> amount){
        if(amount.size() != idSet.length()){
            EditGui.logger.severe("Cannot create stack size buttons with AmountList size: "+amount.size()+" and idSet size: "+ idSet.length());
            return null;
        }
        ArrayList<StaticGuiElement> ret = new ArrayList<>();

        ItemStack scalar = new ItemStack(item);
        for (int i = 0; i < idSet.length(); i++) {
            ret.add(new StaticGuiElement(idSet.charAt(i),
                    scalar,
                    getStackSizeElement(amount.get(i)),
                    amount.get(i)>0?
                            "+"+amount.get(i):
                            "-"+amount.get(i),"Stack Size"));
        }
        return ret;
    }

    private static GuiElement.Action getSellElement(int price){
        return (click) -> {
            EditGui.item.sellprice =Math.max(0,EditGui.item.sellprice+price);
            EditGui.logger.finer("setting sell price to: "+EditGui.item.sellprice +" ("+price+")");
            click.getGui().draw();
            return true;
        };
    }

    private static GuiElement.Action getBuyElement(int price){
        return (click) -> {
            EditGui.item.buyprice =Math.max(0,EditGui.item.buyprice+price);
            EditGui.logger.finer("setting buy price to: "+EditGui.item.buyprice+" ("+price+")");
            click.getGui().draw();
            return true;
        };
    }

    private static GuiElement.Action getStackSizeElement(int amount){
        return (click) -> {
            EditGui.item.item.itemStack.setAmount(EditGui.item.item.itemStack.getAmount()+amount);
            EditGui.logger.finer("setting buy price to: "+EditGui.item.item.itemStack.getAmount()+" ("+amount+")");
            click.getGui().draw();
            return true;
        };
    }
}
