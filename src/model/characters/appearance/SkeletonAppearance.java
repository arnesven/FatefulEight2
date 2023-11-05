package model.characters.appearance;

import model.races.Race;
import model.races.Shoulders;
import model.races.SkeletonRace;
import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.PortraitSprite;

public class SkeletonAppearance extends AdvancedAppearance {

    private static final PortraitSprite SHOULDER_LEFT_TOP = new FaceAndClothesSprite(0x18A, MyColors.CYAN);
    private static final PortraitSprite SHOULDER_TOP = new FaceAndClothesSprite(0x18B, MyColors.CYAN);
    private static final PortraitSprite NECK = new FaceAndClothesSprite(0x18C, MyColors.CYAN);
    private static final PortraitSprite BROAD_SHOULDER_RIGHT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x18D, MyColors.CYAN);
    private static final PortraitSprite SHOULDER_RIGHT_TOP = new FaceAndClothesSprite(0x18E, MyColors.CYAN);

    private static final PortraitSprite SHOULDER_LEFT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x19A, MyColors.CYAN);
    private static final PortraitSprite BROAD_SHOULDER_LEFT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x19B, MyColors.CYAN);
    private static final PortraitSprite RIBS_EXTENSION = new FaceAndClothesSprite(0x19C, MyColors.CYAN);
    private static final PortraitSprite BROAD_LR_CORNER = new FaceAndClothesSprite(0x19D, MyColors.CYAN);
    private static final PortraitSprite SHOULDER_RIGHT_TOP_WITH_FRAME = new FaceAndClothesSprite(0x19E, MyColors.CYAN);

    private static final PortraitSprite LL_CORNER = new FaceAndClothesSprite(0x1AA, MyColors.CYAN);
    private static final PortraitSprite RIBS_LEFT = new FaceAndClothesSprite(0x1AB, MyColors.CYAN);
    private static final PortraitSprite RIBS_MIDDLE = new FaceAndClothesSprite(0x1AC, MyColors.CYAN);
    private static final PortraitSprite RIBS_RIGHT = new FaceAndClothesSprite(0x1AD, MyColors.CYAN);
    private static final PortraitSprite LR_CORNER = new FaceAndClothesSprite(0x1AE, MyColors.CYAN);

    private static final PortraitSprite BROAD_LL_CORNER = new FaceAndClothesSprite(0x1BA, MyColors.CYAN);
    private static final PortraitSprite NARROW_SHOULDER_LEFT = new FaceAndClothesSprite(0x1BB, MyColors.CYAN);
    private static final PortraitSprite NARROW_SHOULDER_RIGHT = new FaceAndClothesSprite(0x1BC, MyColors.CYAN);

    private final Race innerRace;

    public SkeletonAppearance(Race innerRace, boolean gender) {
        super(new SkeletonRace(innerRace), gender, MyColors.WHITE,
                0x148, 0x128, SkeletonRace.EYES, new BaldHairStyle(), null);
        this.innerRace = innerRace;
    }

    protected int getRightCheek() {
        return 0x199;
    }

    protected int getLeftCheek() {
        return 0x197;
    }

    protected int getForeheadRight() {
        return 0x196;
    }

    protected int getForeheadCenter() {
        return 0xFF;
    }

    protected int getForeheadLeft() {
        return 0x186;
    }

    protected int getHeadTopRight() {
        return 0x11;
    }

    protected int getHeadTop() {
        return 0xFE;
    }

    protected int getHeadTopLeft() {
        return 0x01;
    }

    @Override
    protected void specialization() {
        if (innerRace.getShoulders() == Shoulders.BROAD) {
            setRow(5, new PortraitSprite[]{BROAD_SHOULDER_LEFT_TOP_WITH_FRAME, SHOULDER_TOP, SHOULDER_TOP, NECK, SHOULDER_TOP, SHOULDER_TOP, BROAD_SHOULDER_RIGHT_TOP_WITH_FRAME});
            setRow(6, new PortraitSprite[]{BROAD_LL_CORNER, RIBS_LEFT, RIBS_EXTENSION, RIBS_MIDDLE, RIBS_EXTENSION, RIBS_RIGHT, BROAD_LR_CORNER});
        } else if (innerRace.getShoulders() == Shoulders.NARROW) {
            setRow(5, new PortraitSprite[]{PortraitSprite.FRAME_LEFT, SHOULDER_LEFT_TOP, SHOULDER_TOP, NECK, SHOULDER_TOP, SHOULDER_RIGHT_TOP, PortraitSprite.FRAME_RIGHT});
            setRow(6, new PortraitSprite[]{PortraitSprite.FRAME_LL_CORNER, NARROW_SHOULDER_LEFT, RIBS_LEFT, RIBS_MIDDLE, RIBS_RIGHT, NARROW_SHOULDER_RIGHT, PortraitSprite.FRAME_LR_CORNER});
        } else {
            setRow(5, new PortraitSprite[]{SHOULDER_LEFT_TOP_WITH_FRAME, SHOULDER_TOP, SHOULDER_TOP, NECK, SHOULDER_TOP, SHOULDER_TOP, SHOULDER_RIGHT_TOP_WITH_FRAME});
            setRow(6, new PortraitSprite[]{LL_CORNER, RIBS_LEFT, RIBS_EXTENSION, RIBS_MIDDLE, RIBS_EXTENSION, RIBS_RIGHT, LR_CORNER});
        }
    }
}
