package model.races;

import model.classes.Skill;
import view.MyColors;

public class OgreRace extends GigantoidRace {
    protected OgreRace() {
        super("Ogre", MyColors.TAN, 4, -3, 50, new Skill[0],
                "Ogres, the smallest of the Gigantoids, rarely grow larger than 9 feet tall. " +
                        "They are common in woods and swamplands and form small communities. " +
                        "They tend to keep to themselves, not wanting to be disturbed in their hunting, " +
                        "gathering and primitive agriculture.");
    }
}
