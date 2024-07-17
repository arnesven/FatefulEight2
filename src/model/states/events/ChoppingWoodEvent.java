package model.states.events;

import model.Model;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class ChoppingWoodEvent extends FieldsLaborEvent {
     public ChoppingWoodEvent(Model model) {
        super(model, "The farmer needs help chopping wood. Any help is much appreciated.",
                "while chopping the wood");
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Help farmer chop wood", "I know a farmer nearby who always needs help chopping wood");
    }

    @Override
    protected boolean makeSkillRolls(Model model) {
        boolean succ = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Axes, 7);
        succ = succ && model.getParty().doCollaborativeSkillCheck(model, this, Skill.Endurance, 6);
        return succ;
    }
}
