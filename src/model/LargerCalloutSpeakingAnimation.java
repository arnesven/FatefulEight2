package model;

import model.characters.appearance.CharacterAppearance;
import view.sprites.AdvancedCalloutSprite;

import java.awt.*;

public class LargerCalloutSpeakingAnimation extends SpeakingAnimation {
    private static final int MAX_WIDTH = 32;
    private static final int MAX_ROWS = 9;

    public LargerCalloutSpeakingAnimation(Point p, String text, CharacterAppearance appearance, boolean vampireTeeth) {
        super(p, text, appearance, vampireTeeth);
    }

    @Override
    protected AdvancedCalloutSprite makeCalloutSprite(String text) {
        int maxWidth = MAX_WIDTH;
        if (text.length() < 80) {
            maxWidth = 20;
        } else if (text.length() < 110) {
            maxWidth = 25;
        } else if (text.length() < 150) {
            maxWidth = 30;
        }
        return new AdvancedCalloutSprite(text, maxWidth, MAX_ROWS, false);
    }
}
