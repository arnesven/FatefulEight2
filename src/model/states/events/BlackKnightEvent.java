package model.states.events;

import model.Model;
import model.classes.Classes;
import view.subviews.RiverCombatTheme;
import model.enemies.BlackKnightEnemy;
import view.subviews.CollapsingTransition;

import java.util.List;

public class BlackKnightEvent extends RiverEvent {
    private boolean didFlee = false;

    public BlackKnightEvent(Model model) {
        super(model, true);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return didFlee;
    }

    @Override
    protected void doRiverEvent(Model model) {
        showRandomPortrait(model, Classes.BKN, "Black Knight");
        println("A narrow bridge spans the width of the river. Upon it, " +
                "a knight in black armor stands guard.");
        leaderSay("Um, excuse us. Can we cross the bridge?");
        portraitSay("None shall pass.");
        leaderSay("Come again?");
        portraitSay("None shall pass!");
        model.getParty().randomPartyMemberSay(model, List.of("I think we're going to have to fight him if we want to cross here."));
        print("Do you fight the black knight? (Y/N) ");
        if (yesNoInput()) {
            runCombat(List.of(new BlackKnightEnemy('A')), new RiverCombatTheme(), true);
            CollapsingTransition.transition(model, RiverEvent.subView);
            if (!super.haveFledCombat()) {
                portraitSay("You have proven a worthy adversary.");
                println("The black knight offers to instruct you in his martial ways, ");
                ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.BKN);
                changeClassEvent.areYouInterested(model);
            }
        } else {
            println("The party decides to not provoke the black knight today.");
            didFlee = true;
        }
    }
}
