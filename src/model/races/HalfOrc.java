package model.races;

import model.characters.appearance.ThickNeck;
import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import util.MyRandom;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class HalfOrc extends Race {
    protected HalfOrc() {
        super("Half-Orc", MyColors.ORC_GREEN, 1, -1, 25,
                new Skill[]{Skill.BluntWeapons, Skill.Endurance, Skill.Survival},
                "Half-Orcs are a race which have form from millennia of sporadic breeding between " +
                        "Orcs and Humans (or in some cases Elves or Dwarves). They are hardy, stout, rugged " +
                        "individuals who come across rougher than they actually are. Half-Orcs can be found all " +
                        "over the world but are more prevalent in hills and mountainous areas. Half-Orcs take up " +
                        "all kinds of professions but it is not uncommon for them to be Foresters, Miners, " +
                        "Barbarians, Captains and Druids.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x71, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x81, hairColor);
    }

    @Override
    public MyColors getFreckleColor() {
        return MyColors.GRAY;
    }

    @Override
    public Shoulders makeShoulders(boolean gender) {
        if (gender) {
            return new NormalShoulders(true);
        }
        return new BroadShoulders(gender);
    }

    @Override
    public TorsoNeck makeNeck(boolean gender) {
        if (gender) {
            return super.makeNeck(gender);
        }
        return new ThickNeck();
    }

    @Override
    public MyColors getMouthDefaultColor() {
        return MyColors.DARK_GRAY;
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        if (race.id() == id()) {
            return POSITIVE_ATTITUDE;
        }
        if (race.id() == Race.DWARF.id()) {
            return STRONG_DISLIKE_ATTITUDE;
        }
        return 0;
    }

    @Override
    public String getShortDescription() {
        return "hardy, stout and rugged, but most of them aren't as rough as you think.";
    }

    @Override
    public boolean isRandomMouthOk(boolean gender, int mouthIndex) {
        return mouthIndex == 7 || MyRandom.randInt(6) == 0; // Try to get tusks more often.
    }
}
