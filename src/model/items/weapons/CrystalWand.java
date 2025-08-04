package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CrystalWand extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.ORC_GREEN, MyColors.WHITE, MyColors.CYAN);

    public CrystalWand() {
        super("Crystal Wand", 150, Skill.MagicAny, new int[]{9, 11, 12, 14});
        // FEATURE: Add some fun imbuement.
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CrystalWand();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }
}
