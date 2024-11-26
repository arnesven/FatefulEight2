package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.AbsorptionImbuement;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SkullWand extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(6, 6, MyColors.BROWN, MyColors.BEIGE);

    public SkullWand() {
        super("Skull Wand", 3, Skill.MagicBlack, new int[]{8,11,13,14});
        setImbuement(new AbsorptionImbuement());
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

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(14, 15, MyColors.BROWN, MyColors.BEIGE);
    }
}
