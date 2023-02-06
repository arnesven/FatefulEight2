package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class StormEvent extends DailyEventState {
    public StormEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Dark clouds are looming and drops start to fall on your " +
                "heads.");
        model.getParty().randomPartyMemberSay(model, List.of("Looks like bad weather...", "Here it comes...",
                "Get ready to get wet.", "Well it could be worse."));
        println("Soon a strong torrent of rain is coming. The party " +
                "is now severely impeded by the downpour and the strong wind.");
        model.getParty().randomPartyMemberSay(model, List.of("It's worse.", "Why does this always happen to me?",
                "Even my underwear is wet!", "Can we get a fire going?"));
        print("Do you seek shelter (Y) or trudge on through the storm (N)? ");
        if (yesNoInput()) {
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 7);
            if (!success) {
                println("Your attempts at seeking shelter have failed and you have lost your way." +
                        " You will have to spend the entire day tomorrow to try to find your way back" +
                        " to the path.");
                model.mustStayInHex(true);
            }
        } else {
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getSP() == 0) {
                    if (gc.getHP() > 0) {
                        println(gc.getName() + " suffers 1 damage from the storm.");
                    }
                } else {
                    println(gc.getName() + " loses 1 stamina from the storm.");
                }
            }
        }
    }
}
