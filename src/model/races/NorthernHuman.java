package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class NorthernHuman extends HumanRace {
    protected NorthernHuman() {
        super("Human", MyColors.PINK, 0, 0,
                new Skill[]{Skill.Persuade, Skill.Search, Skill.SeekInfo},
                "Northern humans are prevalent in the northern part of the world and are often found in " +
                        "towns, castles and farmlands. Their attributes are notably average in many regards. " +
                        "Northern humans take up all kinds of professions but it is not " +
                        "uncommon for them to be Bards, Thieves, Nobles, Spies, Magicians and Captains.");
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
    public String getQualifiedName() {
        return getName() + " (North)";
    }
}
