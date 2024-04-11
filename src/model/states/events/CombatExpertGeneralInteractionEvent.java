package model.states.events;

import model.Model;
import util.MyPair;
import util.MyRandom;

import java.util.List;
import java.util.Map;

public abstract class CombatExpertGeneralInteractionEvent extends GeneralInteractionEvent {
    private static final List<String> COMBAT_TIPS = List.of(
            "Keep your tough guys in front, and your more vulnerable types in the back.",
            "Be sure to take full advantage of all your combat abilities.",
            "Don't forget about your items in combat.",
            "Armor isn't everything, a quick and agile fighter can evade many attacks.",
            "A little armor is loads better than none.",
            "Don't underestimate the value of a good shield.",
            "Outfit yourself according to your abilities. Only fighters with high endurance can handle the heaviest armors.",
            "Take care when moving from the front to the back row. Enemies may take advantage the situation!",
            "Know your enemies! Opponents with ranged attacks can be dangerous if you have exposed fighters in the back row.",
            "Know your enemies! Magic attacks can be devastating if you come unprepared.");

    public CombatExpertGeneralInteractionEvent(Model model, String interactText, int stealMoney) {
        super(model, interactText, stealMoney);
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("fighting", new MyPair<>("Can you give us any tips on fighting?",
                MyRandom.sample(COMBAT_TIPS)));
    }
}
