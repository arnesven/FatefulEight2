package model.states.events;

import model.Model;
import model.classes.Skill;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.DailyEventState;

public class WildHorseEvent extends DailyEventState {
    public WildHorseEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You spot a wild horse up ahead, peacefully grazing by the side of the path.");
        leaderSay("Perhaps we can tame it?");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 6);
        if (success) {
            println("The horse seems to have taken a liking to you and willingly lets you mount it.");
            Horse horse = HorseHandler.generateHorse();
            model.getParty().getHorseHandler().addHorse(horse);
            println("The party got a " + horse.getName() + ".");
        } else {
            println("The horse prances away and quickly disappears out of sight.");
        }
    }
}
