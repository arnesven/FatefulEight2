package model.races;

import model.classes.Skill;
import view.MyColors;

public class TrollRace extends GigantoidRace {
    protected TrollRace() {
        super("Troll", MyColors.GRAY, 6, -4, 80, new Skill[0],
                "Trolls are a Gigantoid race of semi-intelligent class. " +
                        "Their bodies are wide and rough, and often grow to a height of 10 feet. " +
                        "They tend to be rather aggressive and ill-tempered and are widely feared by common folk. " +
                        "Trolls often steal livestock and food from farmers, but sometimes they fish in rivers and lake.");
    }
}
