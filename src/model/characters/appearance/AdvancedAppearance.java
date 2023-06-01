package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.*;

import java.util.ArrayList;
import java.util.List;

public class AdvancedAppearance extends CharacterAppearance {
    private final int mouth;
    private final int nose;
    private final CharacterEyes eyes;
    private final HairStyle hairStyle;
    private final Beard beard;
    private Sprite32x32 avatarNormalHair;
    private Sprite32x32 avatarBackHair;
    private Sprite avatarFacial;
    private boolean hasGlasses = false;
    private boolean hasEarrings = false;
    private MyColors detailColor = MyColors.GRAY;
    private boolean hasEyePatch = false;
    private boolean raceSpecificEars = true;
    private int[] ears;

    public AdvancedAppearance(Race race, boolean femaleGender, MyColors hairColor,
                              int mouth, int nose, CharacterEyes eyes, HairStyle hair,
                              Beard beard) {
        super(race, femaleGender, hairColor);
        this.mouth = mouth;
        this.nose = nose;
        this.eyes = eyes;
        this.hairStyle = hair;
        this.beard = beard;
        makeAvatarHairSprites(hair, race);
    }

    private void makeAvatarHairSprites(HairStyle hair, Race race) {
        List<Sprite> beardSprite = new ArrayList<>();
        if (beard != null) {
            this.avatarFacial = new Sprite32x32("avatarBeard"+beard.toString(),"hair.png",
                    beard.getAvatarSprite(),  getHairColor(), MyColors.WHITE, MyColors.GOLD);
            beardSprite.add(avatarFacial);
        } else {
            this.avatarFacial = CharacterAppearance.noHair();
        }
        this.avatarNormalHair = new Sprite32x32("avatarNormalHair"+hair.toString(), "hair.png", hairStyle.getNormalHair(),
                getHairColor(), beardSprite);
        this.avatarBackHair =  new Sprite32x32("avatarBackHair"+hair.toString(), "hair.png", hairStyle.getBackHairOnly(),
                getHairColor(), beardSprite);
        if (race.isShort()) {
            avatarNormalHair.shiftUpPx(-2);
            avatarBackHair.shiftUpPx(-2);
        }
    }

    @Override
    public void reset() {
        super.reset();
        makeAvatarHairSprites(hairStyle, getRace());
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
    public void applyFacialHair(Race race, boolean coversEars) {
        if (beard != null) {
            beard.apply(this, race);
        }
        if (hasEyePatch) {
            for (int i = 0; i < 3; ++i) {
                Sprite8x8 left = new Sprite8x8("eyepatch", "clothes.png", 0x5A + i);
                left.setColor1(detailColor);
                addSpriteOnTop(2 + i, 3, left);
            }
        }
        if (hasGlasses) {
            for (int i = 0; i < 3; ++i) {
                Sprite8x8 left = new Sprite8x8("glasses", "clothes.png", 0x3A + i);
                left.setColor1(detailColor);
                addSpriteOnTop(2 + i, 3, left);
            }
        }
        if (hasEarrings && !coversEars) {
            Sprite8x8 left = new Sprite8x8("earringleft", "clothes.png", 0x4A);
            left.setColor1(detailColor);
            addSpriteOnTop(1, 3, left);
            Sprite8x8 right = new Sprite8x8("earringright", "clothes.png", 0x4B);
            right.setColor1(detailColor);
            addSpriteOnTop(5, 3, right);
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

    @Override
    public Sprite getNormalHair() {
        return avatarNormalHair;
    }

    @Override
    public Sprite getBackHairOnly() {
        return avatarBackHair;
    }

    @Override
    public Sprite getFacialOnly() {
        return avatarFacial;
    }

    public void setHasGlasses(boolean b) {
        hasGlasses = b;
    }

    public void setHasEarrings(boolean b) {
        hasEarrings = b;
    }

    public void setHasEyePatch(boolean b) {
        hasEyePatch = b;
    }

    @Override
    protected PortraitSprite getOuterFrameSprite(int i) {
        if (hairStyle.getOuterFrame() != null) {
            return new HairSpriteWithFrame(hairStyle.getOuterFrame()[i-1], getHairColor());
        }
        return super.getOuterFrameSprite(i);
    }

    public void setDetailColor(MyColors color) {
        detailColor = color;
    }

    @Override
    protected boolean classSpecificEars() {
        return raceSpecificEars;
    }

    public void setRaceSpecificEars(boolean raceSpecificEars) {
        this.raceSpecificEars = raceSpecificEars;
    }
    
    public void setEars(Ears ears) {
        ears.setYourself(this);
    }

    public void setEars(int[] ears) {
        this.ears = ears;
    }

    @Override
    protected PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(ears[0], hairColor);
    }

    @Override
    protected PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(ears[1], hairColor);
    }
}
