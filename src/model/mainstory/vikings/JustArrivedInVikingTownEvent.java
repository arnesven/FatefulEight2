package model.mainstory.vikings;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import view.subviews.PortraitSubView;

public class JustArrivedInVikingTownEvent extends DailyEventState {
    public JustArrivedInVikingTownEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        AdvancedAppearance app = PortraitSubView.makeRandomPortrait(Classes.VIKING, Race.NORTHERN_HUMAN);
        println("A large person approaches you. " + heOrSheCap(app.getGender()) + " looks angry.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, app, "Viking");
        portraitSay("Hey! You! You're an outsider.");
        leaderSay("Uhm, yes. But...");
        portraitSay("Leave now. You have no business here.");
        leaderSay("But we've been traveling a long way to get here!");
        portraitSay("Final warning.");
        print("Do you leave the Viking Town? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Alright alright, we'll go. Jeez.");
            setFledCombat(true);
            println("You turn around and head out of town.");
            return;
        }

        leaderSay("Can we perhaps talk to your leader?");
        portraitSay("That's it. You'll be sorry.");
        println("The viking stomps off in anger.");
        leaderSay("Did we offend " + himOrHer(app.getGender()) + " somehow?");
    }
}
