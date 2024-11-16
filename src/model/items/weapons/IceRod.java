package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.FreezeImbuement;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class IceRod extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.LIGHT_PINK, MyColors.CYAN, MyColors.LIGHT_BLUE);

    public IceRod() {
        super("Ice Rod", 44, Skill.MagicBlue, new int[]{9, 11, 12, 14});
        setImbuement(new FreezeImbuement());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new IceRod();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(0, 16, MyColors.LIGHT_PINK, MyColors.CYAN, MyColors.LIGHT_BLUE);
    }
}
