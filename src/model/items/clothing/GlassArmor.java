package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class GlassArmor extends Clothing {

    private static final Sprite SPRITE = new ItemSprite(10, 3,
            MyColors.DARK_BROWN, MyColors.BLACK, MyColors.GREEN);

    public GlassArmor() {
        super("Glass Armor", 160, 5, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 500;
    }

    @Override
    public Item copy() {
        return new GlassArmor();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
