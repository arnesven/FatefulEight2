package model.races;

import model.classes.Skill;
import view.MyColors;

public class GiantRace extends GigantoidRace {
    protected GiantRace() {
        super("Giant", MyColors.PEACH, 16, -6, 200, new Skill[0],
                "Giants, not to be confused with Stone Giants, are of Gigantoid origin, and " +
                        "are exceptionally rare. They stand roughly 20 feet tall. Their slow metabolism require them " +
                        "to eat large meals, which often take days to ingest, and then rest for weeks. " +
                        "Giants are more intelligent than their troll and ogre cousins, but rarely interact with " +
                        "common folk. One explanation may be that Giants have extremely long life times and may " +
                        "perceive the affairs and politics of common folk as fleeting or non-consequential.");
    }
}
