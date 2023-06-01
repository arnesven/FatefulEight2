package model.tasks;

import model.Model;
import model.Summon;
import model.classes.Skill;
import model.map.UrbanLocation;

import java.util.List;

public class HelpMeWithPuzzleTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public HelpMeWithPuzzleTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"Come over here and look at this table. I'm having some real troubles with this " +
                "jigsaw puzzle. Could you help me?\"");
        model.getParty().randomPartyMemberSay(model, List.of("Really? This is the problem you need help with?"));
        print("Do you wish to help with the jigsaw puzzle now? (Y/N) ");
        if (yesNoInput()) {
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Logic, 8);
            if (success) {
                println("You spend a few hours helping " + location.getLordName() + " with the puzzle.");
                summon.increaseStep();
                println(location.getLordName() + ": \"Ahh perfect, it's all done! Please let me give you something for spending your time here.\"");
                println("The party receives 15 gold.");
                model.getParty().addToGold(15);
            }
        } else {
            println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind! " +
                    "Hmm, am I missing a piece?\"");
        }
    }
}
