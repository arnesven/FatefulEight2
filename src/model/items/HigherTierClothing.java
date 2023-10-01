package model.items;

import model.classes.Skill;
import model.items.clothing.Clothing;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.HigherTierItemSprite;

import java.util.List;

public class HigherTierClothing extends Clothing {
    private static final int COST_MULTIPLIER = 3;
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
    public Item copy() {
        return new HigherTierClothing((Clothing) innerItem.copy(), tier);
    }

    @Override
    public String getShoppingDetails() {
        return innerItem.getShoppingDetails();
    }

    @Override
    public String getSound() {
        return innerItem.getSound();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return innerItem.getSkillBonuses();
    }

    @Override
    public int getSpeedModifier() {
        return innerItem.getSpeedModifier();
    }

    @Override
    public Prevalence getPrevalence() {
        return innerItem.getPrevalence();
    }
}
