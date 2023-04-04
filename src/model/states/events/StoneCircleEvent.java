package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class StoneCircleEvent extends DailyEventState {
    public StoneCircleEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.DRU, "Druid");
        print("In a wide field, the party encounters a ring of standing " +
                "stones. In the middle lay one large slab which seems to be " +
                "intended as an altar. A druid is there with a gathering of " +
                "a few followers. She is just about to perform a nature " +
                "ritual so you wait until the ceremony has concluded. " +
                "Afterwards she offers teachings of druidism but asks for " +
                "some small compensation. ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.DRU);
        if (model.getParty().getGold() > 0 && change.noOfCandidates() > 0) {
            int amount = Math.min(model.getParty().getGold(), 10);
            print("Pay " + amount + " gold? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-amount);
                change.doEvent(model);
            }
            println("You part ways with the druid.");
        } else if (change.noOfCandidates() > 0) {
            println("Since you have no money, you decline and depart the stone circle.");
        } else {
            println("But nobody in your party is suitable for such teachings. You depart the stone circle.");
        }
    }
}
