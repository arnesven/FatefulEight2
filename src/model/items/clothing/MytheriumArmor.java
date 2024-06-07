package model.items.clothing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class MytheriumArmor extends Clothing {

    private static final Sprite SPRITE = new ItemSprite(10, 3,
            MyColors.LIGHT_BLUE, MyColors.LIGHT_BLUE, MyColors.CYAN);

    public MytheriumArmor() {
        super("Mytherium Armor", 220, 3, false);
    }

    @Override
    public String getShoppingDetails() {
        return super.getShoppingDetails() + ", AP is also applied to magic damage.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 2000;
    }

    @Override
    public Item copy() {
        return new MytheriumArmor();
    }
}
