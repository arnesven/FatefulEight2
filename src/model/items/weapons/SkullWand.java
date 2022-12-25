package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SkullWand extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(6, 6, MyColors.BROWN, MyColors.BEIGE);

    public SkullWand() {
        super("Skull Wand", 28, Skill.MagicBlack, new int[]{9,11,14,14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SkullWand();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
