package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.combat.conditions.VampirismCondition;
import model.enemies.FaeryEnemy;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class FaeriesEvent extends DailyEventState {
    public FaeriesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Faeries", "Exhausted after a day of travelling through the rough " +
                "the party stumbles into a clearing. As you sit down to " +
                "rest you notice small glowing orbs all around you. ");
        model.getParty().randomPartyMemberSay(model, List.of("Lightning bugs?"));
        model.getParty().randomPartyMemberSay(model, List.of("No, it's something else..."));
        model.getParty().randomPartyMemberSay(model, List.of("There are voices, as if children are laughing and singing."));
        model.getParty().randomPartyMemberSay(model, List.of("Faeries!3"));
        println("The faeries are keeping their distance and assessing the party...");
        int partyAlign = calculatePartyAlignment(model, this);
        if (partyAlign > 0) {
            println("The faeries swoop in and seem elated and cheerful.");
            println("Each party member regains 3 health and 1 stamina.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                gc.addToHP(3);
                if (!gc.hasCondition(VampirismCondition.class)) {
                    gc.addToSP(1);
                }
            }
            randomSayIfPersonality(PersonalityTrait.calm, new ArrayList<>(), "So relaxing.");
        } else if (partyAlign < -1) {
            model.getParty().randomPartyMemberSay(model, List.of("Uh oh... They look pissed. What did we do?"));
            println("The faeries attack the party!");
            runCombat(List.of(new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'),
                    new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'),
                    new FaeryEnemy('A'), new FaeryEnemy('A')));
        } else {
            println("The faeries just fly away.");
            model.getParty().randomPartyMemberSay(model, List.of("Awww", "No wait, come back!",
                    "I guess they didn't trust " + meOrUs() + "."));
        }
    }
}
