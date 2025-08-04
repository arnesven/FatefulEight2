package model.items.special;

import model.Model;
import model.items.Item;
import model.items.Prevalence;
import model.items.SocketedItem;
import model.items.accessories.Accessory;
import model.items.accessories.JewelryItem;
import util.MyLists;
import view.GameView;
import view.MyColors;
import view.party.ConfigureSocketedItemMenu;
import view.party.SelectableListMenu;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class CommonSash extends Accessory implements SocketedItem {
    private static final Sprite SPRITE = new ItemSprite(12, 13, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.LIGHT_GRAY);
    private JewelryItem jewelry1 = null;
    private JewelryItem jewelry2 = null;

    public CommonSash() { // FEATURE: This item is not in use... it needs to get a common super class with fashionable sash.
        super("Common Sash", 38);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 300;
    }

    @Override
    public Item copy() {
        return new CommonSash();
    }

    @Override
    public String getSound() {
        return "chainmail1";
    }

    @Override
    public int getNumberOfSockets() {
        return 2;
    }

    @Override
    public Item getInnerItem(int index) {
        if (index == 0) {
            return jewelry1;
        }
        return jewelry2;
    }

    @Override
    public void setInnerItem(int index, Item it) {
        if (index == 0) {
            jewelry1 = (JewelryItem)it;
        } else {
            jewelry2 = (JewelryItem)it;
        }
    }

    @Override
    public String getSocketLabels() {
        return "Jewelry Jewelery";
    }

    @Override
    public List<Item> getItemsForSlot(Model model, int index) {
        return new ArrayList<>(MyLists.filter(model.getParty().getInventory().getAccessories(),
                (Accessory acc) -> acc instanceof JewelryItem));
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public boolean hasDualUseInMenu() {
        return true;
    }

    @Override
    public String getDualUseLabel() {
        return "Configure";
    }

    @Override
    public boolean supportsHigherTier() {
        return false;
    }

    @Override
    public SelectableListMenu getDualUseMenu(GameView innerView, int x, int y) {
        return new ConfigureSocketedItemMenu(innerView, this);
    }
}
