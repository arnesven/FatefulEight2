package model.tasks;

import model.Model;
import model.Summon;
import model.map.UrbanLocation;

public abstract class GiveResourceTask extends SummonTask {
    private final int amount;
    private final Summon summon;
    private final UrbanLocation location;
    private final String firstPart;
    private final String resourceType;

    public GiveResourceTask(Model model, Summon summon, UrbanLocation location, String resourceType, int amount, String firstPart) {
        super(model);
        this.summon = summon;
        this.location = location;
        this.resourceType = resourceType;
        this.amount = amount;
        this.firstPart = firstPart;
    }

    protected abstract int getResource(Model model);

    protected abstract void reduceResource(Model model, int amount);

    @Override
    protected void doEvent(Model model) {
        portraitSay("" + firstPart +
                "but unfortunately we don't have the resources to do so.");
        leaderSay("How much do you need?");
        portraitSay("I think about " + amount + " " + resourceType + " would be enough.");
        if (getResource(model) >= amount) {
            print("Give " + amount + " " + resourceType + " to " + location.getPlaceName() + "? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Naturally, we will help you.");
                summon.increaseStep();
                reduceResource(model, amount);
                portraitSay("Thank you so much. Please allow me to compensate you.");
                println("The party gains 50 gold.");
                model.getParty().addToGold(50);
            } else {
                decline(model);
            }
        } else {
            decline(model);
        }
    }

    private void decline(Model model) {
        leaderSay("Hmm... well we'll keep our eyes open and get back to you.");
        portraitSay("Please do! We are eager to get the work started.");
    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " needs " + amount + " " + resourceType + ".";
    }
}
