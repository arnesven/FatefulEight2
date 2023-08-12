package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import model.items.spells.Spell;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.SuperiorItemSprite;

import java.util.ArrayList;
import java.util.List;

public class SuperiorAccessory extends Accessory {
    private static final int COST_MULTIPLIER = 3;
    private final Accessory inner;
    private final SuperiorItemSprite sprite;

    public SuperiorAccessory(Accessory inner) {
        super("Superior " + inner.getName(), inner.getCost() * COST_MULTIPLIER);
        this.inner = inner;
        this.sprite = new SuperiorItemSprite(inner.getSpriteForHigherTier());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new SuperiorAccessory((Accessory)inner.copy());
    }

    @Override
    public String getSound() {
        return inner.getSound();
    }

    @Override
    public int getAP() {
        if (inner.getAP() > 0) {
            return inner.getAP() + 1;
        }
        return 0;
    }

    @Override
    public boolean isHeavy() {
        return inner.isHeavy();
    }

    public String getExtraText() {
        return inner.getExtraText();
    }

    public int[] getDamageTable() { return inner.getDamageTable(); }

    public int getHealthBonus() {
        if (inner.getHealthBonus() > 0) {
            return inner.getHealthBonus() + 2;
        }
        return 0;
    }

    public int getSPBonus() {
        if (inner.getSPBonus() > 0) {
            return inner.getSPBonus() + 1;
        }
        return 0;
    }

    @Override
    public int getSpeedModifier() {
        if (inner.getSpeedModifier() > 0) {
            return inner.getSpeedModifier() + 2;
        }
        return 0;
    }

    public int getSpellDiscount(Spell sp) { return inner.getSpellDiscount(sp); }


    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> bonuses = inner.getSkillBonuses();
        for (MyPair<Skill, Integer> pair : bonuses) {
            pair.second += 1;
        }
        return bonuses;
    }
}
