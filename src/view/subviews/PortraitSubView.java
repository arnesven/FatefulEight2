package view.subviews;

import model.Model;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.races.AllRaces;
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
    private static final Sprite SILHOUETTE_SPRITE = new SilhouetteSprite();
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
        boolean gender = MyRandom.randInt(2)==0;
        MyColors hairColor = HairStyle.allHairColors[MyRandom.randInt(HairStyle.allHairColors.length)];
        int mouthIndex;
        do {
            mouthIndex = MyRandom.randInt(CharacterCreationView.mouthSet.length);
        } while (gender && isBeardyMouth(mouthIndex));
        int mouth = CharacterCreationView.mouthSet[mouthIndex];
        int nose = CharacterCreationView.noseSet[MyRandom.randInt(CharacterCreationView.noseSet.length)];
        CharacterEyes eyes = CharacterEyes.allEyes[MyRandom.randInt(CharacterEyes.allEyes.length)];
        HairStyle hair = HairStyle.allHairStyles[MyRandom.randInt(HairStyle.allHairColors.length)];
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, gender,
                hairColor, mouth, nose, eyes, hair, beard);
        appearance.setClass(cls);
    }

    public PortraitSubView(SubView subView, int silhouette, String portraitName) {
        this.previous = subView;
        appearance = null;
        this.portraitName = portraitName;
    }

    private boolean isBeardyMouth(int mouthIndex) {
        return mouthIndex == 4 || mouthIndex == 5;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        BorderFrame.drawFrame(model.getScreenHandler(), X_OFFSET+7, Y_OFFSET+7,
                16, 12, MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, true);
        if (appearance != null) {
            appearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9);
        } else {
            model.getScreenHandler().clearSpace(X_OFFSET + 12, X_OFFSET + 19, Y_OFFSET + 9, Y_OFFSET + 16);
            model.getScreenHandler().put(X_OFFSET + 12, Y_OFFSET + 9, SILHOUETTE_SPRITE);
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

    private static class SilhouetteSprite extends Sprite {
        public SilhouetteSprite() {
            super("silhouette", "silhouette.png", 0, 0, 56, 56);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.LIGHT_GRAY);
            setColor3(MyColors.GRAY);
        }
    }
}
