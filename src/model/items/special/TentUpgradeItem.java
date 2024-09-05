package model.items.special;

import model.items.Inventory;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.TownSubView;

public class TentUpgradeItem extends Item {
    private static final Sprite SPRITE = new ItemSprite(9, 4,
            TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.YELLOW);

    public TentUpgradeItem() {
        super("Tent Upgrade", 25);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addToTentSize();
    }

    @Override
    public String getShoppingDetails() {
        return ", Expands your maximum party size by 1.";
    }

    @Override
    public Item copy() {
        return new TentUpgradeItem();
    }

    @Override
    public String getSound() {
        return "chainmail1";
    }
}
