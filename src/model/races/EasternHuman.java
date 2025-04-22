package model.races;

import model.characters.appearance.CharacterEyes;
import model.classes.Skill;
import util.MyRandom;
import view.MyColors;

import java.util.List;

public class EasternHuman extends HumanRace {

    public static final List<CharacterEyes> EYES = List.of(
            new CharacterEyes(0x1AF, 0x1BF, "", 6),
            new CharacterEyes(0x1CF, 0x1DF, "", 6),
            new CharacterEyes(0x1EF, 0x1FF, "", 6),
            new CharacterEyes(0x19D, 0x19E, "", 6)
    );
    private static final List<MyColors> HAIR_COLORS = List.of(MyColors.DARK_GRAY,
            MyColors.DARK_BROWN, MyColors.GRAY, MyColors.BROWN, MyColors.DARK_RED, MyColors.TAN);

    private static final List<Integer> NOSES = List.of(0x2, 0x9, 0xA, 0xB, 0xE, 0x18, 0x4A, 0x4B);

    public EasternHuman() {
        super("Human", MyColors.BEIGE,
                new Skill[]{Skill.Logic, Skill.SeekInfo, Skill.UnarmedCombat},
                "Eastern humans are found in the far east of the world. " +
                        "Their attributes are notably average in many regards. Eastern humans take up " +
                        "all kinds of professions but it is not " +
                        "uncommon for them to be Warriors, Scholars, Poets, and Artisans.");
    }

    @Override
    public String getQualifiedName() {
        return getName() + " (East)";
    }

    @Override
    public MyColors getRandomHairColor(boolean gender) {
        return MyRandom.sample(HAIR_COLORS);
    }

    @Override
    public CharacterEyes getRandomEyes() {
        return MyRandom.sample(EYES);
    }

    @Override
    public CharacterEyes getRandomOldEyes() {
        return EYES.get(MyRandom.randInt(2) + 1);
    }

    @Override
    public int getRandomNose() {
        return MyRandom.sample(NOSES);
    }

    @Override
    public MyColors getMouthDefaultColor() {
        return MyColors.DARK_BROWN;
    }
}
