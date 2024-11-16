package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.BurningImbuement;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class FireRod extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.YELLOW, MyColors.ORANGE, MyColors.PEACH);

    public FireRod() {
        super("Fire Rod", 36, Skill.MagicRed, new int[]{9, 11, 12, 14});
        setImbuement(new BurningImbuement());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FireRod();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(0, 16, MyColors.YELLOW, MyColors.ORANGE, MyColors.PEACH);
    }
}
