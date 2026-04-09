package model.states.mine;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class MaterialsOreObject extends MineOreObject {
    private static final Sprite[] SPRITES = MineObject.makeOreSprites(MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.GOLD);
    private static final Sprite DEBRIS = new Sprite32x32("rockdebris",
            "warehouse.png", 0x43, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.YELLOW, MyColors.GRAY);

    private final int richness;

    public MaterialsOreObject(int richness) {
        super(MineOreObject.qualityForRichness(richness) + "Iron Ore", 3 + richness,
                SPRITES[richness * SPRITES.length/3 + MyRandom.randInt(SPRITES.length/3)]);
        this.richness = richness;
    }

    @Override
    public void giveReward(Model model, AdvancedMineEvent advancedMineEvent) {
        int materials = 0;
        for (int i = 0; i < richness; ++i) {
            materials += MyRandom.rollD6();
        }
        advancedMineEvent.println("The party gains " + materials + " materials.");
        model.getParty().getInventory().addToMaterials(materials);
    }

    @Override
    public MyColors getAnimationColor() {
        return MyColors.GRAY_RED;
    }

    @Override
    public MineObject copy() {
        return new MaterialsOreObject(richness);
    }

    @Override
    public Sprite getDebrisSprite() {
        return DEBRIS;
    }
}
