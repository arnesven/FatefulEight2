package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.items.Item;
import model.items.books.GelatinousBlobBook;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.ShopState;
import model.states.dailyaction.LodgingState;
import model.tasks.DestinationTask;
import model.tasks.GelatinousBlobTask;
import util.MyLists;
import util.MyRandom;
import view.JournalView;
import view.LogView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeagueOfMagesEvent extends DailyEventState {
    private static final String LEAGUE_OF_MAGES_MEMBER_KEY = "leagueOfMagesMember";
    private static final int ENTRANCE_FEE = 50;
    private ShopState shop = null;
    private boolean spentNight = false;

    public LeagueOfMagesEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit League of Mages",
                "There's an office of the League of Mages here");
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You pass by a building with an ornate sign out front. " +
                "'League of Mages - Guild Hall' is written in gilded letters upon it. " +
                "You step inside.");
        AdvancedAppearance app = PortraitSubView.makeRandomPortrait(Classes.MAGE);
        println("You enter into a medium sized vestibule. A robed figure sits behind a desk in the center of the room.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, app, "League Mage");
        UrbanLocation castle = (UrbanLocation) model.getCurrentHex().getLocation();
        portraitSay("Welcome to " + castle.getPlaceName() + " Office of the League of Mages. How can I help you?");
        if (!hasTask(model) && !isMember(model)) {
            leaderSay("Uhm, what is this place?");
            portraitSay("We are the League of Mages. We are an inter-kingdom organisation " +
                    "dedicated to promote, study, investigate, contain or otherwise deal with magical phenomena.");
            println("The mage pauses and looks you over, as to appraise the party.");
            int align = calculatePartyAlignment(model, this);
            if (align < -1) {
                portraitSay("But you hardly seem the type to be interested in our services. Please show yourselves out.");
                leaderSay("Fine. We don't need your hoity-toity magic anyway.");
                return;
            }
            portraitSay("We offer a few services to the public, like selling spells, magical training and improving " +
                    "spells and wands. Our members enjoy discounts and can stay at the guild halls. " +
                    "Members are also entitled to free teleportations between castles through the court mage at the keep. ");
        }
        do {
            List<String> options = new ArrayList<>(List.of("Ask about spells", "Ask about improving", "Ask about training", "Leave"));
            if (!isMember(model)) {
                options.add(3, "Ask about membership");
            } else {
                options.add(3, "Spend the night");
            }
            int index = multipleOptionArrowMenu(model, 24, 24, options);
            if (index == 0) {
                sellSpells(model);
            } else if (index == 1) {
                leaderSay("What did you say about improving?");
                portraitSay("Through the door to your left is the workshop of our enchanter.");
                ArtisanEvent enchanterEvent = new ArtisanEvent(model, false, new ArtisanEvent.Enchanter());
                enchanterEvent.doEvent(model);
            } else if (index == 2) {
                println("The League Mage offers to train you in the ways of being a wizard, ");
                ChangeClassEvent change = new ChangeClassEvent(model, Classes.WIZ);
                change.areYouInterested(model);
            } else {
                if (options.get(index).contains("Leave")) {
                    leaderSay("We'll come back another time.");
                    println("The mage nods courteously.");
                    break;
                } else if (options.get(index).contains("membership")) {
                    askAboutMembership(model, castle);
                } else {
                    leaderSay("We would like to use the dormitory.");
                    portraitSay("Of course. It's down the hall to the right. Please leave them " +
                            "in the same state as you found them.");
                    spentNight = true;
                    break;
                }
            }
            setCurrentTerrainSubview(model);
            removePortraitSubView(model);
            showExplicitPortrait(model, app, "League Mage");
            portraitSay("Can I help you with anything else?");
        } while (true);
    }

    @Override
    protected GameState getEveningState(Model model) {
        if (spentNight) {
            return new LodgingState(model, true);
        }
        return super.getEveningState(model);
    }

    private void askAboutMembership(Model model, UrbanLocation castle) {
        leaderSay("How does one become a member?");
        portraitSay("Membership requires three things. You must have at least one spell mastery, " +
                "you must complete a task assigned by the League and you must pay the membership fee of " + ENTRANCE_FEE + " gold.");
        GameCharacter leader = model.getParty().getLeader();
        boolean hasMastery = !leader.getMasteries().getAbilityList().isEmpty();
        if (hasMastery) {
            leaderSay("I have a spell mastery, " + leader.getMasteries().getAbilityList().get(0) + ".");
            portraitSay("Good for you.");
        } else {
            leaderSay("I don't have any spell masteries.");
            portraitSay("Then you need to cast more spells. Only diligent training will improve your magic abilities.");
        }
        if (!hasTask(model)) {
            leaderSay("What's the task?");
            println("The mage opens a desk drawer, brings out a book and hands it to you.");
            portraitSay("Here. This is a book on Gelatinous Blobs. The League needs these research notes confirmed.");
            model.getParty().getInventory().add(new GelatinousBlobBook());
            leaderSay("How do we do that?");
            portraitSay("You will have to find the different varieties of Gelatinous Blobs and vanquish them.");
            leaderSay("Okay...");
            Point position = model.getWorld().getPositionForLocation((HexLocation) castle);
            model.getParty().addDestinationTask(new GelatinousBlobTask(position));
            JournalEntry.printJournalUpdateMessage(model);
        } else {
            if (taskCompleted(model)) {
                leaderSay("We've completed the work with the Gelatinous Blobs.");
                portraitSay("Were the research notes accurate?");
                leaderSay("Accurate enough.");
                portraitSay("That's good to hear. Peer-reviewing is one of the foundations " +
                        "of sound magical research in practice.");
                if (hasMastery) {
                    leaderSay("Can I be a member now?");
                    portraitSay("As soon as you pay the membership entrance fee. Do you have " + ENTRANCE_FEE + " gold?");
                    if (model.getParty().getGold() <= ENTRANCE_FEE) {
                        leaderSay("Uhm... no. We're a bit short.");
                        portraitSay("Than unfortunately for you, you cannot become a member.");
                    } else {
                        print("Pay the fee of " + ENTRANCE_FEE + " gold to become a member of the League of Mages? (Y/N) ");
                        if (yesNoInput()) {
                            model.getParty().addToGold(-ENTRANCE_FEE);
                            println("You hand over the gold.");
                            portraitSay("Congratulations. You are now a member of the League of Mages. " +
                                    "Our services will now be available at a discount and you and your friends " +
                                    "can stay in our dorms at any time, free of charge.");
                            leaderSay("Thank you. That's great.");
                            model.getSettings().getMiscFlags().put(LEAGUE_OF_MAGES_MEMBER_KEY, true);
                            model.getLog().addAnimated(LogView.GOLD_COLOR +
                                    "You have become a member of the League of Mages. Their office will now appear when you visit castles.\n" + LogView.DEFAULT_COLOR);
                        } else {
                            leaderSay("Of course, but I think we'll hold back for now.");
                            portraitSay("It's actually a very affordable fee if you ask me. Good value, but the decision is yours.");
                        }
                    }
                } else {
                    portraitSay("But you still need to attain at least one spell mastery to " +
                            "qualify for League Membership. Come back when you have at least one spell mastery.");
                    for (GameCharacter gc : model.getParty().getPartyMembers()) {
                        if (gc != model.getParty().getLeader()) {
                            if (!gc.getMasteries().getAbilityList().isEmpty()) {
                                println(gc.getName() + " gently steps up and whispers.");
                                partyMemberSay(gc, "Maybe you should let me talk to him...");
                                break;
                            }
                        }
                    }
                }
            } else {
                leaderSay("We're still working on those Gelatinous Blobs.");
                portraitSay("Good for you.");
            }
        }
    }

    private boolean taskCompleted(Model model) {
        DestinationTask task = MyLists.find(model.getParty().getDestinationTasks(),
                (DestinationTask dt) -> dt instanceof GelatinousBlobTask);
        if (task == null) {
            return false;
        }
        return task.isCompleted();
    }

    private boolean hasTask(Model model) {
        return MyLists.any(model.getParty().getDestinationTasks(),
                (DestinationTask dt) -> dt instanceof GelatinousBlobTask);
    }

    public static boolean isMember(Model model) {
        return model.getSettings().getMiscFlags().get(LEAGUE_OF_MAGES_MEMBER_KEY) != null;
    }

    private void sellSpells(Model model) {
        portraitSay("I'm sorry if our prices aren't competitive. We don't have time to " +
                "check the market value of spell books.");
        randomSayIfPersonality(PersonalityTrait.stingy, new ArrayList<>(),
                "You should. We won't buy anything overpriced because of your laziness.");
        if (shop == null) {
            List<Item> items = new ArrayList<>();
            items.add(model.getItemDeck().getRandomWand());
            items.add(model.getItemDeck().getRandomPotion());
            items.add(model.getItemDeck().getRandomPotion());
            int noOfSpells = MyRandom.randInt(3, 6);
            for (int i = 0; i < noOfSpells; ++i) {
                items.add(model.getItemDeck().getRandomSpell());
            }
            Collections.shuffle(items);
            int[] costs = new int[items.size()];
            for (int i = 0; i < items.size() - 3; ++i) {
                if (isMember(model)) {
                    costs[i] = items.get(i).getCost() + MyRandom.randInt(-6, 6);
                } else {
                    costs[i] = items.get(i).getCost() + MyRandom.randInt(-8, 0);
                }
                if (costs[i] < 0) {
                    costs[i] = items.get(i).getCost() - 1;
                }
            }
            for (int i = items.size() - 3; i < items.size(); ++i) {
                costs[i] = items.get(i).getCost();
            }
            shop = new ShopState(model, "League Wizard", items, costs);
        }
        shop.run(model);
    }
}
