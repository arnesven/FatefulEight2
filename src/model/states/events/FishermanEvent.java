package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.items.special.GoodFishingPole;
import model.items.weapons.FishingPole;

import java.util.ArrayList;

public class FishermanEvent extends RiverEvent {
    private static final int COST = 15;

    public FishermanEvent(Model model) {
        super(model, false);
    }

    @Override
    public String getDistantDescription() {
        return "a fisherman";
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return false;
    }

    @Override
    protected void doRiverEvent(Model model) {
        println("The party encounters a fisherman on the banks of the river.");
        showRandomPortrait(model, Classes.FARMER, "Fisherman");
        leaderSay("Good day to you sir. Is the river safe to cross here?");
        portraitSay("Shhh! You'll scare the fish away.");
        randomSayIfPersonality(PersonalityTrait.mischievous, new ArrayList<>(),
                "OH, SORRY, WE DIDN'T REALIZE YOU WERE FISHING!");
        portraitSay("Oh well... I wasn't having much fishing luck anyway. " +
                "Guess I'm going home empty-handed again. Or... you wouldn't be interested in purchasing this " +
                "fine fishing pole? I'll let you have it for " + COST + " gold!");
        randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(),
                "Oh come on, we're not that gullible.");
        if (model.getParty().getGold() >= COST) {
            print("Buy the fishing pole? (Y/N) ");
        }
        if (model.getParty().getGold() >= COST && yesNoInput()) {
            leaderSay("Fine, we'll take it.");
            portraitSay("Excellent. Here you go.");
            println("You lost " + COST + " gold");
            model.getParty().spendGold(COST);
            println("You got a fishing pole.");
            model.getParty().getInventory().add(new GoodFishingPole());
            leaderSay("We'll be going now.");
            portraitSay("Good-bye!");
        } else {
            leaderSay("No thank you. We'll be going now.");
            portraitSay("Well, it was worth a shot.");
        }
    }
}
