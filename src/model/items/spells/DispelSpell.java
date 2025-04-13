package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ColorlessSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class DispelSpell extends QuestSpell {
    private static final Sprite SPRITE = new ColorlessSpellSprite(0, false);

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
