package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class HarmonizeSpell extends Spell {
    private static final Sprite SPRITE = new ItemSprite(0, 8, MyColors.BEIGE, MyColors.GREEN);

    public HarmonizeSpell() {
        super("Harmonize", 20, MyColors.GREEN, 10, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "A spell for calming bests.";
    }

    @Override
    public Item copy() {
        return new HarmonizeSpell();
    }
}
