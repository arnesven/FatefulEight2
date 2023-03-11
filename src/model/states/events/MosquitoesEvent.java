package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;

public class MosquitoesEvent extends DailyEventState {
    public MosquitoesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("As large as a fists, and with an unquenchable thirst for blood. Such are the damned insects in this wretched " +
        "place.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "How does any traveller deal with this pestilence?");
        boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 7);
        if (result) {
            println("Fortunately one experienced party member has just " +
                    "to thing to rub on your skin to repel the locust.");
        } else {
            println("Each party member suffers 1 damage and exhausts 1 SP.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                gc.addToSP(-1);
                if (gc.getHP() > 1) {
                    gc.addToHP(-1);
                }
            }
        }
    }
}
