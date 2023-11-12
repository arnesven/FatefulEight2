package model.races;

import view.sprites.NakedClothesSprite;
import view.sprites.PortraitSprite;

public class SlenderShoulders extends Shoulders {

    private final PortraitSprite SLENDER_SHOULDER_LEFT_TOP = new NakedClothesSprite(0x3F);
    private final PortraitSprite SLENDER_SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x4F);
    private final PortraitSprite SLENDER_SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x5F);
    private final PortraitSprite SLENDER_SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x6F);
    private final PortraitSprite SLENDER_ARM_LEFT = new NakedClothesSprite(0xF1);
    private final PortraitSprite SLENDER_ARM_RIGHT = new NakedClothesSprite(0xF2);

    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        grid[0][5] = SLENDER_SHOULDER_LEFT_TOP;
        grid[6][5] = SLENDER_SHOULDER_RIGHT_TOP;
        grid[0][6] = SLENDER_SHOULDER_LEFT_BOTTOM;
        grid[6][6] = SLENDER_SHOULDER_RIGHT_BOTTOM;
        grid[1][5] = SHOULDER_TOP;
        grid[1][6] = SLENDER_ARM_LEFT;
        grid[5][5] = SHOULDER_TOP;
        grid[5][6] = SLENDER_ARM_RIGHT;
    }
}
