package model.tasks;

import model.Model;
import model.Summon;
import model.classes.Skill;
import model.map.UrbanLocation;

public class FencingTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public FencingTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"I've been challenged to a duel by one of my rivals. I've told everybody " +
                "that I'm a masterful swordsman, but the truth is I barely know which end to hold! Will you teach me?\"");
        print("Do you wish to give " + location.getLordName() + " fencing lessons now? (Y/N) ");
        if (yesNoInput()) {
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Blades, 8);
            if (success) {
                println(location.getLordName() + " gets a thorough lesson in fencing.");
                summon.increaseStep();
                println(location.getLordName() + ": \"Wow! I feel much more confident now. Please let me compensate you\"");
                println("The party receives 25 gold.");
                model.getParty().addToGold(25);
            }
        } else {
            println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind!");
        }
    }
}
