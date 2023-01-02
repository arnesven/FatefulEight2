package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameOverState;

import java.util.List;
import java.util.Locale;

public class RopeBridgeEvent extends RiverEvent {
    private boolean walkAway = false;

    public RopeBridgeEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return walkAway;
    }

    @Override
    protected void doEvent(Model model) {
        print("A bridge of rope and planks is hoisted over the river, " +
                "it looks very old and worn. Crossing will obviously be " +
                "perilous.");
        print(" Do you try? (Y/N) ");
        if (yesNoInput()) {
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Acrobatics, 4);
            if (failers.isEmpty()) {
                println("The party manages to cross without incident.");
            } else {
                for (GameCharacter gc : failers) {
                    fallIntoRiver(model, gc, "has fallen into the river and tries to swim across!");
                }
            }
        } else {
            walkAway = true;
        }
    }
}
