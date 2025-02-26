package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.states.DailyEventState;
import view.subviews.PortraitSubView;

import java.util.ArrayList;

public class GuestEvent extends DailyEventState {
    private final CharacterAppearance appearance;

    public GuestEvent(Model model, CharacterAppearance appearance) {
        super(model);
        this.appearance = appearance;
    }

    @Override
    public String getDistantDescription() {
        return "a quite farmstead";
    }

    public GuestEvent(Model model) {
        this(model, PortraitSubView.makeRandomPortrait(Classes.FARMER));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit farmstead",
                "There's a farm here. The family there never turns away travellers");
    }

    @Override
    protected void doEvent(Model model) {
        showExplicitPortrait(model, this.appearance, "Farmer");
        println("The party is invited into the home of these farmers who " +
                "gracefully offer a warm meal and the prospect of sleeping in " +
                "the barn for the night.");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.benevolent, new ArrayList<>(), "What an honor.");
        if (!didSay) {
            randomSayIfPersonality(PersonalityTrait.rude, new ArrayList<>(),
                    "They have beds to offer, and yet they let us sleep in the barn like vagabonds!");
        }
        model.getLog().waitForAnimationToFinish();
    }

    @Override
    protected boolean isFreeRations() {
        return true;
    }
}
