package view.subviews;

import model.Model;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.races.AllRaces;
import model.races.Race;
import model.states.GameState;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.party.CharacterCreationView;
import view.sprites.CalloutSprite;

import java.awt.*;

public class PortraitSubView extends SubView {
    private final SubView previous;
    private final AdvancedAppearance appearance;
    private final String portraitName;
    private CalloutSprite callout = null;

    public PortraitSubView(SubView subView, CharacterClass cls, Race race, String portraitName) {
        this.previous = subView;
        this.portraitName = portraitName;
        Race raceToUse = race;
        if (race.id() == Race.ALL.id()) {
            raceToUse = Race.allRaces[MyRandom.randInt(Race.allRaces.length)];
        }
        MyColors hairColor = HairStyle.allHairColors[MyRandom.randInt(HairStyle.allHairColors.length)];
        int mouthIndex = MyRandom.randInt(CharacterCreationView.mouthSet.length);
        int mouth = CharacterCreationView.mouthSet[mouthIndex];
        int nose = CharacterCreationView.noseSet[MyRandom.randInt(CharacterCreationView.noseSet.length)];
        CharacterEyes eyes = CharacterEyes.allEyes[MyRandom.randInt(CharacterEyes.allEyes.length)];
        HairStyle hair = HairStyle.allHairStyles[MyRandom.randInt(HairStyle.allHairColors.length)];
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, MyRandom.randInt(2)==0,
                hairColor, mouth, nose, eyes, hair, beard);
        appearance.setClass(cls);
    }

    private boolean isBeardyMouth(int mouthIndex) {
        return mouthIndex == 4 || mouthIndex == 5;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        BorderFrame.drawFrame(model.getScreenHandler(), X_OFFSET+7, Y_OFFSET+7,
                16, 12, MyColors.BLACK, MyColors.WHITE, MyColors.BLACK, true);
        appearance.drawYourself(model.getScreenHandler(), X_OFFSET+12, Y_OFFSET+9);
        BorderFrame.drawCentered(model.getScreenHandler(), portraitName, Y_OFFSET+17, MyColors.WHITE, MyColors.BLACK);
        if (callout != null) {
            model.getScreenHandler().register(callout.getName()+"portrait", new Point(40, 12), callout);
            if (callout.isDone()) {
                callout = null;
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    public SubView getPreviousSubView() {
        return previous;
    }

    public void portraitSay(Model model, GameState state, String line) {
        model.getLog().waitForAnimationToFinish();
        state.println(portraitName + ": \"" + line + "\"");
        int num = CalloutSprite.getSpriteNumForText(line);
        callout = new CalloutSprite(num);
    }
}
