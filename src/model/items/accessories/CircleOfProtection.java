package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CircleOfProtection extends JewelryItem {

    private static final Sprite SPRITE = new ItemSprite(1, 9,
            MyColors.DARK_GREEN, MyColors.LIGHT_GREEN, MyColors.LIGHT_PINK);

    public CircleOfProtection() {
        super("Circle of Protection", 22);
    }

    @Override
    public int getMP() {
        return 2;
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CircleOfProtection();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
