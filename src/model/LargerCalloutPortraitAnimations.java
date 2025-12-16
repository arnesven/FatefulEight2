package model;

import model.characters.appearance.CharacterAppearance;

import java.awt.*;

public class LargerCalloutPortraitAnimations extends PortraitAnimations {

    @Override
    protected SpeakingAnimation makeSpeakingAnimation(Point p, String text, CharacterAppearance appearance, boolean vampireTeeth) {
        return new LargerCalloutSpeakingAnimation(p, text, appearance, vampireTeeth);
    }
}
