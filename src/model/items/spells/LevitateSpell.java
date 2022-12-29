package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LevitateSpell extends Spell {
    private static final Sprite SPRITE = new ItemSprite(15, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public LevitateSpell() {
        super("Levitate", 12, MyColors.BLUE, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return "A spell for moving an object through the air via telekinetic means.";
    }

    @Override
    public Item copy() {
        return new LevitateSpell();
    }
}
