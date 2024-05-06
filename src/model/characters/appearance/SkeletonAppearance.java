package model.characters.appearance;

import model.races.Race;
import model.races.Shoulders;
import model.races.SkeletonRace;
import view.MyColors;
import view.ScreenHandler;

public class SkeletonAppearance extends AdvancedAppearance {

    private final Shoulders innerShoulders;

    public SkeletonAppearance(Shoulders innerShoulders, boolean gender) {
        super(new SkeletonRace(), gender, MyColors.WHITE,
                0x148, 0x128, SkeletonRace.EYES, new BaldHairStyle(), null);
        this.innerShoulders = innerShoulders;
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
    public Shoulders getShoulders() {
        return innerShoulders;
    }

    @Override
    protected void specialization() {
        innerShoulders.makeSkeleton(this);
    }

    @Override
    public void applyDetail(Race race, boolean coversEars) { }

    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) { }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) { }
}
