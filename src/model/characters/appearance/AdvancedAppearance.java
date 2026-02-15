package model.characters.appearance;

import model.races.Race;
import util.MyLists;
import util.MyPair;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdvancedAppearance extends CharacterAppearance {
    private int mouth;
    private int nose;
    private CharacterEyes eyes;
    private HairStyle hairStyle;
    private Beard beard;
    private Sprite32x32 avatarNormalHair;
    private Sprite32x32 avatarBackHair;
    private Sprite32x32 avatarFullBackHair;
    private Sprite32x32 avatarHalfBackHair;
    private Sprite avatarFacial;
    private List<FaceDetail> faceDetails = new ArrayList<>();
    private boolean raceSpecificEars = true;
    private int[] ears;
    private MyColors eyeballColor = MyColors.WHITE;

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
        setFaceOverlaySprites();
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
        this.avatarHalfBackHair = new Sprite32x32("avatarHalfBackHair"+hair.toString(), "hair.png", hairStyle.getHalfBackHair(),
                getHairColor(), MyColors.BEIGE, MyColors.BROWN);
        this.avatarFullBackHair = new Sprite32x32("avatarFullBackHair"+hair.toString(), "hair.png", hairStyle.getFullBackHair(),
                getHairColor(), MyColors.BEIGE, MyColors.BROWN);
        if (race.isShort()) {
            avatarNormalHair.shiftUpPx(-2);
            avatarBackHair.shiftUpPx(-2);
            avatarFullBackHair.shiftUpPx(-2);
            avatarHalfBackHair.shiftUpPx(-2);
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
        return beard != null && beard.isTrueBeard();
    }

    @Override
    public CharacterAppearance copy() {
        AdvancedAppearance app = new AdvancedAppearance(super.getRace(), super.isFemale(), getHairColor(), mouth, nose, eyes, hairStyle, beard);
        for (FaceDetail faceDetail : faceDetails) {
            app.addFaceDetail(faceDetail);
        }
        app.setLipColor(getLipColor());
        app.setMascaraColor(getMascaraColor());
        return app;
    }

    @Override
    public void applyFacialHair(Race race, boolean coversEars) {
        if (beard != null) {
            beard.apply(this, race);
        }
    }

    @Override
    public void applyDetail(Race race, boolean coversEars) {
        for (FaceDetail faceDetail : faceDetails) {
            faceDetail.applyYourself(this, race, coversEars);
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
    public Sprite getFullBackHair() {
        return avatarFullBackHair;
    }

    @Override
    public Sprite getHalfBackHair() {
        return avatarHalfBackHair;
    }

    @Override
    public Sprite getFacialOnly() {
        return avatarFacial;
    }

    @Override
    protected int getLookIndex() {
        if (eyes != null) {
            return eyes.getLookIndex();
        }
        return 0;
    }

    @Override
    protected int getSurprisedIndex() {
        if (eyes != null) {
            return eyes.getSurprisedIndex();
        }
        return 0;
    }

    @Override
    protected PortraitSprite getOuterFrameSprite(int i) {
        if (hairStyle.getOuterFrame() != null) {
            return new HairSpriteWithFrame(hairStyle.getOuterFrame()[i-1], getHairColor());
        }
        return super.getOuterFrameSprite(i);
    }

    public void addFaceDetail(FaceDetail detail) {
        this.faceDetails.add(detail);
        setFaceOverlaySprites();
    }

    public void setDetailColor(MyColors color) {
        this.faceDetails.getLast().setColor(color);
        setFaceOverlaySprites();
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

    public void setHairStyle(HairStyle newHairStyle) {
        this.hairStyle = newHairStyle;
    }

    public HairStyle getHairStyle() {
        return hairStyle;
    }

    public void setBeard(Beard beard) {
        this.beard = beard;
    }

    public void drawBlink(ScreenHandler screenHandler, int x, int y) {
        if (MyLists.any(faceDetails, fd -> fd instanceof EyePatchDetail)) {
            screenHandler.register("blinkright", new Point(x+1, y), getBlinkRight());
        } else {
            super.drawBlink(screenHandler, x, y);
        }
    }

    protected MyPair<Sprite8x8, Sprite8x8> makeBlinkSprites(MyColors mascaraColor) {
        if (faceDetails == null) {
            return super.makeBlinkSprites(mascaraColor);
        }
        FaceDetail detail = MyLists.find(faceDetails, fd -> fd instanceof GlassesDetail);
        if (detail != null) {
            return new MyPair<>(new Sprite8x8("blinkleftwglasses", "mouth.png", 0x33,
                    MyColors.BLACK, mascaraColor, detail.color, MyColors.BEIGE),
                    new Sprite8x8("blinkrightwclasses", "mouth.png", 0x34,
                            MyColors.BLACK, mascaraColor, detail.color, MyColors.BEIGE));
        }
        return super.makeBlinkSprites(mascaraColor);
    }

    @Override
    protected MyPair<Sprite8x8, Sprite8x8> makeBigEyesSprites(MyColors mascaraColor) {
        if (faceDetails == null) {
            return super.makeBigEyesSprites(mascaraColor);
        }
        FaceDetail detail = MyLists.find(faceDetails, fd -> fd instanceof GlassesDetail);
        if (detail != null) {
            return new MyPair<>(new Sprite8x8("suprisedleft", "mouth.png", 0x60 + 2 * getSurprisedIndex(),
                    MyColors.BLACK, getEyeballColor(), detail.getColor(), mascaraColor),
                    new Sprite8x8("surprisedright", "mouth.png", 0x61 + 2 * getSurprisedIndex(),
                            MyColors.BLACK, getEyeballColor(), detail.getColor(), mascaraColor));
        }
        return super.makeBigEyesSprites(mascaraColor);
    }

    @Override
    protected boolean hasThinEyebrows() {
        if (hairStyle == null) {
            return false;
        }
        return hairStyle.hasThinEyebrows();
    }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) {
        if (!(MyLists.any(faceDetails, detail -> detail instanceof EyePatchDetail))) {
            super.drawDrawLook(screenHandler, left, x, y);
        }
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }

    public void setEyes(CharacterEyes eyes) {
        this.eyes = eyes;
    }

    public void setNose(int nose) { this.nose = nose; }

    @Override
    protected MyColors getEyeballColor() {
        return eyeballColor;
    }

    public void setEyeballColor(MyColors color) {
        this.eyeballColor = color;
    }

    public Beard getBeard() {
        return beard;
    }
}
