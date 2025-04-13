package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.*;

public class LevitateSpell extends QuestSpell {
    private static final Sprite SPRITE = new BlueSpellSprite(0, false);

    public LevitateSpell() {
        super("Levitate", 24, MyColors.BLUE, 10, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "A spell for moving objects through the air via telekinesis.";
    }

    @Override
    public Item copy() {
        return new LevitateSpell();
    }
}
