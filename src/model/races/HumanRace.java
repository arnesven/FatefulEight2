package model.races;

import model.classes.Skill;
import view.MyColors;

public abstract class HumanRace extends Race {
    public HumanRace(String name, MyColors color, int hpModifier, int speed, Skill[] skillBonuses) {
        super(name, color, hpModifier, speed, skillBonuses);
    }
}
