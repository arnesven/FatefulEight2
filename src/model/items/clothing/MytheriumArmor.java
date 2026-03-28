package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
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
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 2000;
    }

    @Override
    public int getMP() {
        return 3;
    }

    @Override
    public Item copy() {
        return new MytheriumArmor();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }
}
