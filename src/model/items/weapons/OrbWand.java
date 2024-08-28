package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class OrbWand extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(4, 6, MyColors.BROWN, MyColors.BEIGE);

    public OrbWand() {
        super("Orb Wand", 20, Skill.MagicWhite, new int[]{9, 11, 14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OrbWand();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(12, 15, MyColors.BROWN, MyColors.BEIGE);
    }
}
