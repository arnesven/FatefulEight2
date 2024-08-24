package model.states.events;

import model.Model;
import util.MyPair;
import util.MyRandom;

import java.util.HashMap;
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
            "Southern Cross is a powerful healing spell, but you can only use it under the right conditions.",
            "The Resurrect spell is a real life saver, literally. But it is exhausting, and takes a long time to cast."
    );
    private static final List<String> VAMPIRE_TIPS = List.of(
            "Vampires need to feed on humanoid creatures to sustain themselves. That's why they're drawn to towns and castles.",
            "Vampirism can be cured. Look for stone circles out on the plains. Such places are often frequented by druids who know the right rituals for curing vampirism.",
            "The longer a vampire lives, the strong it becomes. A fully grown vampire is very fast, strong and intelligent. Be wary of such dark fiends.",
            "Vampires can turn into bats. Probably pretty convenient for them."
    );

    public MagicExpertGeneralInteractionEvent(Model model, String interactText, int stealMoney) {
        super(model, interactText, stealMoney);
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        Map<String, MyPair<String, String>> map = new HashMap<>();
        map.put("spells", new MyPair<>("Got any good tips about spells?",
                MyRandom.sample(SPELL_TIPS)));
        map.put("vampires", new MyPair<>("Know anything about vampires?",
                MyRandom.sample(VAMPIRE_TIPS)));
        return map;
    }
}
