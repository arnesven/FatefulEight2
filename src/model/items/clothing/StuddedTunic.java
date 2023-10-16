package model.items.clothing;

import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class StuddedTunic extends Clothing {

    private Sprite sprite;

    public StuddedTunic() {
        super("Studded Tunic", 20, 2, false);
        sprite = new ItemSprite(0, 2, MyRandom.sample(List.of(MyColors.BLUE, MyColors.GRAY_RED, MyColors.DARK_RED, MyColors.DARK_GREEN)),
                MyColors.GOLD);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return 1500;
    }

    @Override
    public Item copy() {
        return new StuddedTunic();
    }
}
