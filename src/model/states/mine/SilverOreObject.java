package model.states.mine;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;

public class SilverOreObject extends MineOreObject {
    private static final Sprite[] SPRITES = MineObject.makeOreSprites(MyColors.DARK_GRAY, MyColors.GRAY, MyColors.LIGHT_GRAY);
    private final int richness;

    public SilverOreObject(int richness) {
        super(MineOreObject.qualityForRichness(richness) + "Silver Ore", 4 + 2 * richness,
                SPRITES[richness * SPRITES.length/3 + MyRandom.randInt(SPRITES.length/3)]);
        this.richness = richness;
    }

    @Override
    public void giveReward(Model model, AdvancedMineEvent advancedMineEvent) {
        int obols = MyRandom.rollObD6((richness + 1) * (richness + 1));
        advancedMineEvent.println("The party gains " + obols + " obols.");
        model.getParty().addToObols(obols);
    }

    @Override
    public MyColors getAnimationColor() {
        return MyColors.GRAY;
    }

    @Override
    public MineObject copy() {
        return new SilverOreObject(richness);
    }
}
