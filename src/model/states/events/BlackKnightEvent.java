package model.states.events;

import model.Model;
import model.classes.Classes;
import model.enemies.BlackKnightEnemy;
import model.states.CombatEvent;
import view.subviews.CollapsingTransition;

import java.util.List;

public class BlackKnightEvent extends RiverEvent {
    private boolean didFlee = false;

    public BlackKnightEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return didFlee;
    }

    @Override
    protected void doEvent(Model model) {
        println("A narrow bridge spans the width of the river. Upon it, " +
                "a knight in black armor stands guard.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), List.of("Um, excuse us. Can we cross the bridge?"));
        println("Black knight: \"None shall pass.\"");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), List.of("Come again?"));
        println("Black knight: \"None shall pass!\"");
        model.getParty().randomPartyMemberSay(model, List.of("I think we're going to have to fight him if we want to cross here."));
        print("Do you fight the black knight? (Y/N) ");
        if (yesNoInput()) {
            CombatEvent combat = new CombatEvent(model, List.of(new BlackKnightEnemy('A')));
            combat.run(model);
            CollapsingTransition.transition(model, RiverEvent.subView);
            didFlee = combat.fled();
            if (!didFlee) {
                println("Black knight: \"You have proven a worthy adversary.\"");
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
