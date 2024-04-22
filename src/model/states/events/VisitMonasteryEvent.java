package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.map.TownLocation;
import model.states.DailyEventState;
import model.states.TravelBySeaState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class VisitMonasteryEvent extends DailyEventState {
    private static final String FIRST_TIME_KEY = "firstTimeAtMonastery";
    private static final String PREVIOUS_DONATION_KEY = "previousDonation";
    private boolean didLeave = false;

    public VisitMonasteryEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean firstTime = model.getSettings().getMiscFlags().get(FIRST_TIME_KEY) == null;
        if (firstTime) {
            println("As you walk up a hill on this island you see a huge structure towering in front of you.");
            leaderSay("A castle? No... wait, it looks like a monastery.");
            randomSayIfPersonality(PersonalityTrait.intellectual, new ArrayList<>(),
                    "This must be the famous monastery on the isle of Faith! It is said it was once a fulcrum " +
                            "of worship in these parts of the world. But alas, it seems to have not withstood the test of time!");
            println("As you approach the hulking ruin you see monks moving about the grounds. One of them approaches you.");
            showRandomPortrait(model, Classes.PRI, "Sixth Monk");
            portraitSay("Greetings traveller. You are most welcome here at this outpost of peace.");
            leaderSay("What is this place?");
            portraitSay("This is the Isle of Faith, and what you see behind me is its once glorious Monastery.");
            randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(),
                    "Looks like you monks have your work cut out for you.");
            portraitSay("We have been restoring the structure. Our order, the followers of Sixth, are determined " +
                    "to make it as it once was, a safe haven for lost travelers and wayward souls.");
            leaderSay("I see. Would it be possible for us to rest here for the night?");
            portraitSay("Of course. Our kitchen has enough to feed many mouths. Our beds may be narrow, " +
                    "but they are quite comfortable.");
            randomSayIfPersonality(PersonalityTrait.greedy, new ArrayList<>(),
                    "And what do you benevolent monks charge for these services?");
            portraitSay("There is no charge, of course. And you may stay as long as you like.");
            leaderSay("Thank you. That is very generous of you. Please tell us if there is anything we " +
                    "can do to contribute to your noble project.");
            portraitSay("We would hope that you would think of us if you have a surplus of gold.");
            randomSayIfPersonality(PersonalityTrait.greedy, new ArrayList<>(), "I knew it! There's no such thing as a free lunch.");
            leaderSay("You accept donations?");
            portraitSay("Indeed we do. And you should know that we never forget the ones who help us.");
            leaderSay("We will consider it.");
            randomSayIfPersonality(PersonalityTrait.generous, new ArrayList<>(), "It's for a good cause.");
            leaderSay("Can I ask you one more thing? Is there any way to get off this island?");
            portraitSay("Certainly. Several of our monks are well trained in seafaring and " +
                    "can take you to a nearby port. Just talk to us again when you're ready to leave.");
            leaderSay("Thanks.");
            portraitSay("Please, excuse me now. I have matters to attend to. Again, welcome.");
            leaderSay("Bye...");
            model.getTutorial().monastery(model);
            model.getSettings().getMiscFlags().put(FIRST_TIME_KEY, true);
        } else {
            showRandomPortrait(model, Classes.PRI, "Sixth Monk");
            portraitSay("Traveller. I hope you find our monastery to your liking. How can I help you?");
            do {
                int selected = multipleOptionArrowMenu(model, 24, 24, List.of("Make Donation", "Ask to Leave", "Cancel"));
                if (selected == 0) {
                    makeDonation(model);
                } else if (selected == 1) {
                    if (travelBySea(model)) {
                        this.didLeave = true;
                        break;
                    }
                } else {
                    break;
                }
                portraitSay("How else can I help you?");
            } while (true);
        }
    }

    private void makeDonation(Model model) {
        leaderSay("We would like to make a donation, to sponsor the restoration of the Monastery.");
        portraitSay("Splendid. How much would you like to donate?");
        int amount = 0;
        do {
            try {
                print("Enter an amount you would like to donate: ");
                amount = Integer.parseInt(lineInput());
                if (amount > model.getParty().getGold() || amount < 0) {
                    print("That is not a valid amount. ");
                } else {
                    break;
                }
            } catch (NumberFormatException nfe) {
                print("Please enter an integer between 0 and " + model.getParty().getGold() + ". ");
            }
        } while (true);

        if (amount == 0) {
            leaderSay("On second thought, I think I'll hold on to my gold for now.");
            portraitSay("Oh... well, that's unfortunate.");
            leaderSay("Yeah...");
        } else {
            leaderSay("I would like to donate " + amount + " gold.");
            String reaction = "good.";
            if (amount >= 1000) {
                reaction = "wonderful!";
            } else if (amount >= 300) {
                reaction = "great!";
            } else if (amount >= 100) {
                reaction = "very good.";
            }
            portraitSay("A donation of " + amount + " gold, that's " + reaction +
                    " This will help make the Monastery into a wonderful place.");
            leaderSay("You're welcome. You need it more than we do.");
            model.getParty().addToGold(-amount);
            Integer previousDonation = model.getSettings().getMiscCounters().get(PREVIOUS_DONATION_KEY);
            int prev = previousDonation == null ? 0 : previousDonation;

            int donation = prev + amount;

            int repIncreases = donation / 1000;
            if (repIncreases > 0) {
                portraitSay("People far and wide will hear of your generosity!");
                println("Your reputation has increased by " + repIncreases + "!");
                model.getParty().addToReputation(repIncreases);
                donation -= repIncreases * 1000;
                leaderSay("I'm just glad we could help.");
            }
            model.getSettings().getMiscCounters().put(PREVIOUS_DONATION_KEY, donation);
        }
    }

    private boolean travelBySea(Model model) {
        leaderSay("We would like to leave. Is there a boat we can use?");
        portraitSay("Of course. " + generateBrotherName() + " will take you. Where would you like to go?");
        List<TownLocation> destinations = List.of(model.getWorld().getTownByName("Cape Paxton"),
                                                    model.getWorld().getTownByName("Ebonshire"),
                                                    model.getWorld().getTownByName("Lower Theln"));
        List<String> options = MyLists.transform(destinations, TownLocation::getTownName);
        int selected = multipleOptionArrowMenu(model, 24, 24, options);
        leaderSay("Can he take us to " + destinations.get(selected).getTownName() + "?");
        portraitSay("Without a doubt. Are you ready to go now?");
        print("Are you ready to travel to " + destinations.get(selected).getTownName() + "? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Yes. Let's go.");
            TravelBySeaState.travelBySea(model, new MyPair<>(destinations.get(0), 0), this);
            return true;
        }
        leaderSay("On second thought, no, there was something I wanted to do first.");
        portraitSay("Alright.");
        return false;
    }

    private String generateBrotherName() {
        if (MyRandom.flipCoin()) {
            return "Sister " + MyRandom.sample(List.of("Salva", "Nancy", "Fiona", "Mary", "Gertrude", "Debbie", "Berta"));
        }
        return "Brother " + MyRandom.sample(List.of("Maynard", "Patsy", "Ferric", "Stephen", "Golan", "Delby", "Buki"));
    }

    @Override
    protected boolean isFreeRations() {
        return !didLeave;
    }

    @Override
    protected boolean isFreeLodging() {
        return !didLeave;
    }
}
