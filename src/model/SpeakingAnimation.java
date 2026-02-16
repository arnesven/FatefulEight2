package model;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.awt.*;
import java.io.Serializable;

public class SpeakingAnimation implements Serializable {
    private final Point location;
    private final AdvancedCalloutSprite callout;
    private final MouthMovementSprite mouthAnimation;

    public SpeakingAnimation(Point location, String text,
                             CharacterAppearance app, boolean vampireTeeth) {
        this.location = location;
        callout = makeCalloutSprite(text);
        if (app.showFacialHair() && app.supportsSpeakingAnimation()) {
            MyColors skinColor = app.hasAlternateSkinColor() ? app.getAlternateSkinColor() : app.getRace().getColor();
            int lengthOfMovement = Math.max(15, text.length());
            mouthAnimation = new MouthMovementSprite(lengthOfMovement, skinColor,
                    app.getLipColor(), app.hasTuskMouth(), vampireTeeth);
        } else {
            mouthAnimation = null;
        }
    }

    protected AdvancedCalloutSprite makeCalloutSprite(String text) {
        return new AdvancedCalloutSprite(text);
    }

    public void drawYourself(ScreenHandler screenHandler) {
        if (!callout.isDone()) {
            //screenHandler.register(callout.getName(), location, callout, 2);
            callout.drawYourself(screenHandler, location);
        }
        if (mouthAnimation != null && !mouthAnimation.isDone()) {
            Point p = new Point(location.x, location.y + 5);
            screenHandler.register(mouthAnimation.getName(), p, mouthAnimation, 2);
        }
    }

    public boolean isMouthMoving() {
        return mouthAnimation != null && !mouthAnimation.isDone();
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
