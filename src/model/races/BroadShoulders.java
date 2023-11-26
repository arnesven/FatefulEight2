package model.races;

import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SkeletonAppearance;
import view.MyColors;
import view.sprites.*;

public class BroadShoulders extends Shoulders {

    private final PortraitSprite BROAD_SHOULDER_LEFT_TOP = new NakedClothesSprite(0x5D);
    private final PortraitSprite BROAD_SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x5E);

    private static final PortraitSprite BROAD_SHOULDER_RIGHT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x18D, MyColors.CYAN);
    private static final PortraitSprite BROAD_SHOULDER_LEFT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x19B, MyColors.CYAN);

    private static final PortraitSprite BROAD_LR_CORNER = new FaceAndClothesSprite(0x19D, MyColors.CYAN);
    private static final PortraitSprite BROAD_LL_CORNER = new FaceAndClothesSprite(0x1BA, MyColors.CYAN);

    public BroadShoulders(boolean gender) {
        super(gender);
    }

    @Override
    public void makeNaked(PortraitSprite[][] grid) {
        super.makeNaked(grid);
        grid[0][5] = BROAD_SHOULDER_LEFT_TOP;
        grid[6][5] = BROAD_SHOULDER_RIGHT_TOP;
    }

    public PortraitSprite makeLeftTopSprite(MyColors color) {
        return new ClothesSprite(0x5D, color);
    }

    public PortraitSprite makeRightTopSprite(MyColors color) {
        return new ClothesSprite(0x5E, color);
    }

    public void putOnHideRight(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(5, 5, new FaceAndClothesSprite(0xE8, clothingColor));
        characterAppearance.setSprite(6, 5, new FaceAndClothesSprite(0x16F, clothingColor));
        characterAppearance.setSprite(5, 6, new FaceAndClothesSprite(0xF8, clothingColor));
        characterAppearance.setSprite(6, 6, new FaceAndClothesSprite(0xF9, clothingColor));
    }

    public void putOnHideLeft(CharacterAppearance characterAppearance, MyColors clothingColor) {
        characterAppearance.setSprite(0, 5, new FaceAndClothesSprite(0x15F, clothingColor));
        characterAppearance.setSprite(1, 5, new FaceAndClothesSprite(0xC8, clothingColor));
        characterAppearance.setSprite(0, 6, new FaceAndClothesSprite(0xD7, clothingColor));
        characterAppearance.setSprite(1, 6, new FaceAndClothesSprite(0xD8, clothingColor));
    }

    public void makeSkeleton(SkeletonAppearance appearance) {
        appearance.setRow(5, new PortraitSprite[]{BROAD_SHOULDER_LEFT_TOP_WITH_FRAME, SKELETON_SHOULDER_TOP, SKELETON_SHOULDER_TOP, NECK, SKELETON_SHOULDER_TOP, SKELETON_SHOULDER_TOP, BROAD_SHOULDER_RIGHT_TOP_WITH_FRAME});
        appearance.setRow(6, new PortraitSprite[]{BROAD_LL_CORNER, RIBS_LEFT, RIBS_EXTENSION, RIBS_MIDDLE, RIBS_EXTENSION, RIBS_RIGHT, BROAD_LR_CORNER});
    }
}
