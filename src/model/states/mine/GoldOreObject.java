package model.states.mine;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;

public class GoldOreObject extends MineOreObject {

    private static final Sprite[] SPRITES = MineObject.makeOreSprites(MyColors.DARK_GRAY, MyColors.GRAY, MyColors.YELLOW);
    private final int richness;

    public GoldOreObject(int richness) {
        super(MineOreObject.qualityForRichness(richness) + "Gold Ore", 6 + 2 * richness,
                SPRITES[richness * SPRITES.length/3 + MyRandom.randInt(SPRITES.length/3)]);
        this.richness = richness;
    }

    @Override
    public void giveReward(Model model, AdvancedMineEvent advancedMineEvent) {
        int gold = MyRandom.rollObD6(2 * richness + 1);
        advancedMineEvent.println("The party gains " + gold + " gold.");
        model.getParty().earnGold(gold);
    }

    @Override
    public MyColors getAnimationColor() {
        return MyColors.GRAY;
    }

    @Override
    public MineObject copy() {
        return new GoldOreObject(richness);
    }
}
