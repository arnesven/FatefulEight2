package model.states.events;

import model.Model;
import model.achievements.Achievement;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.items.Item;
import model.mainstory.GainSupportOfVikingsTask;
import model.mainstory.vikings.BeforeTrainMonksEveningState;
import model.mainstory.vikings.DonateEquipmentToMonastaryState;
import model.map.TownLocation;
import model.map.locations.VikingVillageLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.TransferItemState;
import model.states.TravelBySeaState;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class VisitMonasteryEvent extends DailyEventState {
    public static final String FACTION_NAME = "Sixth Order";
    private static final String FIRST_TIME_KEY = "firstTimeAtMonastery";
    private static final String PREVIOUS_DONATION_KEY = "previousDonation";
    private static final String PREVIOUS_REP_INCREASES = "previousRepIncreases";
    public static final int GOLD_PER_REP = 1000;
    private boolean didLeave = false;

    public VisitMonasteryEvent(Model model) {
        super(model);
    }

    public static boolean hasVisited(Model model) {
        return model.getSettings().getMiscFlags().get(FIRST_TIME_KEY) != null;
    }

    public static int getDonatedAmount(Model model) {
        Integer prevDon = model.getSettings().getMiscCounters().get(PREVIOUS_DONATION_KEY);
        if (prevDon == null) {
            return 0;
        }
        Integer prevRep = model.getSettings().getMiscCounters().get(PREVIOUS_REP_INCREASES);
        if (prevRep == null) {
            prevRep = 0;
        }
        return prevDon + prevRep * GOLD_PER_REP;
    }

    public static Achievement.Data getAchievmentData() {
        return new Achievement.Data(VisitMonasteryEvent.class.getCanonicalName(), "Monastary on Isle of Faith",
                "You donated at least 1000 gold to the Sixth Order monks to support them in their work " +
                        "of restoring the monastary on the Isle of Faith.");
    }

    @Override
    protected void doEvent(Model model) {
        GainSupportOfVikingsTask vikingTask = VikingVillageLocation.getVikingTask(model);
        if (vikingTask != null && vikingTask.isMonastaryRaided()) {
            monastaryInRuins(model);
            return;
        }

        if (!hasVisited(model)) {
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
            if (canWarn(vikingTask)) {
                print("Do you wish to warn the monk about the Viking raid? (Y/N) ");
                if (yesNoInput()) {
                    warnMonks(model, vikingTask);
                }
            } else {
                leaderSay("Bye...");
            }
            model.getTutorial().monastery(model);
            model.getSettings().getMiscFlags().put(FIRST_TIME_KEY, true);
        } else {
            showRandomPortrait(model, Classes.PRI, "Sixth Monk");
            portraitSay("Traveller. I hope you find our monastery to your liking. How can I help you?");
            do {
                List<String> options = new ArrayList<>(List.of("Make Donation", "Ask to Leave", "Cancel"));
                if (canWarn(vikingTask)) {
                    options.add(2, "Warn about Vikings");
                }
                if (vikingTask != null && vikingTask.isMonksWarned() && !vikingTask.hasDonatedEnoughEquipment()) {
                    options.add(2, "Give Equipment");
                }
                int selected = multipleOptionArrowMenu(model, 24, 24, options);
                if (selected == 0) {
                    makeDonation(model);
                } else if (selected == 1) {
                    if (travelBySea(model)) {
                        this.didLeave = true;
                        break;
                    }
                } else if (options.get(selected).contains("Warn")) {
                    warnMonks(model, vikingTask);
                } else if (options.get(selected).contains("Equipment")) {
                    if (donateEquipment(model, vikingTask)) {
                        break;
                    }
                } else {
                    break;
                }
                portraitSay("How else can I help you?");
            } while (true);
        }
    }

    private boolean canWarn(GainSupportOfVikingsTask vikingTask) {
        return vikingTask != null && vikingTask.isLokiMet() &&
                !vikingTask.isMonksWarned() && !vikingTask.isCompleted();
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
            if (amount >= GOLD_PER_REP) {
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

            int repIncreases = donation / GOLD_PER_REP;
            if (repIncreases > 0) {
                portraitSay("You have given so much to our cause. " +
                        "People far and wide will hear of your generosity!");
                completeAchievement(VisitMonasteryEvent.class.getCanonicalName());

                donation -= repIncreases * GOLD_PER_REP;
                leaderSay("I'm just glad we could help.");

                Integer previousIncreases = model.getSettings().getMiscCounters().get(PREVIOUS_REP_INCREASES);
                if (previousIncreases == null) {
                    previousIncreases = 0;
                }
                previousIncreases += repIncreases;
                model.getSettings().getMiscCounters().put(PREVIOUS_REP_INCREASES, previousIncreases);
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
            TravelBySeaState.travelBySea(model, destinations.get(selected), this, false, true);
            return true;
        }
        leaderSay("On second thought, no, there was something I wanted to do first.");
        portraitSay("Alright.");
        return false;
    }

    private void warnMonks(Model model, GainSupportOfVikingsTask vikingTask) {
        leaderSay("Just a minute. " + iOrWeCap() + "'ve come here to warn you about a grave threat.");
        portraitSay("By the Holy! Whatever is this threat?");
        leaderSay("A tribe of Vikings are planning to raid your monastary.");
        portraitSay("Oh the horror. What should we do?");
        leaderSay("Perhaps you could evacuate?");
        portraitSay("Impossible, we can't abandon our cause here at the monastary. But maybe we could " +
                "defend ourselves?");
        leaderSay("Do you have any combat training, or weapons?");
        portraitSay("Alas, we have neither. Perhaps you can help us?");
        leaderSay("Yes. How many of you are there?");
        portraitSay("There's several dozen of us, but I'd say only about twenty which are of fighting age.");
        leaderSay("Then we'll need about twenty swords, shields and armors.");
        portraitSay("Good heavens. Can you procure them for us?");
        leaderSay("I can, but it will be costly.");
        portraitSay("We have some gold. Let me fetch it. One moment, I'll be right back.");
        println("The monk rushes off into the monastary. After a few moment, the monk returns with a leather sack.");
        portraitSay("This is all we have. I hope it is enough. I think we'll need armor and shields too.");
        println("The party receives 650 gold!");
        model.getParty().addToGold(650);
        leaderSay("It's a start at least. " + iOrWeCap() + " will have to travel to the mainland to get the equipment. " +
                "While " + imOrWere() + " away you should build barricades and work improve the fortifications around the monastary.");
        portraitSay("I'll spread the word. Bless your soul for doing this!");
        leaderSay("It's the right thing to do.");
        randomSayIfPersonality(PersonalityTrait.lawful, List.of(model.getParty().getLeader()), "It truly is.");
        randomSayIfPersonality(PersonalityTrait.cold, List.of(model.getParty().getLeader()), "It's a waste of time, is what it is.");
        vikingTask.setMonksWarned();
    }

    private boolean donateEquipment(Model model, GainSupportOfVikingsTask vikingTask) {
        leaderSay(iOrWeCap() + " have got some equipment for you to arm yourself with.");
        portraitSay("Splendid! What do you have.");
        model.getLog().waitForReturn();
        List<Item> donatedItems = new ArrayList<>();
        SubView previous = model.getSubView();
        TransferItemState transfer = new DonateEquipmentToMonastaryState(model, donatedItems);
        transfer.setSellingMode(model);
        transfer.run(model);
        model.setSubView(previous);
        if (donatedItems.isEmpty()) {
            leaderSay("Actually, let me hang on to the stuff for now.");
            portraitSay("Oh, okay. Whatever you think is best.");
            return false;
        }
        List<Item> rejected = vikingTask.donateEquipmentToMonastary(donatedItems);
        if (!rejected.isEmpty()) {
            portraitSay("Some of this will not be of any use.");
            for (Item it : rejected) {
                it.addYourself(model.getParty().getInventory());
            }
            println("The monk handed some items back to you.");
        }
        if (vikingTask.hasDonatedEnoughEquipment()) {
            portraitSay("Thank you! I think we now have everything we need to properly defend ourselves against the raid.");
            leaderSay("Good. Distribute the equipment to every able person. " +
                    "And spread the word that combat training begins first thing tomorrow.");
            portraitSay("I will do that.");
            return true;
        }
        portraitSay("Thank you. I think we still need some more equipment to defend " +
                "ourselves against the viking raid.");
        boolean said = randomSayIfPersonality(PersonalityTrait.greedy, List.of(), "This stuff isn't cheap you know. Got any more money?");
        if (said) {
            portraitSay("We already gave you all the money we had! Please, we really need that equipment.");
        }
        leaderSay("It's coming. I promise.");
        return false;
    }

    @Override
    protected GameState getEveningState(Model model) {
        GainSupportOfVikingsTask task = VikingVillageLocation.getVikingTask(model);
        if (task != null && task.isMonksWarned() && task.hasDonatedEnoughEquipment()) {
            return new BeforeTrainMonksEveningState(model);
        }
        return super.getEveningState(model);
    }

    private void monastaryInRuins(Model model) {
        println("You approach the Monastary. It seems nobody has been here since " +
                "it was raided. It is now a desolate place, fallen into disrepair.");
        leaderSay("Now the Monastary will never be restored. The Sixth Order is no more.");
        randomSayIfPersonality(PersonalityTrait.cold, List.of(), "Who cares?");
        println("Down by the dock you see a small skiff moored. You approach and see that it is a fishing vessel.");
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.FARMER, "Fisherman");
        portraitSay("Huh? Nobody ever comes here anymore. This place was quite lovely while the monks were " +
                "tending it. Who are you?");
        leaderSay("Just some pilgrims. Can we travel with you back to the mainland?");
        portraitSay("Fine by me, I was heading back anyway. I can take you to Lower Theln.");
        print("Travel by boat to Lower Theln?");
        if (yesNoInput()) {
            leaderSay("Thanks");
            TownLocation destination = model.getWorld().getTownByName("Lower Theln");
            TravelBySeaState.travelBySea(model, destination, this, false, true);
        } else {
            leaderSay("On second thought, " + iOrWe() + " will stay a little longer.");
            portraitSay("Alright. Safe travels.");
        }
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
