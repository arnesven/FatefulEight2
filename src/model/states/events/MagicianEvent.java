package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class MagicianEvent extends DailyEventState {
    public MagicianEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        super.showRandomPortrait(model, Classes.MAG, "Magician");
        println("A traveling magician has set up his stage next to the road " +
                "and you can hear him calling to the small crowd that has gathered here.");
        portraitSay(model, "Ladies and gentlemen, step right up! Come into my tent " +
                    "to see astounding thaumaturgy. Right before your very " +
                    "eyes, I will...");
        int cost = model.getParty().size();
        if (cost > model.getParty().getGold()) {
            println("Unfortunately, your purse is so light you cannot even afford to see the show, and you pass by" +
                    " wondering what kind of spectacle you are missing.");
        } else {
            print("Do you wish to pay " + cost + " gold to see show? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-cost);
                print("The show is truly very good and you stay a while after it has concluded to complement " +
                        "the magician. He thanks you and offers to give you some pointers in the field of " +
                        "magical showmanship, ");
                ChangeClassEvent changeEvents = new ChangeClassEvent(model, Classes.MAG);
                changeEvents.areYouInterested(model);
            }
        }
        println("You part ways with the traveling magician.");
    }
}
