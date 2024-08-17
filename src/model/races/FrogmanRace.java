package model.races;

import model.classes.Skill;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.PortraitSprite;

import java.util.List;

public class FrogmanRace extends Race {
    private static final List<String> MUMBO_JUMBO = List.of(
            "shlrrp", "chiirp", "chi", "chi", "shsh", "llrrrp", "chi", "chi", "chirp", "shllrrip",
            "chirri", "shhhl", "urrr", "shlupp", "shlupp", "glrlrluuuhu",
            "gluhurrip", "plrrpi", "shhshh", "lurrilurp", "chi", "chi", "glurg",
            "shlurri", "shlurriiii", "glurri", "chirp", "shlurr", "pir", "urrrp",
            "churri", "churr", "shlurrp");

    protected FrogmanRace() {
        super("Frogmen", MyColors.GREEN, 0, 0, 10, new Skill[]{},
                "Frogmen are a semi-intelligent tribal race prevalent in swamps, bogs and " +
                        "along rivers. They have large heads with fish-like eyes, bulbous bodies to which " +
                        "skinny extremities are attached. They form primitive societies and often avoid contact with " +
                        "members of other races.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return null;
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return null;
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return 0;
    }


    public static String makeFrogmanMumboJumbo(int wordCount) {
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < wordCount; ++i) {
            String word = MyRandom.sample(MUMBO_JUMBO);
            if (i == 0) {
                word = MyStrings.capitalize(word);
            }
            bldr.append(word);
            if (i < wordCount - 1) {
                if (MyRandom.randInt(10) == 0) { // Every 10th word pair gets hyphen
                    bldr.append("-");
                } else if (MyRandom.randInt(5) != 0) { // Every fifth word pair gets concatenated
                    bldr.append(" ");
                }
            }
        }
        return bldr.toString();
    }
}
