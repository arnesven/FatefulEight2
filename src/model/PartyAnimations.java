package model;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import util.MyRandom;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.DieRollAnimation;
import view.ScreenHandler;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class PartyAnimations implements Serializable {
    private static final int BLINK_RATE = 350;
    private static final int CHANGE_LOOK_LONG_DIRECTION = 800;
    private static final int CHANGE_LOOK_SHORT_DURATION = 400;
    private final Map<CharacterAppearance, SpeakingAnimation> speakingAnimations = new HashMap<>();
    private final Map<CharacterAppearance, Integer> blinking = new HashMap<>();
    private final Map<CharacterAppearance, Boolean> lookers = new HashMap<>();
    private final Map<Point, DieRollAnimation> dieRollAnimations = new HashMap<>();
    private final Map<CharacterAppearance, VampireFeedingLook> feedingLooks = new HashMap<>();

    public void drawBlink(ScreenHandler screenHandler, CharacterAppearance app, Point p) {
        if (!app.showFacialHair()) {
            return;
        }
        handleLook(screenHandler, app, p);
        handleBlink(screenHandler, app, p);
        handleMisc(screenHandler, app);
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
            app.drawDrawLook(screenHandler, lookers.get(app), p.x+3, p.y+6);
        }
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
            if (speakAni == null) {
                continue;
            }
            speakAni.drawYourself(screenHandler);
            if (speakAni.isDone()) {
                speakAni.unregister();
                speakingAnimations.remove(app);
            }
        }
    }

    public synchronized void addSpeakAnimation(int calloutNum, Point pOrig, String text,
                                               CharacterAppearance appearance,
                                  boolean vampireTeeth) {
        lookers.remove(appearance);
        Point p = new Point(pOrig.x, pOrig.y);
        p.x += 3;
        p.y += 2;
        if (speakingAnimations.containsKey(appearance)) {
            speakingAnimations.get(appearance).unregister();
            speakingAnimations.remove(appearance);
        }
        speakingAnimations.put(appearance, new SpeakingAnimation(calloutNum, p, text, appearance, vampireTeeth));
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

    public void forceEyesClosed(GameCharacter victim, boolean closed) {
        forceEyesClosed(victim.getAppearance(), closed);
    }

    private void handleMisc(ScreenHandler screenHandler, CharacterAppearance appearance) {
        if (feedingLooks.containsKey(appearance)) {
            feedingLooks.get(appearance).drawYourself(screenHandler);
        }
    }

    public void setVampireFeedingLookEnabledFor(CharacterAppearance app, Point point) {
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
}
