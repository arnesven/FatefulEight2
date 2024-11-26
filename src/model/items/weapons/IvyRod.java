package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.PoisonImbuement;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class IvyRod extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(14, 11,
            MyColors.PEACH, MyColors.DARK_GREEN, MyColors.GOLD);

    public IvyRod() {
        super("Ivy Rod", 11, Skill.MagicGreen, new int[]{8,10,13});
        setImbuement(new PoisonImbuement());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new IvyRod();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(1, 16, MyColors.PEACH, MyColors.DARK_GREEN, MyColors.GOLD);
    }

}
