package model;

import model.characters.appearance.CharacterAppearance;
import view.sprites.AdvancedCalloutSprite;

import java.awt.*;

public class SmallCalloutPortraitAnimations extends PortraitAnimations {

    @Override
    protected SpeakingAnimation makeSpeakingAnimation(Point p, String text, CharacterAppearance appearance, boolean vampireTeeth) {
        return new SmallCalloutSpeakingAnimation(p, text, appearance, vampireTeeth);
    }

    private class SmallCalloutSpeakingAnimation extends SpeakingAnimation {
        public SmallCalloutSpeakingAnimation(Point p, String text, CharacterAppearance appearance, boolean vampireTeeth) {
            super(p, text, appearance, vampireTeeth);
        }

        @Override
        protected AdvancedCalloutSprite makeCalloutSprite(String text) {
            return new AdvancedCalloutSprite(text, 20, 5, false);
        }
    }
}
