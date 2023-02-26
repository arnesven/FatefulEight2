package model;

import model.classes.Skill;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.events.SummonTask;

import java.io.Serializable;

public class Summon implements Serializable {
    public static final int ACCEPTED = 0;
    public static final int MET_LORD = 1;
    public static final int COMPLETE = 2;
    private int step = ACCEPTED;

    public SummonTask getTask(Model model, UrbanLocation location) {
        // TODO: Add a bunch more and keep track of which ones have been used.
        return new MissingGlassesTask(model, location);
    }

    public int getStep() {
        return step;
    }

    public void increaseStep() {
        step++;
    }

    private class MissingGlassesTask extends SummonTask {
        private final UrbanLocation location;

        public MissingGlassesTask(Model model, UrbanLocation location) {
            super(model);
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
                    increaseStep();
                    println(location.getLordName() + ": \"There they are, thank goodness. Here, let me pay you for your trouble.\"");
                    println("The party receives 50 gold.");
                    model.getParty().addToGold(50);
                }
            } else {
                println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind! " +
                        "They must be around here somewhere...\"");
            }
        }
    }
}
