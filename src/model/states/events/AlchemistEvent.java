package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.items.Item;
import model.items.potions.Potion;
import model.races.Race;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyPair;

import java.util.ArrayList;
import java.util.List;

public class AlchemistEvent extends DailyEventState {
    public AlchemistEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.ALCHEMIST, Race.HALF_ORC, "Durok");
        println("As you walk down the street, you see a busy half-orc carrying boxes into a little shop. You casually " +
                "approach the half-orc. The facade of the shop looks brand new. 'Alchemy by Durok' is painted on the window.");
        leaderSay("Opening up a new shop?");
        portraitSay("Yes! Alchemy is in my blood and I've finally been able to follow my dreams of opening up a little apothecary.");
        model.getParty().randomPartyMemberSay(model, List.of("Can we see your wares?"));
        portraitSay("Uh, unfortunately it's still a bit of a mess inside. But actually, if you don't mind, I have " +
                "chore that needs doing. Would you spread the word about my new shop?");
        print("Will you help Durok? (Y/N) ");
        if (yesNoInput()) {
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Entertain, 8);
            if (success) {
                println("You go to the town square and start chanting about Durok's new business. You even manage to " +
                        "make up a little song. Before long, a few townsfolk gather around, amused and intrigued. " +
                        "You keep it up for a few hours. Afterwards, you return to Durok's shop.");
               portraitSay("There you are! I've had many customers since you left. Whatever you did, it worked. " +
                        "Please permit me to reward you!");
                Potion potion = model.getItemDeck().getRandomPotion();
                println("The party gains a " + potion.getName() + ".");
                model.getParty().getInventory().add(potion);
               portraitSay("I have lots of other potions, if you're interested. I'll give you a discount!");
                waitForReturn();
                alchemistShop(model, true);
            } else {
                println("You go to the town square and start to randomly shout at people about Durok's new shop.");
                model.getParty().randomPartyMemberSay(model, List.of("Nobody seems to care...",
                        "We're just scaring people away..."));
                println("You keep trying for some time, but ultimately you realize you're not getting anybody's " +
                        "attention. Frustrated and ashamed you decide to abandon the effort and go on with your day.");
                leaderSay("I'm sure Durok's business will take off once people get to know him.");
                println("You return to Durok's shop, which is quiet.");
                portraitSay("Oh, hello. Nice to see somebody in here. I wonder if people don't understand what " +
                        "an apothecary is?");
                model.getParty().randomPartyMemberSay(model, List.of("They're probably just shy..."));
                portraitSay("Well, I have lots of potions for sale. Are you interested?");
                waitForReturn();
                alchemistShop(model, false);
            }
        } else {
            leaderSay("Sorry, but we need to get on with our own business.");
        }
    }

    private void alchemistShop(Model model, boolean discount) {
        MyPair<List<Item>, int[]> result = new MyPair<>(new ArrayList<>(), new int[16]);
        for (int i = 0; i < 16; ++i) {
            Potion p = model.getItemDeck().getRandomPotion();
            result.first.add(p);
            if (discount) {
                result.second[i] = p.getCost() / 2;
            } else {
                result.second[i] = p.getCost();
            }
        }
        ShopState shop = new ShopState(model, "Alchemist", result.first, result.second);
        shop.run(model);
    }
}
