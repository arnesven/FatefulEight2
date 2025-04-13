package model.items.spells;

import model.items.Item;
import view.MyColors;
import view.sprites.RedSpellSprite;
import view.sprites.Sprite;

public class FireworksSpell extends QuestSpell {
    private static final Sprite SPRITE = new RedSpellSprite(0, false);

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
