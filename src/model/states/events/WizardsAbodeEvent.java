package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class WizardsAbodeEvent extends DailyEventState {
    private boolean admitted;

    public WizardsAbodeEvent(Model model) {
        super(model);
        admitted = false;
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.WIZ, "Wizard");
        println("A white stone structure pokes out from the surrounding " +
                "hills. A wizard lives here and gladly accepts some " +
                "company. He seems a jovial fellow and presents the party " +
                "with an enticing riddle. No visitor has been able to solve " +
                "it for many years and he offers a beautiful golden trinket " +
                "if the party can solve the riddle.");
        boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Logic, 8);
        if (result) {
            admitted = true;
            println("The wizard admits the party into his home and hands over the trinket (the party receives 10 gold).");
            model.getParty().addToGold(10);
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.WIZ);
            println("After a scrumptious supper, the wizard offers to teach you about his trade, ");
            change.areYouInterested(model);
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("Agh, I don't get it. Let's just give up.#"));
            println("The wizard is annoyed and refuses to accept you into his abode.");
        }
    }

    @Override
    protected boolean isFreeRations() {
        return admitted;
    }
}
