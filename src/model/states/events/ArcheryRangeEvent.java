package model.states.events;

import model.Model;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class ArcheryRangeEvent extends DailyEventState {
    public ArcheryRangeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("There is an archery range on the outskirts of town. The marksman there approaches you.");
        println("Marksman: \"Hello there. Want to practice your marksmanship? For 5 gold I'll lend you " +
                " a bow if you don't have one and some arrows.\"");
        model.getParty().randomPartyMemberSay(model, List.of("I don't know... Do we really have time for this?"));
        println("Marksman: \"Let's make it more interesting. If you hit the bull's eye, I'll give you 30 gold. Deal?\"");

        while (model.getParty().getGold() >= 5) {
            print("Do you pay 5 gold to play? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-5);
                boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Bows, 10);
                if (result) {
                    model.getParty().randomPartyMemberSay(model, List.of("Bullseye!", "Right on target!",
                            "Dead-center.", "Can't get much more in the middle than that.",
                            "Nice shot!"));
                    println("The marksman looks rather surprised.");
                    println("Marksman: \"Nice shot indeed. Here's your gold.\"");
                    println("The party receives 30 gold.");
                    model.getParty().addToGold(30);
                    println("Marksman: \"If you'll excuse me, I have to go now. Please come and" +
                            " see me again some time.\"");
                    break;
                } else {
                    model.getParty().randomPartyMemberSay(model, List.of("I think the arrow went over the target.",
                            "That's a miss.", "Aaw, so close!"));
                    println("Marksman: \"That's too bad. But you can always try again. Whaddaya say?\"");
                }
            } else {
                break;
            }
        }
        model.getParty().randomPartyMemberSay(model, List.of("Maybe we can come back later."));
    }
}
