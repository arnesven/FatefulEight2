package model.items.accessories;

import model.combat.conditions.BleedingCondition;
import model.combat.conditions.Condition;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CowlOfRegeneration extends HeadGearItem {

    private static final Sprite SPRITE = new ItemSprite(14, 10, MyColors.DARK_BROWN, MyColors.BROWN);

    public CowlOfRegeneration() {
        super("Cowl of Regeneration", 84);
    }

    @Override
    public boolean grantsConditionImmunity(Condition cond) {
        return cond instanceof BleedingCondition;
    }

    @Override
    public String getExtraText() {
        return "Grants the wearer immunity toward bleeding.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getHealthBonus() {
        return 2;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public Item copy() {
        return new CowlOfRegeneration();
    }

    @Override
    public int getAP() {
        return 1;
    }
}
