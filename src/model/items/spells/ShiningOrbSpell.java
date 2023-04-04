package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ShiningOrbSpell extends QuestSpell {
    private static final Sprite SPRITE = new ItemSprite(3, 8, MyColors.BROWN, MyColors.WHITE, MyColors.GOLD);
    // TODO: add this spell to some quests.
    public ShiningOrbSpell() {
        super("Shining Orb", 12, MyColors.WHITE, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ShiningOrbSpell();
    }

    @Override
    public String getDescription() {
        return "Conjures a bright shining ball of pure light.";
    }
}
