package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class FireworksSpell extends QuestSpell {
    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);

    public FireworksSpell() {
        super("Fireworks", 12, MyColors.RED, 9, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FireworksSpell();
    }

    @Override
    public String getDescription() {
        return "Conjures a breathtaking display of pyrotechnics.";
    }
}
