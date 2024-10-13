package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class ApprenticeRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(6, 9, MyColors.LIGHT_YELLOW, MyColors.ORC_GREEN, MyColors.LIGHT_GREEN);
    private static final double BASE_EXPERIENCE_FACTOR = 1.2;

    public ApprenticeRing() {
        super("Apprentice Ring", 12);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ApprenticeRing();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public double getExperienceFactor() {
        return BASE_EXPERIENCE_FACTOR;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HigherTierApprenticeRing(this, tier);
    }

    private static class HigherTierApprenticeRing extends HigherTierAccessory {
        public HigherTierApprenticeRing(ApprenticeRing apprenticeRing, int tier) {
            super(apprenticeRing, tier);
        }

        @Override
        public double getExperienceFactor() {
            return BASE_EXPERIENCE_FACTOR + 0.1 * getTier();
        }

        @Override
        public List<MyPair<Skill, Integer>> getSkillBonuses() {
            return List.of(new MyPair<>(Skill.Logic, getTier()));
        }
    }
}
