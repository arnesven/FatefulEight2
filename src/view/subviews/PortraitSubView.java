package view.subviews;

import model.Model;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.races.Race;
import model.states.GameState;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.party.CharacterCreationView;
import view.sprites.CalloutSprite;
import view.sprites.Sprite;

import java.awt.*;

public class PortraitSubView extends SubView {
    public static final int SILHOUETTE = 0;
    private static final CharacterAppearance silhouetteAppearance = new SilhouetteAppearance();
    private final SubView previous;
    private final CharacterAppearance appearance;
    private final String portraitName;
    private CalloutSprite callout = null;

    public PortraitSubView(SubView subView, CharacterClass cls, Race race, String portraitName) {
        this.previous = subView;
        this.portraitName = portraitName;
        appearance = makeRandomPortrait(cls, race);
    }

    public static AdvancedAppearance makeRandomPortrait(CharacterClass cls, Race race) {
        AdvancedAppearance appearance;
        Race raceToUse = race;
        if (race.id() == Race.ALL.id()) {
            raceToUse = Race.allRaces[MyRandom.randInt(Race.allRaces.length)];
        }
        boolean gender = MyRandom.randInt(2)==0;
        MyColors hairColor;
        do {
            hairColor = HairStyle.randomHairColor();
        } while (hairColor == raceToUse.getColor());

        int mouthIndex;
        do {
            mouthIndex = MyRandom.randInt(CharacterCreationView.mouthSet.length);
        } while (gender && isBeardyMouth(mouthIndex));
        int mouth = CharacterCreationView.mouthSet[mouthIndex];
        int nose = CharacterCreationView.noseSet[MyRandom.randInt(CharacterCreationView.noseSet.length)];
        CharacterEyes eyes = CharacterEyes.allEyes[MyRandom.randInt(CharacterEyes.allEyes.length)];
        HairStyle hair = HairStyle.randomHairStyle();
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, gender,
                hairColor, mouth, nose, eyes, hair, beard);
        appearance.setHasGlasses(MyRandom.rollD10() == 10);
        appearance.setHasEarrings(MyRandom.rollD10() == 10);
        boolean patch = MyRandom.randInt(50) == 0;
        appearance.setHasEyePatch(patch);
        int detailColor = MyRandom.randInt(CharacterCreationView.detailColorSet.length);
        if (!patch) {
            appearance.setDetailColor(CharacterCreationView.detailColorSet[detailColor]);
        } else {
            appearance.setDetailColor(MyColors.BLACK);
        }
        appearance.setClass(cls);
        return appearance;
    }

    public PortraitSubView(SubView subView, int silhouette, String portraitName) {
        this.previous = subView;
        appearance = null;
        this.portraitName = portraitName;
    }

    public PortraitSubView(SubView subView, CharacterAppearance app, String portraitName) {
        this.previous = subView;
        appearance = app;
        this.portraitName = portraitName;
    }

    private static boolean isBeardyMouth(int mouthIndex) {
        return mouthIndex == 4 || mouthIndex == 5 || mouthIndex == 12 || mouthIndex == 13 || mouthIndex == 14;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        BorderFrame.drawFrame(model.getScreenHandler(), X_OFFSET+7, Y_OFFSET+7,
                16, 12, MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, true);
        if (appearance != null) {
            appearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9);
        } else {
            silhouetteAppearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9);
        }
        BorderFrame.drawCentered(model.getScreenHandler(), portraitName, Y_OFFSET+17, MyColors.LIGHT_GRAY, MyColors.BLACK);
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
        MyPair<Integer, String> pair = CalloutSprite.getSpriteNumForText(line);
        state.println(portraitName + ": \"" + pair.second + "\"");
        callout = new CalloutSprite(pair.first);
    }

    public boolean getPortraitGender() {
        return appearance.isFemale();
    }

}
