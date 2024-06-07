package model.items.accessories;

import model.combat.conditions.Condition;
import model.combat.conditions.ParalysisCondition;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BootsOfMobility extends ShoesItem {
    private static final Sprite SPRITE = new ItemSprite(13, 13, MyColors.DARK_BROWN, MyColors.RED, MyColors.DARK_RED);

    public BootsOfMobility() {
        super("Boots of Mobility", 120);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean grantsConditionImmunity(Condition cond) {
        return cond instanceof ParalysisCondition;
    }

    @Override
    public int getSpeedModifier() {
        return 2;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public Item copy() {
        return new BootsOfMobility();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
