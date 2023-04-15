package model.tasks;

import model.Model;
import model.Summon;
import model.classes.Skill;
import model.map.UrbanLocation;

public class MissingGlassesTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public MissingGlassesTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"You see, I would ask some locals to handle it, " +
                "but it's quite embarrassing and I was, uh, hoping we could handle this rather discretely? " +
                "I've lost my glasses somewhere, and I just can't find them! I can't work without them and " +
                "some of the townsfolk are starting to think I'm just slacking off. Will you please find them " +
                "for me? I'll pay you of course!\"");
        print("Do you wish to go find " + location.getLordName() + " glasses now? (Y/N) ");
        if (yesNoInput()) {
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 10);
            if (success) {
                summon.increaseStep();
                println(location.getLordName() + ": \"There they are, thank goodness. Here, let me pay you for your trouble.\"");
                println("The party receives 25 gold.");
                model.getParty().addToGold(25);
            }
        } else {
            println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind! " +
                    "They must be around here somewhere...\"");
        }
    }
}
