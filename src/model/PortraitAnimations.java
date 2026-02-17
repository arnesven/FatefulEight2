package model;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.FacialExpression;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.sprites.DieRollAnimation;
import view.ScreenHandler;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class PortraitAnimations implements Serializable {
    private static final int BLINK_RATE = 350;
    private static final int CHANGE_LOOK_LONG_DIRECTION = 800;
    private static final int CHANGE_LOOK_SHORT_DURATION = 400;
    private static final int SLOT_ANIMATION_DELAY = 4;
    private final Map<CharacterAppearance, SpeakingAnimation> speakingAnimations = new HashMap<>();
    private final Map<CharacterAppearance, Integer> blinking = new HashMap<>();
    private final Map<CharacterAppearance, Boolean> lookers = new HashMap<>();
    private final Map<CharacterAppearance, MyPair<FacialExpression, Integer>> facialExpressions = new HashMap<>();
    private final Map<Point, DieRollAnimation> dieRollAnimations = new HashMap<>();
    private final Map<CharacterAppearance, VampireFeedingLook> feedingLooks = new HashMap<>();
    private Map<Integer, Integer> slotAnimations = new HashMap<>();

    public void drawBlink(ScreenHandler screenHandler, CharacterAppearance app, Point p, boolean isVampire) {
        if (!app.showFacialHair()) {
            return;
        }
        if (!handleFacialExpression(screenHandler, app, p, isVampire)) {
            handleLook(screenHandler, app, p);
            handleBlink(screenHandler, app, p);
            handleMisc(screenHandler, app);
        }
    }

    private void handleLook(ScreenHandler screenHandler, CharacterAppearance app, Point p) {
        if (lookers.containsKey(app)) {
            if (MyRandom.randInt(CHANGE_LOOK_SHORT_DURATION) == 0) {
                lookers.remove(app);
            }
        } else {
            if (!speakingAnimations.containsKey(app) && MyRandom.randInt(CHANGE_LOOK_LONG_DIRECTION) == 0) {
                lookers.put(app, MyRandom.flipCoin());
            }
        }

        if (lookers.containsKey(app)) {
            app.drawDrawLook(screenHandler, lookers.get(app), p.x+3, p.y+6, true, true);
        }
    }

    private boolean handleFacialExpression(ScreenHandler screenHandler, CharacterAppearance app, Point p, boolean isVampire) {
        if (facialExpressions.containsKey(app)) {
            app.drawFacialExpression(screenHandler, p.x+3, p.y+6, facialExpressions.get(app).first,
                    !isMovingMouth(app), isVampire);
            return true;
        }
        return false;
    }

    private boolean isMovingMouth(CharacterAppearance app) {
        return !speakingAnimations.containsKey(app) || speakingAnimations.get(app).isMouthMoving();
    }

    private void handleBlink(ScreenHandler screenHandler, CharacterAppearance app, Point p) {
        if (MyRandom.randInt(BLINK_RATE) == 0 || blinking.containsKey(app)) {
            app.drawBlink(screenHandler, p.x+3, p.y+6);
            if (blinking.containsKey(app)) {
                int step = blinking.get(app);
                step--;
                if (step == 0) {
                    blinking.remove(app);
                } else {
                    blinking.put(app, step);
                }
            } else {
                blinking.put(app, 10);
            }
        }
    }

    public synchronized void drawSpeakAnimations(ScreenHandler screenHandler) {
        for (CharacterAppearance app : new ArrayList<>(speakingAnimations.keySet())) {
            SpeakingAnimation speakAni = speakingAnimations.get(app);
            if (speakAni != null) {
                speakAni.drawYourself(screenHandler);
                if (speakAni.isDone()) {
                    speakAni.unregister();
                    speakingAnimations.remove(app);
                    removeFacialExpressionWithCalloutLifetime(app);
                }
            }
        }
    }

    private void removeFacialExpressionWithCalloutLifetime(CharacterAppearance app) {
        if (facialExpressions.containsKey(app)) {
            if (facialExpressions.get(app).second == FacialExpression.END_OF_CALLOUT) {
                facialExpressions.remove(app);
            }
        }
    }

    public synchronized void addSpeakAnimation(Point pOrig, String text,
                                               CharacterAppearance appearance,
                                               boolean vampireTeeth) {
        lookers.remove(appearance);
        Point p = new Point(pOrig.x, pOrig.y);
        p.x += 3;
        p.y += 2;
        if (speakingAnimations.containsKey(appearance)) {
            speakingAnimations.get(appearance).unregister();
            speakingAnimations.remove(appearance);
            removeFacialExpressionWithCalloutLifetime(appearance);
        }
        speakingAnimations.put(appearance, makeSpeakingAnimation(p, text, appearance, vampireTeeth));
    }

    protected SpeakingAnimation makeSpeakingAnimation(Point p, String text, CharacterAppearance appearance, boolean vampireTeeth) {
        return new SpeakingAnimation(p, text, appearance, vampireTeeth);
    }

    public DieRollAnimation addDieRollAnimation(Point location, int unmodifiedRoll) {
        if (dieRollAnimations.containsKey(location)) {
            dieRollAnimations.get(location).unregisterYourself();
            dieRollAnimations.remove(location);
        }
        DieRollAnimation animation = new DieRollAnimation(unmodifiedRoll);
        dieRollAnimations.put(location, animation);
        return animation;
    }

    public void drawDieRollAnimations(ScreenHandler screenHandler) {
        for (Point p : new ArrayList<>(dieRollAnimations.keySet())) {
            DieRollAnimation spr = dieRollAnimations.get(p);
            screenHandler.register(spr.getName(), p, spr, 3, spr.getXShift(), spr.getYShift());
            if (spr.isDone()) {
                spr.unregisterYourself();
                dieRollAnimations.remove(p);
            } else if (!spr.blocksGame()) {
                spr.startTwinkle();
            }
        }
    }

    public synchronized void clearAnimations() {
        dieRollAnimations.clear();
        speakingAnimations.clear();
        blinking.clear();
        lookers.clear();
    }

    public synchronized void clearAnimationsFor(GameCharacter gc) {
        speakingAnimations.remove(gc.getAppearance());
        blinking.remove(gc.getAppearance());
        lookers.remove(gc.getAppearance());
    }

    public void forceEyesClosed(CharacterAppearance app, boolean closed) {
        if (closed) {
            blinking.put(app, Integer.MAX_VALUE);
        } else {
            blinking.remove(app);
        }
    }

    public void setFacialExpression(CharacterAppearance app, FacialExpression emph, int expressionLifetime) {
        if (emph != FacialExpression.none) {
            blinking.remove(app);
            lookers.remove(app);
            facialExpressions.put(app, new MyPair<>(emph, expressionLifetime));
        }
        if (expressionLifetime == FacialExpression.PERMANENT && emph == FacialExpression.none) {
            facialExpressions.remove(app);
        }
    }

    public void forceEyesClosed(GameCharacter victim, boolean closed) {
        facialExpressions.remove(victim.getAppearance());
        forceEyesClosed(victim.getAppearance(), closed);
    }

    private void handleMisc(ScreenHandler screenHandler, CharacterAppearance appearance) {
        if (feedingLooks.containsKey(appearance)) {
            feedingLooks.get(appearance).drawYourself(screenHandler);
        }
    }

    public void setVampireFeedingLookEnabledFor(CharacterAppearance app, Point point) {
        facialExpressions.remove(app);
        Point p = new Point(point);
        p.x += 3;
        p.y += 2;
        this.feedingLooks.put(app, new VampireFeedingLook(app, p));
    }

    public void removeVampireFeedingLookFor(CharacterAppearance app) {
        this.feedingLooks.remove(app);
    }

    public void unregisterAll() {
        for (SpeakingAnimation a : speakingAnimations.values()) {
            a.unregister();
        }
    }

    public void addSlotAnimation(int i) {
        this.slotAnimations.put(i, SLOT_ANIMATION_DELAY * 8);
    }

    public void drawSlot(ScreenHandler screenHandler, int count, Point wordLocation, boolean isVacant) {
        String vacantString = "*VACANT*";
        String lockedString = "*LOCKED*";
        BorderFrame.drawString(screenHandler, lockedString, wordLocation.x, wordLocation.y, MyColors.DARK_RED, MyColors.BLACK);

        if (slotAnimations.containsKey(count)) {
            int index = vacantString.length() - (slotAnimations.get(count)-1) / SLOT_ANIMATION_DELAY - 1;

            BorderFrame.drawString(screenHandler, vacantString.substring(0, index), wordLocation.x, wordLocation.y, MyColors.GRAY, MyColors.BLACK);
            BorderFrame.drawString(screenHandler, (char)0xFF + "",
                    wordLocation.x + index, wordLocation.y, MyColors.WHITE, MyColors.BLACK);

            int newStep = slotAnimations.get(count) - 1;
            if (newStep <= 0) {
                slotAnimations.remove(count);
            } else {
                slotAnimations.put(count, newStep);
            }
        } else if (isVacant) {
            BorderFrame.drawString(screenHandler, vacantString, wordLocation.x, wordLocation.y, MyColors.GRAY, MyColors.BLACK);
        }
    }

    public void clearFacialExpressions() {
        facialExpressions.clear();
    }
}
