package model.races;

import view.sprites.NakedClothesSprite;
import view.sprites.PortraitSprite;

public class NormalShoulders extends Shoulders {

    private final PortraitSprite SHOULDER_LEFT_TOP = new NakedClothesSprite(0x00);
    private final PortraitSprite SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x01);

    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        grid[0][5] = SHOULDER_LEFT_TOP;
        grid[6][5] = SHOULDER_RIGHT_TOP;

        grid[0][6] = SHOULDER_LEFT_BOTTOM;
        grid[6][6] = SHOULDER_RIGHT_BOTTOM;

        grid[1][5] = SHOULDER_TOP;
        grid[1][6] = FILLED_BLOCK_CLOTHES;
        grid[5][5] = SHOULDER_TOP;
        grid[5][6] = FILLED_BLOCK_CLOTHES;
    }
}
