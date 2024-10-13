package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class MasterRing extends JewelryItem {

    private static final int MASTERY_FACTORY = 3;
    private static final Sprite SPRITE = new ItemSprite(8, 9,
            MyColors.GOLD, MyColors.ORANGE, MyColors.GRAY_RED);

    public MasterRing() {
        super("Master Ring", 18);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MasterRing();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public boolean supportsHigherTier() {
        return false;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public int getMasteryFactor() {
        return MASTERY_FACTORY;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HigherTierMasterRing(this, tier);
    }

    private static class HigherTierMasterRing extends HigherTierAccessory {
        public HigherTierMasterRing(MasterRing masterRing, int tier) {
            super(masterRing, tier);
        }

        @Override
        public int getMasteryFactor() {
            return getTier() + MASTERY_FACTORY;
        }

        @Override
        public List<MyPair<Skill, Integer>> getSkillBonuses() {
            return List.of(new MyPair<>(Skill.SpellCasting, getTier()));
        }
    }
}
