package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;

import java.util.List;

public class FertilityRitualEvent extends RitualEvent {
    public FertilityRitualEvent(Model model) {
        super(model, Skill.MagicGreen);
    }

    @Override
    protected CombatTheme getTheme() {
        return new GrassCombatTheme();
    }

    @Override
    protected void runEventIntro(Model model, List<GameCharacter> ritualists) {
        println("There are some mages here who are about to perform a fertility ritual. Do you wish to help them?");
    }

    @Override
    protected void runEventOutro(Model model) {
        println("The fertility of the land has been greatly improved.");
    }

}
