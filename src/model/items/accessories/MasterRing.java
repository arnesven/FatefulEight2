package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import util.MyStrings;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class MasterRing extends JewelryItem {

    public static final int MASTERY_FACTORY = 3;
    private static final Sprite SPRITE = new ItemSprite(8, 9, MyColors.GOLD, MyColors.TAN, MyColors.DARK_GRAY);

    public MasterRing() {
        super("Master Ring", 18);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MasterRing();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public boolean supportsHigherTier() {
        return false;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public String getExtraText() {
        return ", Levels of mastery are attained " + MyStrings.numberWord(MASTERY_FACTORY) + " times as fast.";
    }
}
