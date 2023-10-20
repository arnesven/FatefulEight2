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
        println("Fertility event intro text TODO");
    }

    @Override
    protected void runEventOutro(Model model) {
        println("Fertility event outro text TODO");
    }

}
