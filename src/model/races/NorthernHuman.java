package model.races;

import model.classes.Skill;
import view.MyColors;
import view.sprites.PortraitSprite;

public class NorthernHuman extends HumanRace {
    protected NorthernHuman() {
        super("Human", MyColors.PINK,
                new Skill[]{Skill.Persuade, Skill.Search, Skill.SeekInfo},
                "Northern humans are prevalent in the northern part of the world and are often found in " +
                        "towns, castles and farmlands. Their attributes are notably average in many regards. " +
                        "Northern humans take up all kinds of professions but it is not " +
                        "uncommon for them to be Bards, Thieves, Nobles, Spies, Magicians and Captains.");
    }

    @Override
    public MyColors getFreckleColor() {
        return MyColors.PEACH;
    }

    @Override
    public String getQualifiedName() {
        return getName() + " (North)";
    }
}
