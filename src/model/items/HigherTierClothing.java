package model.items;

import model.classes.Skill;
import model.items.clothing.Clothing;
import model.items.spells.Spell;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.HigherTierItemSprite;

import java.util.List;

public class HigherTierClothing extends Clothing implements HigherTierItem {
    private final Clothing innerItem;
    private final HigherTierItemSprite sprite;
    private final int tier;

    public HigherTierClothing(Clothing innerItem, int tier) {
        super(Item.getHigherTierPrefix(tier) + " " + innerItem.getName(),
                innerItem.getCost()*(tier*2+1), innerItem.getAP() + tier, innerItem.isHeavy());
        this.innerItem = innerItem;
        this.sprite = new HigherTierItemSprite(innerItem.getSpriteForHigherTier(tier), tier);
        this.tier = tier;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getWeight() {
        return innerItem.getWeight();
    }

    @Override
    public Item copy() {
        return new HigherTierClothing((Clothing) innerItem.copy(), tier);
    }

    @Override
    public String getSound() {
        return innerItem.getSound();
    }

    @Override
    public int getSpeedModifier() {
        if (innerItem.getSpeedModifier() > 0) {
            return innerItem.getSpeedModifier() + 2*tier;
        }
        return 0;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> bonuses = innerItem.getSkillBonuses();
        for (MyPair<Skill, Integer> pair : bonuses) {
            if (pair.second > 0) {
                pair.second += 1 * tier;
            }
        }
        return bonuses;
    }

    @Override
    public Prevalence getPrevalence() {
        return innerItem.getPrevalence();
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public Item getInnerItem() {
        return innerItem;
    }
}
