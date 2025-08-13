package model.tasks;

import model.Model;
import model.headquarters.Headquarters;
import model.map.UrbanLocation;
import model.states.DailyEventState;

public abstract class SummonTask extends DailyEventState {
    public SummonTask(Model model) {
        super(model);
    }

    public void doTask(Model model) {
        doEvent(model);
    }

    public abstract String getJournalDescription();

    protected boolean offerTransfer(Model model, Headquarters hq, UrbanLocation location, String verb) {
        Headquarters current = model.getParty().getHeadquarters();
        leaderSay("I would like to " + verb + ", but I already have a house in " + current.getLocationName() + ".");
        UrbanLocation loc = model.getWorld().getUrbanLocationByPlaceName(current.getLocationName());
        int sellPrice = current.getCost() / 2;
        portraitSay("Oh, I see. But If you like, I could send a letter to " +
                loc.getLordTitle() + " " + loc.getLordName() + " and tell " + himOrHer(loc.getLordGender()) +
                " to sell that house for " + sellPrice + " gold. Any belongings you have there, as well as your compatriots and " +
                "horses would be transferred to your new house.");
        if (current.getCharacters().size() > hq.getMaxCharacters() || current.getHorses().size() > hq.getMaxHorses()) {
            leaderSay("It seems this new house is smaller than our old one.");
            portraitSay("Yes, you are right. Some of your friends and horses would not fit in the new house.");
        }
        print("Sell your old home and transfer any belongings, characters and horses to the new one? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Alright, let's do it.");
            portraitSay("Good. I'll make all the arrangements. Consider your former residence sold.");
            println(location.getLordName() + " hands you " + sellPrice + " gold.");
            model.getParty().earnGold(sellPrice);
            current.transferTo(hq);
            return true;
        }
        return false;
    }
}
