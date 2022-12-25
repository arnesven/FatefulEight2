package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class AdvancedAppearance extends CharacterAppearance {
    private final int mouth;
    private final int nose;
    private final CharacterEyes eyes;
    private final HairStyle hairStyle;
    private final Beard beard;

    public AdvancedAppearance(Race race, boolean femaleGender, MyColors hairColor,
                              int mouth, int nose, CharacterEyes eyes, HairStyle hair,
                              Beard beard) {
        super(race, femaleGender, hairColor);
        this.mouth = mouth;
        this.nose = nose;
        this.eyes = eyes;
        this.hairStyle = hair;
        this.beard = beard;
    }

    @Override
    protected final int getMouth() {
        return 0x50 + mouth;
    }

    @Override
    protected final int getEye() {
        return eyes.getEye();
    }

    @Override
    protected final boolean symmetricalEyes() {
        return eyes.areSymmetrical();
    }

    @Override
    protected final int getLeftEye() {
        return eyes.getLeftEye();
    }

    @Override
    protected final int getRightEye() {
        return eyes.getRightEye();
    }

    @Override
    public final int getNose() {
        return 0x60 + nose;
    }

    @Override
    public boolean hairInForehead() {
        if (hairStyle == null) {
            return false;
        }
        return hairStyle.hairInForhead();
    }

    @Override
    public boolean hairOnTop() {
        if (hairStyle == null) {
            return false;
        }
        return hairStyle.hairOnTop();
    }

    @Override
    protected int getForeheadLeft() {
        if (hairStyle == null) {
            return super.getForeheadLeft();
        }
        return hairStyle.getForeheadLeft();
    }

    @Override
    protected int getForeheadCenter() {
        if (hairStyle == null) {
            return super.getForeheadCenter();
        }
        return hairStyle.getForeheadCenter();
    }

    @Override
    protected int getForeheadRight() {
        if (hairStyle == null) {
            return super.getForeheadRight();
        }
        return hairStyle.getForeheadRight();
    }

    protected int getHeadTopLeft() {
        if (hairStyle == null) {
            return super.getHeadTopLeft();
        }
        return hairStyle.getHeadTopLeft();
    }

    protected int getHeadTop() {
        if (hairStyle == null) {
            return super.getHeadTop();
        }
        return hairStyle.getHeadTop();
    }

    protected int getHeadTopRight() {
        if (hairStyle == null) {
            return super.getHeadTopRight();
        }
        return hairStyle.getHeadTopRight();
    }

    @Override
    protected int getLeftCheek() {
        if (beard == null) {
            return super.getLeftCheek();
        }
        return beard.getLeftCheck();
    }

    @Override
    protected int getRightCheek() {
        if (beard == null) {
            return super.getRightCheek();
        }
        return beard.getRightCheek();
    }

    @Override
    public boolean hasBeard() {
        return beard != null;
    }

    @Override
    public CharacterAppearance copy() {
        return new AdvancedAppearance(super.getRace(), super.isFemale(), getHairColor(), mouth, nose, eyes, hairStyle, beard);
    }

    @Override
    public void applyFacialHair(Race race) {
        if (beard != null) {
            beard.apply(this, race);
        }
    }

    @Override
    protected void specialization() {
        if (hairStyle != null) {
            hairStyle.apply(this);
        }
    }

    @Override
    public void addHairInBack() {
        if (hairStyle != null) {
            hairStyle.addHairInBack(this);
        }
    }
}
