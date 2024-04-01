package model;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import util.MyRandom;
import view.ScreenHandler;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyAnimations implements Serializable {
    private static final int BLINK_RATE = 350;
    private final List<SpeakingAnimation> speakingAnimations = new ArrayList<>();
    private Map<CharacterAppearance, Integer> blinking = new HashMap<>();

    public void drawBlink(ScreenHandler screenHandler, CharacterAppearance app, Point p) {
        if (!app.showFacialHair()) {
            return;
        }
        if (MyRandom.randInt(BLINK_RATE) == 0 || blinking.containsKey(app)) {
            screenHandler.register("blinkleft", new Point(p.x+2, p.y+6), app.getBlinkLeft());
            screenHandler.register("blinkright", new Point(p.x+4, p.y+6), app.getBlinkRight());
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

    public void drawSpeakAnimations(ScreenHandler screenHandler) {
        for (SpeakingAnimation speakAni : new ArrayList<>(speakingAnimations)) {
            speakAni.drawYourself(screenHandler);
            if (speakAni.isDone()) {
                speakAni.unregister();
                speakingAnimations.remove(speakAni);
            }
        }
    }

    public void addSpeakAnimation(int calloutNum, Point pOrig, int length, CharacterAppearance appearance) {
        Point p = new Point(pOrig.x, pOrig.y);
        p.x += 3;
        p.y += 2;
        speakingAnimations.removeIf((SpeakingAnimation sp) -> sp.isInLocation(p));
        speakingAnimations.add(new SpeakingAnimation(calloutNum, p, length, appearance));
    }
}
