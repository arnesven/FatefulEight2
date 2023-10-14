package model.states.events;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.states.DailyEventState;
import view.subviews.PortraitSubView;

public class GuestEvent extends DailyEventState {
    private final CharacterAppearance appearance;

    public GuestEvent(Model model, CharacterAppearance appearance) {
        super(model);
        this.appearance = appearance;
    }

    public GuestEvent(Model model) {
        this(model, PortraitSubView.makeRandomPortrait(Classes.FARMER));
    }

    @Override
    protected void doEvent(Model model) {
        showExplicitPortrait(model, this.appearance, "Farmer");
        println("The party is invited into the home of these farmers who " +
                "gracefully offer a warm meal and the prospect of sleeping in " +
                "the barn for the night.");
    }

    @Override
    protected boolean isFreeRations() {
        return true;
    }
}
