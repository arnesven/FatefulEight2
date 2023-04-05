package model.states.events;

import model.Model;
import model.classes.Skill;

import java.util.List;

public class MirageEvent extends LostEvent {
    private boolean lost = false;

    public MirageEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Up in the distance, the party sees a shimmering light " +
                "reflecting off of something. Is it a caravan, an oasis, a " +
                "city?");
        boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Perception, 10);
        if (result) {
            model.getParty().randomPartyMemberSay(model, List.of("Wait, that's just a mirage. Let's not get sidetracked people."));
        } else {
            this.lost = true;
            model.getParty().randomPartyMemberSay(model, List.of("Whatever it is, let's check it out!"));
            println("After walking for a while, the shimmering light can't be seen anymore.");
            model.getParty().randomPartyMemberSay(model, List.of("Was it just a mirage?"));
            super.doEvent(model);
        }
    }

    @Override
    public boolean haveFledCombat() {
        if (lost) {
            return super.haveFledCombat();
        }
        return false;
    }
}
