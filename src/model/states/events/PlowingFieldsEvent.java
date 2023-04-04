package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class PlowingFieldsEvent extends FieldsLaborEvent {
    private boolean freeRations;

    public PlowingFieldsEvent(Model model) {
        super(model);
        freeRations = false;
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        println("A farmer needs some help plowing his fields. Any help is much appreciated.");
        boolean succ = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 6);
        if (succ) {
            success(model);
        } else {
            failure(model, "in the field");
        }
    }

    @Override
    public boolean isFreeRations() {
        return freeRations;
    }
}
