package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class PlowingFieldsEvent extends FieldsLaborEvent {

    public PlowingFieldsEvent(Model model) {
        super(model, "Plowing Fields", "The farmer needs some help plowing his fields. Any help is much appreciated.",
                "in the field");
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Help farmer plow",
                "I know a farmer nearby who always needs help plowing his fields.");
    }

    @Override
    protected boolean makeSkillRolls(Model model) {
        return model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 6);
    }

}
