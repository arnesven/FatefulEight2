package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;

import java.util.List;

public class CleansingRitual extends DailyEventState {
    public CleansingRitual(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The monks are chanting, there is an elaborate tea " +
                "drinking ceremony and there is incense everywhere. " +
                "However, afterward the party members feel better than " +
                "they have in a long time");
        print("Each party member is restored to full health and recovers all SP!");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToHP(1000);
            gc.addToSP(1000);
        }
        for (int i = 0; i < 3; ++i) {
            model.getParty().randomPartyMemberSay(model, List.of("Refreshing!3", "I could stay here forever...",
                    "What pleasant smells and sounds.", "Peaceful.", "I feel... calm.", "Can I have some more tea?",
                    "I could be a monk.", "I feel soooo good."));
        }
    }
}
