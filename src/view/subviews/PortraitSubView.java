package view.subviews;

import model.Model;
import model.PartyAnimations;
import model.characters.GameCharacter;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
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
    private static final Point CHAR_LOCATION = new Point(36, 10);
    private final SubView previous;
    private final CharacterAppearance appearance;
    private final String portraitName;
    private final PartyAnimations partyAnimations = new PartyAnimations();

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
            hairColor = raceToUse.getRandomHairColor(gender);
        } while (hairColor == raceToUse.getColor());

        int mouthIndex;
        do {
            mouthIndex = MyRandom.randInt(CharacterCreationView.mouthSet.length);
        } while ((gender && isBeardyMouth(mouthIndex)) || // woman with beard
                !raceToUse.isRandomMouthOk(gender, mouthIndex));
        int mouth = CharacterCreationView.mouthSet[mouthIndex];

        int nose = raceToUse.getRandomNose();
        CharacterEyes eyes = race.getRandomEyes();
        HairStyle hair = race.getRandomHairStyle(gender);
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, gender,
                hairColor, mouth, nose, eyes, hair, beard);
        raceToUse.setRandomDetail(appearance);
        if (gender) { // makup
             if (MyRandom.randInt(3) == 0) {
                 appearance.setMascaraColor(MakeUpColors.randomMascaraColor(raceToUse));
             }
            if (MyRandom.randInt(3) == 0) {
                MyColors lipColor = MakeUpColors.randomLipColor(raceToUse);
                if (lipColor != raceToUse.getColor()) {
                    appearance.setLipColor(lipColor);
                }
            }
            if (MyRandom.randInt(10) == 0) {
                appearance.addFaceDetail(new RougeDetail());
                appearance.setDetailColor(MakeUpColors.randomRougeColor(raceToUse));
            }
        }
        appearance.setClass(cls);
        return appearance;
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

    public static boolean isBeardyMouth(int mouthIndex) {
        return mouthIndex == 4 || mouthIndex == 5 || mouthIndex >= 12;
    }

    public static CharacterAppearance makeChildAppearance(Race race, boolean gender) {
        ChildAppearance app = new ChildAppearance(race, gender, HairStyle.randomHairColor());
        app.setClass(Classes.None);
        return app;
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
            partyAnimations.drawBlink(model.getScreenHandler(), appearance, CHAR_LOCATION);
        } else {
            silhouetteAppearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9);
        }
        if (portraitName.length() > 14 && portraitName.contains(" ")) {
            String[] splits = portraitName.split(" ");
            String first = splits[0];
            String last = splits[1];

            if (splits.length > 2) {
                last = splits[2];
                if (first.length() <= last.length()) {
                    first += " " + splits[1];
                } else {
                    last += " " + splits[1];
                }
            }
            BorderFrame.drawCentered(model.getScreenHandler(), first, Y_OFFSET + 17, MyColors.LIGHT_GRAY, MyColors.BLACK);
            BorderFrame.drawCentered(model.getScreenHandler(), last, Y_OFFSET + 18, MyColors.LIGHT_GRAY, MyColors.BLACK);

        } else {
            BorderFrame.drawCentered(model.getScreenHandler(), portraitName, Y_OFFSET + 17, MyColors.LIGHT_GRAY, MyColors.BLACK);
        }
        partyAnimations.drawSpeakAnimations(model.getScreenHandler());
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
        if (appearance != null) {
            partyAnimations.addSpeakAnimation(pair.first, CHAR_LOCATION, line.length(), appearance, false);
        }
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
        } while (gender && isBeardyMouth(mouthIndex) || (!raceToUse.isRandomMouthOk(gender, mouthIndex)));

        int mouth = CharacterCreationView.mouthSet[mouthIndex];
        int nose = raceToUse.getRandomNose();
        CharacterEyes eyes = raceToUse.getRandomOldEyes();
        HairStyle hair = raceToUse.getRandomHairStyle(gender);
        Beard beard;
        do {
            beard = Beard.allBeards[MyRandom.randInt(Beard.allBeards.length)];
        } while (beard.isTrueBeard() != isBeardyMouth(mouthIndex));
        appearance = new AdvancedAppearance(raceToUse, gender,
                hairColor, mouth, nose, eyes, hair, beard);
        raceToUse.setRandomDetail(appearance);
        appearance.setClass(cls);
        return appearance;
    }

    public void forceVampireFeedingLook() {
        partyAnimations.setVampireFeedingLookEnabledFor(appearance, CHAR_LOCATION);
    }

    public void removeVampireFeedingLook() {
        partyAnimations.removeVampireFeedingLookFor(appearance);
    }

    public void forceEyesClosed(boolean closed) {
        partyAnimations.forceEyesClosed(appearance, closed);
    }

    public void dispose() {
        partyAnimations.unregisterAll();
    }
}
