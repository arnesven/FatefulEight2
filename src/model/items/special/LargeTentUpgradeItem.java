package model.items.special;

import model.items.Inventory;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.TownSubView;

public class LargeTentUpgradeItem extends TentUpgradeItem {

    private static final Sprite SPRITE = new ItemSprite(9, 4,
            TownSubView.PATH_COLOR, MyColors.GOLD, MyColors.YELLOW);


    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getCost() {
        return super.getCost() * 2 - 15;
    }

    @Override
    public String getName() {
        return "Large Tent Upgrade";
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addToTentSize();
        inventory.addToTentSize();
    }

    @Override
    public String getShoppingDetails() {
        return ", Expands your maximum party size by 2.";
    }

    @Override
    public Item copy() {
        return new LargeTentUpgradeItem();
    }
}
