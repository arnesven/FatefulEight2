package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class DispellSpell extends QuestSpell {
    private static final Sprite SPRITE = new ItemSprite(14, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public DispellSpell() {
        super("Dispell", 12, MyColors.BLUE, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DispellSpell();
    }

    @Override
    public String getDescription() {
        return "A spell for dissipating enchantments, curses and conjurations.";
    }
}
