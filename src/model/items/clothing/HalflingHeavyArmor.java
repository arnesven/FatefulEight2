package model.items.clothing;

import model.Model;
import model.classes.Skill;
import model.items.Item;
import model.items.analysis.ItemAnalysis;
import util.MyPair;
import view.sprites.Sprite;

import java.util.List;

public class HalflingHeavyArmor extends HeavyArmorClothing {
    public static final int CONVERT_BACK_MATERIALS_COST = 5;
    public static final int CONVERT_SKILL_DIFFICULTY = 6;
    private final Clothing inner;

    public HalflingHeavyArmor(Clothing armor) {
        super("HL " + armor.getName(), armor.getCost(), armor.getAP());
        this.inner = armor;
    }

    @Override
    public boolean isHalflingArmor() {
        return true;
    }

    @Override
    protected Sprite getSprite() {
        return inner.getSpriteForHigherTier(0);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return inner.getSkillBonuses();
    }

    @Override
    public int getWeight() {
        return inner.getWeight() / 2;
    }

    @Override
    public boolean isCraftable() {
        return inner.isCraftable();
    }

    @Override
    public boolean isSellable() {
        return inner.isSellable();
    }

    @Override
    public String getShoppingDetails() {
        return inner.getShoppingDetails() + ", Halfling Only";
    }

    @Override
    public String getSound() {
        return inner.getSound();
    }

    @Override
    public boolean isAnalyzable() {
        return inner.isAnalyzable();
    }

    @Override
    public List<ItemAnalysis> getAnalyses(Model model) {
        return inner.getAnalyses(model);
    }


    @Override
    public boolean supportsHigherTier() {
        return inner.supportsHigherTier();
    }

    @Override
    public int getMP() {
        return inner.getMP();
    }

    @Override
    public Item copy() {
        return new HalflingHeavyArmor(inner);
    }

    public Clothing getInnerItem() {
        return inner;
    }
}
