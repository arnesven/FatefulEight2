package model.states.events;

import model.Model;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class ChoppingWoodEvent extends FieldsLaborEvent {
     public ChoppingWoodEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A farmer needs help chopping wood. Any help is much appreciated.");
        boolean succ = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Axes, 7);
        succ &= model.getParty().doCollaborativeSkillCheck(model, this, Skill.Endurance, 6);
        if (succ) {
            super.success(model);
        } else {
            super.failure(model, "while chopping the wood");
        }
    }
}
