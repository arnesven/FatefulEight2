package model.characters.appearance;

import model.characters.GameCharacter;
import view.MyColors;
import view.ScreenHandler;

public class InvisibleAppearance extends AdvancedAppearance {
    private final CharacterAppearance inner;

    public InvisibleAppearance(GameCharacter basedOn) {
        super(basedOn.getRace(),
                basedOn.getGender(), MyColors.TRANSPARENT,
                -0x50 + 0x0F, -0x60 + 0x0F,
                new CharacterEyes(-0x20 + 0x0F, "", 0, 0),
                HairStyle.allHairStyles[0], new NoBeard());
        setAlternateSkinColor(MyColors.TRANSPARENT);
        inner = basedOn.getAppearance();
    }

    public CharacterAppearance getOriginalAppearance() {
        return inner;
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

    @Override
    public boolean hairOnTop() {
        return false;
    }

    @Override
    public CharacterAppearance copy() {
        return null;
    }

    @Override
    public boolean supportsSpeakingAnimation() {
        return false;
    }

    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) {}

    @Override
    protected void drawBigEyes(ScreenHandler screenHandler, int x, int y, boolean left, boolean right) {}

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y, boolean leftEye, boolean rightEye) {}

    @Override
    public void drawFacialExpression(ScreenHandler screenHandler, int x, int y, FacialExpression emphasis, boolean drawDefaultMouth, boolean isVampire) {}
}
