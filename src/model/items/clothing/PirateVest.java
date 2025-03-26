package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class PirateVest extends Clothing {

    private static final List<MyColors> COLORS = List.of(MyColors.DARK_GREEN, MyColors.DARK_RED,
            MyColors.GOLD, MyColors.BROWN, MyColors.DARK_BLUE);
    private final ItemSprite sprite;

    public PirateVest() {
        super("Pirate Vest", 8, 1, false);
        MyColors color = MyRandom.sample(COLORS);
        this.sprite = new ItemSprite(2, 17, color, MyColors.DARK_BROWN);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new PirateVest();
    }

    @Override
    public int getWeight() {
        return 400;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }
}
