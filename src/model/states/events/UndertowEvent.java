package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;

import java.util.List;

public class UndertowEvent extends RiverEvent {
    private boolean turnedBack = false;

    public UndertowEvent(Model model) {
        super(model, true);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return turnedBack;
    }

    @Override
    protected void doRiverEvent(Model model) {
        println("The party comes to a narrower part of the river and " +
                "decides on trying to swim. A few feet into the water a " +
                "strong current starts tugging at the party members' legs. ");
        leaderSay("Do we turn back, or press on?");
        print("Do you turn back? (Y/N) ");
        if (yesNoInput()) {
            turnedBack = true;
        } else {
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Endurance, 5);
            model.getParty().getHorseHandler().someHorsesRunAway(model);
            if (failers.isEmpty()) {
                println("The party manages to swim across.");
            } else {
                for (GameCharacter gc : failers) {
                    fallIntoRiver(model, gc, "is being swept away by the current and must fight hard to survive!");
                }
            }
        }
    }
}
