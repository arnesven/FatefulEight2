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
        println(location.getLordName() + ": \"" + firstPart +
                "but unfortunately we don't have the resources to do so.\"");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "How much do you need?");
        println(location.getLordName() + ": \"I think about " + amount + " " + resourceType + " would be enough.\"");
        if (getResource(model) >= amount) {
            print("Give " + amount + " " + resourceType + " to " + location.getPlaceName() + "? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Naturally, we will help you.");
                summon.increaseStep();
                reduceResource(model, amount);
                println(location.getLordName() + ": \"Thank you so much. Please allow me to compensate you.\"");
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
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Hmm... well we'll keep our eyes open and get back to you.");
        println(location.getLordName() + ": \"Please do! We are eager to get the work started.\"");
    }
}