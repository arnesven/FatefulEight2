package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.DailyEventState;

import java.util.List;

public class PriestEvent extends DailyEventState {
    private final boolean withIntro;

    public PriestEvent(Model model, boolean withIro) {
        super(model);
        this.withIntro = withIro;
    }

    public PriestEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.PRI, "Priest");
        if (withIntro) {
            print("The party meets a priest who ");
        } else {
            print("The priest ");
        }
        print("offers to bless the members of the party - for a small 'donation'. ");
        while (true) {
            if (model.getParty().getGold() < 5) {
                println("Unfortunately you cannot afford any more 'donations' right now.");
                break;
            }
            print("Would you like to pay 5 gold to bless a party member? (Y/N) ");
            if (yesNoInput()) {
                GameCharacter gc = model.getParty().partyMemberInput(model, this, null);
                println("The priest blesses " + gc.getName() + ".");
                gc.addToHP(1000);
                gc.addToSP(1);
                model.getParty().addToGold(-5);
                model.getParty().partyMemberSay(model, gc, List.of("I feel refreshed!",
                        "I feel like a new person!", "What a cleansing feeling!",
                        "I feel reborn!", "Ahhh, that did wonders for me!"));
            } else {
                break;
            }
        }
        print("The priest also offers to guide you in the ways of priesthood, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.PRI);
        changeClassEvent.areYouInterested(model);
        println("You part ways with the priest.");
    }
}
