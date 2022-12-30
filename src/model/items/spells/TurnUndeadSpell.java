package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TurnUndeadSpell extends Spell {
    private static final Sprite SPRITE = new ItemSprite(4, 8, MyColors.BROWN, MyColors.WHITE, MyColors.GOLD);

    public TurnUndeadSpell() {
        super("Turn Undead", 20, MyColors.WHITE, 10, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return "A spell for disintegrating undead.";
    }

    @Override
    public Item copy() {
        return new TurnUndeadSpell();
    }
}
