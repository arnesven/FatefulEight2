package model.items.special;

import model.Inventory;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CrimsonPearlItem extends Item {
    private static final Sprite SPRITE = new PearlSprite();

    public CrimsonPearlItem() {
        super("Crimson Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addSpecialItem(this);
    }

    @Override
    public String getShoppingDetails() {
        return "";
    }

    @Override
    public Item copy() {
        return new CrimsonPearlItem();
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    private static class PearlSprite extends ItemSprite {
        public PearlSprite() {
            super(15, 4);
            setColor2(MyColors.DARK_RED);
            setColor3(MyColors.GRAY_RED);
            setColor4(MyColors.RED);
        }
    }
}
