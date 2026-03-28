package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class InitiatesRobes extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(13, 2, MyColors.DARK_PURPLE, MyColors.DARK_GRAY, MyColors.DARK_GRAY);

    public InitiatesRobes() {
        super("Initiate's Robes", 8, 0, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getMP() {
        return 1;
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public Item copy() {
        return new InitiatesRobes();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
