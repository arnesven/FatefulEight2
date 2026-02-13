package model.states.events;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.RandomAppearance;
import model.classes.Classes;
import model.combat.CombatAdvantage;
import model.combat.conditions.CowardlyCondition;
import model.enemies.Enemy;
import model.enemies.SmugglerEnemy;
import model.items.Equipment;
import model.items.accessories.GrayRing;
import model.items.accessories.HeavyRing;
import model.items.clothing.ScaleArmor;
import model.items.potions.LethalPoison;
import model.items.weapons.Glaive;
import model.items.weapons.TwoHandedSword;
import model.map.TownLocation;
import model.races.AllRaces;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class SmugglersEvent extends LetterOnTheStreetEvent {
    private AdvancedAppearance constableAppearance = null;
    private String meetingPlace;

    public SmugglersEvent(Model model) {
        super(model);
    }

    public static Achievement.Data getAchievementData() {
        return new Achievement.Data(SmugglersEvent.class.getCanonicalName(), "Busted Smugglers",
                "You helped the constables expose and arrested a ring of smugglers, moving a lethal " +
                        "and illegal poison called 'Devil's Draught'.");
    }

    @Override
    protected void innerDoEvent(Model model) {
        this.meetingPlace = "behind the tavern";
        if (model.getCurrentHex().getLocation() instanceof TownLocation &&
            ((TownLocation) model.getCurrentHex().getLocation()).hasWaterAccess()) {
            meetingPlace = "down by the docks";
        }
        leaderSay("Hmm... let's open it... It just says 'Got the shipment. " +
                "Meet me tonight " + meetingPlace + ", bring the money'.");
        if (model.getParty().size() > 1) {
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                    "Sounds kind of suspicious.");
            leaderSay("Sure does.");
        }
        boolean didSay = randomSayIfPersonality(PersonalityTrait.lawful, List.of(model.getParty().getLeader()),
                "We should report this");
        if (!didSay) {
            randomSayIfPersonality(PersonalityTrait.mischievous, List.of(model.getParty().getLeader()),
                    "Why not make the rendezvous, to find out more?");
        }
        println("What do you do?");
        int choice = multipleOptionArrowMenu(model, 24, 28,
                List.of("Ignore it", "Go to meeting place", "Report to constable"));
        if (choice == 0) {
            leaderSay("But it's not our problem. Moving on.");
            return;
        }
        if (choice == 1) {
            goToMeetingPlace(model, false);
        } else {
            reportToConstable(model);
        }
    }

    private void goToMeetingPlace(Model model, boolean withConstable) {
        println("You wait for nightfall, then you go " + meetingPlace + ".");
        if (withConstable) {
            portraitSay("Okay, now listen. I'll hide behind that corner while you meet with " +
                    "whoever sent the letter. If anything illegal seems to be going on, just say 'looks like bad weather', " +
                    "and I'll pop out like a jack-in-the-box. Got that?");
            leaderSay("Crystal clear.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
        println("You wait for about twenty minutes...");
        int numberOfSmugglers = MyRandom.randInt(4, 8);
        println("Suddenly, some shady figures approaches you, there are " + MyStrings.numberWord(numberOfSmugglers) +
                " of them. One of them speaks up.");
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.THF, "Smuggler");
        portraitSay("Good evening.");
        leaderSay("Hello, I've been waiting. Got the goods?");
        portraitSay("Right here.");
        println("One of the smugglers holds up a crate. Inside are two dozen bottles.");
        leaderSay("Pumpkin juice?");
        portraitSay("Heh, yeah, the most lethal 'pumpkin juice' in the realm. Got the coin?");
        leaderSay("Uhm, yeah... how much did we agree on again?");
        portraitSay("Are you stupid or something? It's 100 gold, like always.");

        List<String> options = new ArrayList<>(List.of("Attack smuggler", "Refuse to pay"));
        if (model.getParty().getGold() >= 100) {
            options.add("Pay 100 gold");
        }
        if (withConstable) {
            options.add("Signal constable");
        }
        int choice = multipleOptionArrowMenu(model, 24, 28, options);
        if (choice == 0) {
            println("You draw your weapon. Hand the stuff over, or else!");
            portraitSay("What's this, a set up? Come on then, you've made a big mistake!");
            combatSmugglers(model, numberOfSmugglers, withConstable, false);
        } else if (choice == 1) {
            leaderSay("100 gold? That's outrageous.");
            portraitSay("Wait a minute, you're not the person I was supposed to meet, are you?");
            leaderSay("...");
            portraitSay("We can't leave any witnesses. Hack'em to bits!");
            combatSmugglers(model, numberOfSmugglers, withConstable, false);
        } else if (options.get(choice).contains("Pay 100")) {
            leaderSay("Here's the gold.");
            println("You hand over 100 gold.");
            model.getParty().loseGold(100);
            portraitSay("Shipment's all yours. Until next time then.");
            getShipment(model);
            if (withConstable) {
                println("After the smugglers leave, the constable approaches you.");
                model.getLog().waitForAnimationToFinish();
                showConstablePortrait(model);
                portraitSay("No funny business?");
                leaderSay("No sir. Just an honest merchant peddling goods in an odd place at an odd time.");
                portraitSay("Fair enough then. If you'll excuse me.");
            }
        } else {
            leaderSay("Uhm, LOOKS LIKE BAD WEATHER!");
            portraitSay("What are you shouting about? I see nothing but a clear sky... Hh crap the cops!");
            model.getLog().waitForAnimationToFinish();
            showConstablePortrait(model);
            portraitSay("I'm busting this deal wide open scum!");
            combatSmugglers(model, numberOfSmugglers, false, true);
        }
    }

    private void combatSmugglers(Model model, int numberOfSmugglers, boolean constableAfter, boolean constableFights) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = numberOfSmugglers; i > 0; --i) {
            enemies.add(new SmugglerEnemy('A'));
        }
        for (Enemy e : enemies) {
            e.addCondition(new CowardlyCondition(enemies));
        }

        List<GameCharacter> allies = new ArrayList<>();
        if (constableFights) {
            CharacterAppearance copy = constableAppearance.copy();
            allies.add(new GameCharacter("Constable", "", copy.getRace(), Classes.CAP, copy,
                        new Equipment(new TwoHandedSword(), new ScaleArmor(), new GrayRing())));
        }

        runCombat(enemies, model.getCurrentHex().getCombatTheme(), true, CombatAdvantage.Neither, allies);
        setCurrentTerrainSubview(model);
        if (haveFledCombat()) {
            println("You run out from the city limits.");
            return;
        }
        if (MyLists.all(enemies, Enemy::isDead)) {
            if (constableFights) {
                showConstablePortrait(model);
                portraitSay("Now, let's have a look at this 'shipment'. Whoa, it's a big crate of poison bottles!");
                leaderSay("Poison?");
                portraitSay("Yes. This is 'Devil's Draught', it will kill anybody who has the tiniest sip. " +
                        "I better get this stored away safely.");
                leaderSay("Yes. Don't want that stuff to get lost.");
                portraitSay("You've done a great service for our town.");
                if (model.getParty().getNotoriety() > 0) {
                    portraitSay("To tell you the truth, I know you've had trouble with the law in the past. " +
                            "But I'm willing to clear your record as thanks.");
                    leaderSay("That would be mighty kind of you.");
                    model.getParty().addToNotoriety(-model.getParty().getNotoriety());
                    leaderSay("But stay on the right side of the law from now on.");
                    leaderSay("You got it. So long, constable.");
                    portraitSay("Good bye then.");
                } else {
                    portraitSay("I'll make sure people in high places hear about this. " +
                            "This will open up some doors for you and your company.");
                    leaderSay("I appreciate that!");
                    completeAchievement(SmugglersEvent.class.getCanonicalName());
                    portraitSay("Good bye then.");
                }
            } else {
                leaderSay("There... now we can just help ourselves to this 'shipment'.");
                getShipment(model);
            }
        } else {
            leaderSay("One of the smugglers must have gotten away with the crate. That's too bad.");
            if (constableFights) {
                portraitSay("How unfortunate, well. At least now they may think twice before doing their shady deals " +
                        "in our town again. Thank you for your service.");
                println(model.getParty().getLeader().getFirstName() + " makes a little salute. " +
                        "Then you wander off about your business.");
            }
        }
        if (constableAfter) {
            println("The constable comes rushing up to you.");
            showConstablePortrait(model);
            portraitSay("What is going on here!");
            leaderSay("It was as we feared. These scoundrels were smuggling some " +
                    "kind of dangerous substance.");
            portraitSay("That's terrible! Where is the stuff?");
            leaderSay("They must have gotten away with it.");
            portraitSay("How unfortunate, well. At least now they may think twice before doing their shady deals " +
                    "in our town again. Thank you for your service.");
            println(model.getParty().getLeader().getFirstName() + " makes a little salute. " +
                    "Then you wander off about your business.");
        }
    }

    private void getShipment(Model model) {
        println("The party receives 24 " + new LethalPoison().getName() + ".");
        for (int i = 24; i > 0; --i) {
            new LethalPoison().addYourself(model.getParty().getInventory());
        }
    }

    private void reportToConstable(Model model) {
        leaderSay("We're taking this to the authorities.");
        println("You seek out the constable.");
        model.getLog().waitForAnimationToFinish();
        showConstablePortrait(model);
        portraitSay("What seems to be the trouble citizen?");
        leaderSay("We found this suspicious note. Could it be smugglers?");
        portraitSay("Hmm... yes, this does look like it may be some kind of covert operation. " +
                "Thank you for bringing this to our attention, we'll take it from here.");
        print("Do you offer to help? (Y/N) ");
        if (!yesNoInput()) {
            if (model.getParty().getLeader().hasPersonality(PersonalityTrait.greedy)) {
                leaderSay("What, no reward?");
                portraitSay("Hmph! It's only a vague lead. Most likely only a wild goose chase. " +
                        "Now move along, no loitering!");
                leaderSay("Bah... let's go.");
            } else {
                println(model.getParty().getLeader().getFirstName() + " makes a little salute. " +
                        "Then you wander off about your business.");
            }
            return;
        }
        leaderSay("Maybe we can help?");
        portraitSay("You? Well... fine. To tell the truth, we are quite short staffed. If you would accompany me " +
                "to the meeting place, we may eliminate some risk. But you better be ready to follow orders.");
        leaderSay("Yes sir!");
        goToMeetingPlace(model, true);
    }

    private void showConstablePortrait(Model model) {
        if (constableAppearance == null) {
            this.constableAppearance = PortraitSubView.makeRandomPortrait(Classes.CONSTABLE);
        }
        showExplicitPortrait(model, constableAppearance, "Constable");
    }
}
