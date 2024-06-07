package model.items.accessories;

import model.classes.Skill;
import model.combat.conditions.Condition;
import model.combat.conditions.PoisonCondition;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class GardeningGloves extends GlovesItem {

    private static final Sprite SPRITE = new ItemSprite(14, 13, MyColors.ORANGE, MyColors.BROWN, MyColors.YELLOW);

    public GardeningGloves() {
        super("Gardening Gloves", 96);
    }

    @Override
    public boolean grantsConditionImmunity(Condition cond) {
        return cond instanceof PoisonCondition;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicGreen, 1));
    }

    @Override
    public String getExtraText() {
        return "Grants the wearer immunity toward poison.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public Item copy() {
        return new GardeningGloves();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
