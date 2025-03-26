package model.items.potions;

import model.Model;
import model.items.Item;
import model.races.Halfling;
import model.races.Race;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RumPotion extends IntoxicatingPotion {

    private static final Sprite SPRITE = new ItemSprite(15, 16,
            MyColors.DARK_GREEN, MyColors.DARK_RED, MyColors.BLACK);

    public RumPotion() {
        super("Rum", 3, "halflings");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RumPotion();
    }

    @Override
    protected int getStrength() {
        return 8;
    }

    @Override
    public boolean doesReject(Model model, Race race) {
        return race instanceof Halfling;
    }
}
