package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class DispelSpell extends QuestSpell {
    private static final Sprite SPRITE = new ItemSprite(14, 8, MyColors.BROWN, MyColors.PEACH, MyColors.BLACK);

    public DispelSpell() {
        super("Dispel", 12, COLORLESS, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DispelSpell();
    }

    @Override
    public String getDescription() {
        return "A spell for dissipating enchantments, curses and conjurations.";
    }
}
