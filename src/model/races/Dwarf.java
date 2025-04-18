package model.races;

import model.characters.appearance.HunkyNeck;
import model.characters.appearance.TorsoNeck;
import model.classes.Skill;
import util.MyRandom;
import view.MyColors;
import view.sprites.PortraitSprite;
import view.subviews.PortraitSubView;

public class Dwarf extends Race {
    public Dwarf() {
        super("Dwarf", MyColors.PINK, +2, -2, 30,
                new Skill[]{Skill.Axes, Skill.Endurance, Skill.Labor},
                "Dwarves are short, stocky individuals who are skilled in all kinds of crafting " +
                        "and manual labor. They are prevalent in hills, mountains and in caves. Dwarves have all " +
                        "kinds of professions but is not uncommon for them to be Miners, Foresters, Artisans, " +
                        "Paladins, Captains and Sorcerers.");
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return Race.normalLeftEar(hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return Race.normalRightEar(hairColor);
    }

    @Override
    public boolean isShort() {
        return true;
    }

    @Override
    public Shoulders makeShoulders(boolean gender) {
        return new HunkyShoulders(gender);
    }

    @Override
    public TorsoNeck makeNeck(boolean gender) {
        return new HunkyNeck();
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        if (race instanceof ElvenRace || race.id() == Race.HALF_ORC.id()) {
            return STRONG_DISLIKE_ATTITUDE;
        }
        if (race.id() == id()) {
            return POSITIVE_ATTITUDE;
        }
        return 0;
    }

    @Override
    public String getPlural() {
        return "Dwarves";
    }

    @Override
    public String getShortDescription() {
        return "short, stocky individuals. More often with beards than not.";
    }

    @Override
    public boolean isRandomMouthOk(boolean gender, int mouthIndex) {
        return super.isRandomMouthOk(gender, mouthIndex) &&
                (gender || PortraitSubView.isBeardyMouth(mouthIndex) || MyRandom.randInt(4) == 0);
    }
}
