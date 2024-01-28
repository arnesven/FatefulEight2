package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class PlowingFieldsEvent extends FieldsLaborEvent {

    public PlowingFieldsEvent(Model model) {
        super(model, "The farmer needs some help plowing his fields. Any help is much appreciated.",
                "in the field");
    }

    @Override
    protected boolean makeSkillRolls(Model model) {
        return model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 6);
    }

}
