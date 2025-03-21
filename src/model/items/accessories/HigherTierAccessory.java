package model.items.accessories;

import model.classes.Skill;
import model.items.HigherTierItem;
import model.items.Item;
import model.items.Prevalence;
import model.items.spells.Spell;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.HigherTierItemSprite;

import java.util.ArrayList;
import java.util.List;

public class HigherTierAccessory extends Accessory implements HigherTierItem {
    private final Accessory inner;
    private final HigherTierItemSprite sprite;
    private final int tier;

    public HigherTierAccessory(Accessory inner, int tier) {
        super(Item.getHigherTierPrefix(tier) + " " + inner.getName(),
                inner.getCost()*(tier*2+1));
        this.inner = inner;
        this.sprite = new HigherTierItemSprite(inner.getSpriteForHigherTier(tier), tier);
        this.tier = tier;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return inner.getWeight();
    }

    @Override
    public Item copy() {
        return new HigherTierAccessory((Accessory)inner.copy(), tier);
    }

    @Override
    public String getSound() {
        return inner.getSound();
    }

    @Override
    public int getAP() {
        if (inner.getAP() > 0) {
            return inner.getAP() + tier;
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
            return inner.getHealthBonus() + 2*tier;
        }
        return 0;
    }

    public int getSPBonus() {
        if (inner.getSPBonus() > 0) {
            return inner.getSPBonus() + 1*tier;
        }
        return 0;
    }

    @Override
    public int getSpeedModifier() {
        if (inner.getSpeedModifier() > 0) {
            return inner.getSpeedModifier() + 2*tier;
        }
        return 0;
    }

    public int getSpellDiscount(Spell sp) { return inner.getSpellDiscount(sp); }


    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> bonuses = new ArrayList<>(inner.getSkillBonuses());
        for (MyPair<Skill, Integer> pair : bonuses) {
            pair.second += tier;
        }
        bonuses.removeIf(p -> p.second == 0);
        return bonuses;
    }

    @Override
    public boolean isOffHandItem() {
        return inner.isOffHandItem();
    }

    @Override
    public Prevalence getPrevalence() {
        return inner.getPrevalence();
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public Item getInnerItem() {
        return inner;
    }

    @Override
    public int getMasteryFactor() {
        return inner.getMasteryFactor();
    }

    @Override
    public double getExperienceFactor() {
        return inner.getExperienceFactor();
    }

    @Override
    public double getGoldFromLootFactor() {
        return inner.getGoldFromLootFactor();
    }
}
