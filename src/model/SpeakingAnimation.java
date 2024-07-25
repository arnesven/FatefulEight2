package model;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.AnimationManager;
import view.sprites.CalloutSprite;
import view.sprites.MouthMovementSprite;
import view.sprites.TimedAnimationSprite;

import java.awt.*;
import java.io.Serializable;

public class SpeakingAnimation implements Serializable {
    private final Point location;
    private final TimedAnimationSprite callout;
    private final MouthMovementSprite mouthAnimation;

    public SpeakingAnimation(int calloutNum, Point location, int textLength,
                             CharacterAppearance app, boolean vampireTeeth) {
        this.location = location;
        callout = new CalloutSprite(calloutNum, textLength);
        if (app.showFacialHair() && app.supportsSpeakingAnimation()) {
            MyColors skinColor = app.hasAlternateSkinColor() ? app.getAlternateSkinColor() : app.getRace().getColor();
            mouthAnimation = new MouthMovementSprite(textLength, skinColor,
                    app.getLipColor(), app.hasTuskMouth(), vampireTeeth);
        } else {
            mouthAnimation = null;
        }
    }

    public void drawYourself(ScreenHandler screenHandler) {
        if (!callout.isDone()) {
            screenHandler.register(callout.getName(), location, callout, 2);
        }
        if (mouthAnimation != null && !mouthAnimation.isDone()) {
            Point p = new Point(location.x, location.y + 5);
            screenHandler.register(mouthAnimation.getName(), p, mouthAnimation, 2);
        }
    }

    public boolean isDone() {
        return callout.isDone() && (mouthAnimation == null || mouthAnimation.isDone());
    }

    public void unregister() {
        AnimationManager.unregister(callout);
        if (mouthAnimation != null) {
            AnimationManager.unregister(mouthAnimation);
        }
    }

    public boolean isInLocation(Point p) {
        return location.x == p.x && location.y == p.y;
    }
}
