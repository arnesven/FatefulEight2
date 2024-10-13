package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;


public class GreedyRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(6, 9, MyColors.LIGHT_YELLOW, MyColors.PURPLE, MyColors.PINK);
    private static final double BASE_GOLD_FACTOR = 1.2;

    public GreedyRing() {
        super("Greedy Ring", 18);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new GreedyRing();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public double getGoldFromLootFactor() {
        return BASE_GOLD_FACTOR;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HigherTierGreedyRing((GreedyRing)copy(), tier);
    }

    private static class HigherTierGreedyRing extends HigherTierAccessory {
        public HigherTierGreedyRing(GreedyRing apprenticeRing, int tier) {
            super(apprenticeRing, tier);
        }

        @Override
        public double getExperienceFactor() {
            return BASE_GOLD_FACTOR + 0.1 * getTier();
        }
    }
}
