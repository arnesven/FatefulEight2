package model.states.mine;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.items.potions.RumPotion;
import model.states.events.MinerEvent;
import util.MyRandom;

public class MineMinerEvent extends MinerEvent {
    public MineMinerEvent(Model model, CharacterAppearance appearance) {
        super(model, false, (AdvancedAppearance) appearance);
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        int dieRoll = MyRandom.rollD6();
        if (dieRoll <= 2) {
            return super.doMainEventAndShowDarkDeeds(model);
        } else if (dieRoll <= 4) {
            portraitSay("Plenty of amethysts down here, and topazes. Further down there are " +
                    "sapphires and emeralds though. Go down far enough and you may even find rubies. " +
                    "And oh... to find a diamond. One or two of those and I could retire...");
            println("The miner seems to stare off into space.");
            return true;
        }
        portraitSay("The work is tough done here. Good thing I brought something to strengthen me.");
        println("The miner brings out a small flask.");
        leaderSay("What's that, a strength potion?");
        portraitSay("No, no. Something way stronger. Here, I brought a spare. You can have it.");
        println("You got a bottle of rum.");
        new RumPotion().addYourself(model.getParty().getInventory());
        return true;
    }
}
