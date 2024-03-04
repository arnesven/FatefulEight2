package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LuckyTalisman extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(2, 9, MyColors.TAN, MyColors.GOLD, MyColors.ORANGE);

    public LuckyTalisman() {
        super("Lucky Talisman", 20);
    }

    @Override
    public int getSPBonus() { return 1; }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LuckyTalisman();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
