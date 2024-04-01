package view.subviews;

import model.Model;
import model.SpeakingAnimation;
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
import java.awt.*;
import java.util.List;

public class PortraitSubView extends SubView {
    public static final int SILHOUETTE = 0;
    private static final CharacterAppearance silhouetteAppearance = new SilhouetteAppearance();
    public static final int PORTRAIT_FRAME_WIDTH = 16;
    public static final int PORTRAIT_FRAME_HEIGHT = 13;
    private final SubView previous;
    private final CharacterAppearance appearance;
    private final String portraitName;
    private SpeakingAnimation speakingAnimation = null;

    public PortraitSubView(SubView subView, CharacterClass cls, Race race, String portraitName) {
        this.previous = subView;
        this.portraitName = portraitName;
        appearance = makeRandomPortrait(cls, race);
    }

    public static AdvancedAppearance makeRandomPortrait(CharacterClass cls, Race race, boolean gender) {
        AdvancedAppearance appearance;
        Race raceToUse = race;
        if (race.id() == Race.ALL.id()) {
            raceToUse = Race.randomRace();
        }
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
        HairStyle hair = HairStyle.randomHairStyle(gender);
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, gender,
                hairColor, mouth, nose, eyes, hair, beard);
        setDetail(appearance);
        if (gender) { // makup
             if (MyRandom.randInt(3) == 0) {
                 appearance.setMascaraColor(CharacterCreationView.makeupColorSet[
                         MyRandom.randInt(CharacterCreationView.makeupColorSet.length)]);
             }
            if (MyRandom.randInt(3) == 0) {
                appearance.setLipColor(CharacterCreationView.makeupColorSet[
                        MyRandom.randInt(CharacterCreationView.makeupColorSet.length)]);
            }
        }
        appearance.setClass(cls);
        return appearance;
    }

    private static void setDetail(AdvancedAppearance appearance) {
        if (MyRandom.randInt(50) == 0) {
            appearance.setFaceDetail(new EyePatchDetail());
            appearance.setDetailColor(MyColors.BLACK);
        } else {
            boolean glasses = MyRandom.rollD10() == 10;
            boolean earrings = MyRandom.rollD10() == 10;
            if (glasses && earrings) {
                appearance.setFaceDetail(new GlassesAndEarringsDetail());
            } else if (glasses) {
                appearance.setFaceDetail(new GlassesDetail());
            } else if (earrings) {
                appearance.setFaceDetail(new EarringsDetail());
            }
            int detailColor = MyRandom.randInt(CharacterCreationView.detailColorSet.length);
            appearance.setDetailColor(CharacterCreationView.detailColorSet[detailColor]);
        }
    }

    public static AdvancedAppearance makeRandomPortrait(CharacterClass cls, Race race) {
        return makeRandomPortrait(cls, race, MyRandom.flipCoin());
    }

    public static AdvancedAppearance makeRandomPortrait(CharacterClass cls) {
        return makeRandomPortrait(cls, Race.randomRace(), MyRandom.flipCoin());
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
        return mouthIndex == 4 || mouthIndex == 5 || mouthIndex >= 12;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        model.getScreenHandler().clearForeground(X_OFFSET+5, X_OFFSET+17+PORTRAIT_FRAME_WIDTH,
                Y_OFFSET+5, Y_OFFSET+7+PORTRAIT_FRAME_HEIGHT);
        BorderFrame.drawFrame(model.getScreenHandler(), X_OFFSET+7, Y_OFFSET+7,
                PORTRAIT_FRAME_WIDTH, PORTRAIT_FRAME_HEIGHT, MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, true);
        if (appearance != null) {
            appearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9);
        } else {
            silhouetteAppearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9);
        }
        if (portraitName.length() > 14 && portraitName.contains(" ")) {
               String[] splits = portraitName.split(" ");
            BorderFrame.drawCentered(model.getScreenHandler(), splits[0], Y_OFFSET + 17, MyColors.LIGHT_GRAY, MyColors.BLACK);
            BorderFrame.drawCentered(model.getScreenHandler(), splits[1], Y_OFFSET + 18, MyColors.LIGHT_GRAY, MyColors.BLACK);
        } else {
            BorderFrame.drawCentered(model.getScreenHandler(), portraitName, Y_OFFSET + 17, MyColors.LIGHT_GRAY, MyColors.BLACK);
        }
        if (speakingAnimation != null) {
            speakingAnimation.drawYourself(model.getScreenHandler());
            if (speakingAnimation.isDone()) {
                speakingAnimation.unregister();
                speakingAnimation = null;
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
        state.printQuote(portraitName, pair.second);
        speakingAnimation = new SpeakingAnimation(pair.first, new Point(39, 12), line.length(), appearance);
    }

    public boolean getPortraitGender() {
        return appearance.isFemale();
    }

    public static CharacterAppearance makeOldPortrait(CharacterClass cls, Race raceToUse, boolean gender) {
        AdvancedAppearance appearance;
        MyColors hairColor;
        do {
            hairColor = MyRandom.sample(List.of(MyColors.WHITE, MyColors.GRAY, MyColors.LIGHT_GRAY));
        } while (hairColor == raceToUse.getColor());

        int mouthIndex;
        do {
            mouthIndex = MyRandom.randInt(CharacterCreationView.mouthSet.length);
        } while (gender && isBeardyMouth(mouthIndex));
        int mouth = CharacterCreationView.mouthSet[mouthIndex];
        int nose = CharacterCreationView.noseSet[MyRandom.randInt(CharacterCreationView.noseSet.length)];
        CharacterEyes[] oldEyes = new CharacterEyes[]{CharacterEyes.allEyes[3], CharacterEyes.allEyes[5], CharacterEyes.allEyes[7]};
        CharacterEyes eyes = oldEyes[MyRandom.randInt(oldEyes.length)];
        HairStyle hair = HairStyle.randomHairStyle(gender);
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, gender,
                hairColor, mouth, nose, eyes, hair, beard);
        setDetail(appearance);
        appearance.setClass(cls);
        return appearance;
    }

}
