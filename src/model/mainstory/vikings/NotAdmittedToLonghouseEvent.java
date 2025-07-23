package model.mainstory.vikings;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.mainstory.GainSupportOfVikingsTask;
import model.states.DailyEventState;

import java.util.List;

public class NotAdmittedToLonghouseEvent extends DailyEventState {
    private final GainSupportOfVikingsTask task;

    public NotAdmittedToLonghouseEvent(Model model, GainSupportOfVikingsTask task) {
        super(model);
        this.task = task;
    }

    @Override
    protected void doEvent(Model model) {
        println("Two large, very aggressive looking vikings are guarding the entrance to the longhouse.");
        print("Do you attempt to persuade them to let you enter? (Y/N) ");
        if (yesNoInput()) {
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 18);
            if (success) {
                println("Just as the guards are about to pound you to oblivion, you manage to explain " +
                        "the reason for your presence in a concise and convincing manner. Baffled by your eloquence " +
                        "the guards let you into the longhouse.");
                new MeetWithChieftainEvent(model, false, task).run(model);
                return;
            }
            println("The guards are unconvinced and come at you with weapons drawn. Other vikings who are passing by " +
                    "are taking note of the situation.");
            leaderSay(iOrWeCap() + "'d better leave now before this gets ugly.");
        } else {
            leaderSay("Hmm... These guys don't look too friendly. Provoking them would be unwise.");
            randomSayIfPersonality(PersonalityTrait.cowardly, List.of(), "A very wise standpoint!");
        }
        leaderSay("In fact, everybody in this village seems pretty upset with " + myOrOur() + " presence. " +
                "Somehow " + iOrWe() + " must convince them that we are worthy of an alliance.");
        println("You turn away from the longhouse.");
    }
}
