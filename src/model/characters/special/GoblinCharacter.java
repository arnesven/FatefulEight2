package model.characters.special;

import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.conditions.Condition;
import model.combat.conditions.VampirismCondition;
import model.races.Race;

import static model.classes.Classes.None;

public class GoblinCharacter extends GameCharacter {
    public GoblinCharacter() {
        super("Goblin", "", Race.GOBLIN, Classes.GOBLIN,
                new GoblinAppearance());
    }

    @Override
    protected boolean hasConditionImmunity(Condition cond) {
        return cond instanceof VampirismCondition;
    }
}
