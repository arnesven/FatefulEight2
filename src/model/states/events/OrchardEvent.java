package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class OrchardEvent extends DailyEventState {
    public OrchardEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        println("The party suddenly finds itself in a lovely orchard, " +
                "with fruit hanging everywhere. As one member of your " +
                "party reaches for a low hanging piece of fruit, a farmer " +
                "suddenly pops up behind a bush. Embarrassed, you " +
                "try to explain that you are lost and thought the trees " +
                "were wild.");
        portraitSay("Don't worry! I have " +
                "way more fruit than I can ever sell at the market. Take " +
                "as much as you want!");
        randomSayIfPersonality(PersonalityTrait.gluttonous, new ArrayList<>(), "We should take as many as we can carry!");
        randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(), "It's only apples...");
        int gained = 5 * model.getParty().size();
        println("The party gains " + gained + " rations.");
        model.getParty().addToFood(gained);
        model.getParty().randomPartyMemberSay(model, List.of("Yummy!3"));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit orchard", "There's an orchard nearby, it's filled with juice fruit");
    }
}
