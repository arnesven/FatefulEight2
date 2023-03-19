package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TurnUndeadSpell extends QuestSpell {
    private static final Sprite SPRITE = new ItemSprite(4, 8, MyColors.BROWN, MyColors.WHITE, MyColors.GOLD);

    public TurnUndeadSpell() {
        super("Turn Undead", 20, MyColors.WHITE, 10, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "A spell for disintegrating undead.";
    }

    @Override
    public Item copy() {
        return new TurnUndeadSpell();
    }
}
