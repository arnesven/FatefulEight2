package model.items.spells;

import model.items.Item;
import view.sprites.ColorlessSpellSprite;
import view.sprites.Sprite;

public class TelekinesisSpell extends QuestSpell {
    private static final Sprite SPRITE = new ColorlessSpellSprite(7, false);

    public TelekinesisSpell() {
        super("Telekinesis", 12, COLORLESS, 7, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TelekinesisSpell();
    }

    @Override
    public String getDescription() {
        return "Let's the caster move objects without touching them";
    }
}
