package model.states.events;

import model.Model;
import util.MyPair;
import util.MyRandom;

import java.util.List;
import java.util.Map;

public abstract class MagicExpertGeneralInteractionEvent extends GeneralInteractionEvent {
    private static final List<String> SPELL_TIPS = List.of(
            "Chain Lightning is excellent for taking out many weak opponents.",
            "Disabling enemies with Conjure Phantasm can be a life saver in many situations.",
            "You can make custom magical combinations with the Combine spell. It's really quite special.",
            "Only good with one type of magic? You should get a copy of Channeling.",
            "Levitate can get you across rivers, among other things.",
            "Tired of walking? Check out Teleport.",
            "Southern Cross is a powerful healing spell, but you can only use it under the right conditions."
    );

    public MagicExpertGeneralInteractionEvent(Model model, String interactText, int stealMoney) {
        super(model, interactText, stealMoney);
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("spells", new MyPair<>("Got any good tips about spells?",
                MyRandom.sample(SPELL_TIPS)));
    }
}
