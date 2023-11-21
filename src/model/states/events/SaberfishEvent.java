package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import util.MyLists;
import util.MyRandom;

import java.util.List;

public class SaberfishEvent extends RiverEvent {
    public SaberfishEvent(Model model) {
        super(model, true);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return false;
    }

    @Override
    protected void doRiverEvent(Model model) {
        println("The party wades out into the shallows, vicious fish start " +
                "nipping at their legs!");
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToHP(-1));
        removeKilledPartyMembers(model, false);
        model.getParty().randomPartyMemberSay(model, List.of("Ouch!#", "Damn fishies!#", "Stop eating me!#",
                "I though fish were vegetarians.", "Hey! that hurt..."));
        GameCharacter gc = MyRandom.sample(model.getParty().getPartyMembers());
        SkillCheckResult result = gc.testSkill(Skill.Survival, 10);
        if (result.isSuccessful()) {
            println(gc.getName() + " manages to catch some of the fish! (Survival " + result.asString() + ")");
            model.getParty().addToFood(MyRandom.randInt(2,5));
            model.getParty().partyMemberSay(model, gc, List.of("They are actually quite tasty.",
                    "Eat or get eaten...", "Gotcha you little devil!", "Hey look, dinner!",
                    "Anybody care for fish tonight?"));
        }
    }
}
