package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.weapons.CrystalWand;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.races.Race;
import model.states.GameState;
import model.states.duel.MagicDuelEvent;
import model.states.duel.gauges.PowerGauge;
import model.tasks.BountyDestinationTask;
import util.MyRandom;
import view.MyColors;
import view.subviews.DuelingContestSubView;
import view.subviews.PortraitSubView;
import view.subviews.TournamentSubView;

import java.util.*;

public class MagicDuelContest extends TournamentEvent {
    private final CastleLocation castle;
    private boolean sponsored;
    private final Map<GameCharacter, MyColors> knownColors = new HashMap<>();
    private final Map<GameCharacter, PowerGauge> knownGauges = new HashMap<>();

    public MagicDuelContest(Model model, CastleLocation castleLocation) {
        super(model, castleLocation);
        this.castle = castleLocation;
    }

    @Override
    protected void doEvent(Model model) {
        print("The " + castle.getLordTitle() + " is hosting a magic dueling contest today. " +
                "Do you wish to attend? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        println("Outside the castle walls many tents and pavilions have been erected. And there, " +
                "on a stretch of lawn, an arena has been set up for the purpose of hosting magical duels.");
        println("As you wander around you see mages, fair ladies, noblemen, merchants and commoners " +
                "all bustling about and getting ready for the contest. Some people are lining up at a little booth " +
                "where a small gentleman in fancy clothing is accepting coins and writing things down in big ledgers.");
        model.getLog().waitForAnimationToFinish();
        showOfficial();
        portraitSay("Yes, we're still accepting participants. Are you here to compete in the dueling contest?");
        leaderSay("Perhaps. What are the parameters?");
        portraitSay("The entry fee is " + ENTRY_FEE + " gold. Eight duelists will enter the contest. There will be one-on-one " +
                "face offs until only one remains. Duels continue until one duelist has been hit five times or more! The winner receives " +
                "a marvelous prize, the Crystal Wand. Oh, and you'll also receive the " + castle.getLordTitle() + "'s blessing - which is worth more than you can imagine.");
        portraitSay("But all of you can't enter the contest, in fact we only have room for one more. Are you still interested?");
        this.sponsored = false;
        if (model.getParty().getGold() < ENTRY_FEE) {
            println("You are about to reply that you can't afford it when a shady fellow in a hood steps up behind you.");
            showSponsor();
            portraitSay("You look like you're good with spells and stuff, I can front the money for the contest. " +
                    "But if you win you'll owe me a small favor. What do you say?");
            print("Do you accept the stranger's sponsorship? (Y/N) ");
            sponsored = true;
        } else {
            print("Will you enter one of your party members into the contest? (Y/N) ");
        }
        if (yesNoInput()) {
            enterTournament(model);
        } else {
            leaderSay("I think we'll just watch this time.");
            println("You spend the day watching the contest, which is both exciting and interesting. " +
                    "Afterwards you feel inspired to try some magic dueling yourself.");
        }
    }

    private void enterTournament(Model model) {
        if (!sponsored) {
            println("The party pays " + ENTRY_FEE + " gold to the official.");
            model.getParty().addToGold(-ENTRY_FEE);
        }
        print("Which party member do you wish to enter into the contest?");
        GameCharacter chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        if (chosen.hasPersonality(PersonalityTrait.narcissistic)) {
            partyMemberSay(chosen, "I'll grace this contest with my presence.");
        } else {
            partyMemberSay(chosen, "I'll enter the tournament.");
        }
        super.showOfficial();
        portraitSay("There needs to be a name on the entry. What is your name?");
        partyMemberSay(chosen, "'" + chosen.getName() + "', spelled just the way it sounds.");
        portraitSay("Okay then. That means we have all eight duelists! Let me just randomize the setup.");
        println("The official places eight slips of paper into his hat, and then pulls them out one by one. For each one " +
                "he writes the name on a large sign next to the booth.");
        portraitSay("This is the match tree. It shows who will duel with whom and what the outcome has been " +
                "as the duels conclude. Please have a look at it now.");
        print("Press enter to continue.");
        waitForReturn();
        List<GameCharacter> duelists = makeDuelists(model, 7);
        duelists.add(MyRandom.randInt(duelists.size()), chosen);
        TournamentSubView tournamentSubView = new DuelingContestSubView(duelists, knownColors, knownGauges);
        model.setSubView(tournamentSubView);
        waitForReturnSilently();
        setCurrentTerrainSubview(model);

        List<GameCharacter> winners = new ArrayList<>();
        List<GameCharacter> losers = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            GameCharacter duelistB = duelists.remove(duelists.size() - 1);
            GameCharacter duelistA = duelists.remove(duelists.size() - 1);
            if (i == 0) {
                showOfficial();
                portraitSay("It looks like the first duel is going to be " + duelistA.getName() + " versus " +
                        duelistB.getName() + ", how exciting! You should hurry to the dueling arena!");
                println("You make your way over to the dueling arena. Up on a podium, a man is speaking behind a large cone which " +
                        "is amplifying his voice enough for everyone to hear.");
            } else {
                println("You hear the announcer start to call out that the duel is about to start, so you walk back over to the " +
                        "dueling arena and take your seats again.");
            }
            GameCharacter winner = performOneDuel(model, duelistA, duelistB);
            winners.add(0, winner);
            losers.add(duelistB == winner ? duelistA : duelistB);
            List<GameCharacter> current = new ArrayList<>(duelists);
            current.addAll(winners);
            if (i == 0) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match. ");
            } else if (i < 3) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match.");
            } else {
                announcerSay("That concludes the quarter matches. We will now have a longer break before we proceed " +
                        "to the semi-finals.");
            }
            model.getLog().waitForAnimationToFinish();
            if (checkForOutOfTournament(current, chosen)) {
                handleSponsorWhenLost(model);
                return;
            }
            lookAtBoard(model, current);
        }

        doLongBreak(model);
        duelists.addAll(winners);
        announcerSay("We are about ready to start the semi-finals! Please take your seats.");

        winners.clear();
        losers.clear();
        for (int i = 0; i < 2; ++i) {
            GameCharacter duelistB = duelists.remove(duelists.size() - 1);
            GameCharacter duelistA = duelists.remove(duelists.size() - 1);
            if (i == 1) {
                println("You hear the announcer start to call out that the duel is about to start, so you walk back over to the " +
                        "arena and take your seats again.");
            }
            GameCharacter winner = performOneDuel(model, duelistA, duelistB);
            if (model.getParty().isWipedOut()) {
                return;
            }
            winners.add(0, winner);
            losers.add(duelistB == winner ? duelistA : duelistB);
            List<GameCharacter> current = new ArrayList<>(duelists);
            current.addAll(winners);
            if (i == 0) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match.");
            } else {
                announcerSay("That concludes the semi-finals. We will now have a longer break before we proceed " +
                        "to the final fight between " + winners.get(0).getName() + " and " + winners.get(1).getName() + "!");
            }
            if (checkForOutOfTournament(current, chosen)) {
                handleSponsorWhenLost(model);
                return;
            }
            lookAtBoard(model, current);
        }

        doLongBreak(model);
        duelists.addAll(winners);
        announcerSay("We are about ready to start the final duel! Please take your seats.");
        GameCharacter winner = performOneDuel(model, duelists.get(0), duelists.get(1));
        announcerSay("That means we have a winner ladies and gentlemen... It's " + winner.getName() + "!");
        if (winner == chosen) {
            println(chosen.getName() + " accepts the prize, the Crystal Wand!");
            new CrystalWand().addYourself(model.getParty().getInventory());
            if (sponsored) {
                handleSponsorWhenWon(model);
            }
        } else {
            leaderSay("Well, we lost.");
            if (chosen.isDead()) {
                leaderSay("And we lost " + chosen.getFirstName() + "... It hasn't been a good day.");
            } else {
                leaderSay("At least we didn't lose " + chosen.getFirstName() +
                        "... Let's focus on the tasks before us instead.");
            }
            handleSponsorWhenLost(model);
        }
    }

    private void doLongBreak(Model model) {
        println("Taking advantage of the longer break, you have a stroll around to look at some of the side attractions.");
        new FoodStandsEvent(model).doEvent(model);
        setCurrentTerrainSubview(model);
        println("You're surprised at how quickly time has passed when you again hear the voice of the announcer.");
    }

    private void lookAtBoard(Model model, List<GameCharacter> current) {
        println("You get up from your seats and walk over to the board next to the booth where you signed up " +
                "for the tournament. It has already been updated.");
        model.getLog().waitForAnimationToFinish();
        TournamentSubView tournamentSubView = new DuelingContestSubView(current, knownColors, knownGauges);
        model.setSubView(tournamentSubView);
        waitForReturnSilently();
        setCurrentTerrainSubview(model);
    }

    private void handleSponsorWhenLost(Model model) {
        if (!sponsored) {
            return;
        }
        println("You turn away from the tournament grounds and start back toward the castle when the mysterious stranger " +
                "suddenly pops up as out of nowhere.");
        showSponsor();
        portraitSay("Rotten luck friend. I really thought you were going to win. " +
                "The way I see it, you owe me " + ENTRY_FEE + " gold.");
        leaderSay("I'm no richer now than I was this morning.");
        portraitSay("That's okay friend, you can pay the Brotherhood back later.");
        addToEntryFeeToLoan(model);
        println("The mysterious stranger just smiles, then he disappears.");
        leaderSay("Wait... what did we just agree to?");
    }

    private void handleSponsorWhenWon(Model model) {
        println("As you're about to start celebrating your victory, the mysterious stranger " +
                "suddenly pops up as out of nowhere");
        showSponsor();
        portraitSay("Congratulations friend. Now it's time to for you to uphold your part of the bargain.");
        leaderSay("Yes... what was the little favor you needed doing?");
        portraitSay("Oh, not much. You just need to kill somebody for me.");
        leaderSay("What? That... that's not at all what I expected!");
        portraitSay("Nevertheless, a deal is a deal.");
        println("The mysterious stranger holds out a roll of paper, bound with a crimson ribbon.");
        portraitSay("Here, a writ of execution. It contains details on your target.");
        print("Do you refuse to accept the writ of execution? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("No. " + iOrWe() + " won't do this. Can't you find somebody else to do your dirty work?");
            portraitSay("I'm sure the brotherhood can reclaim the write and hand it to another assassin. The " +
                    "cost however, will be a dept that you must pay later.");
            super.addToEntryFeeToLoan(model);
            super.addToEntryFeeToLoan(model);
            println("The mysterious stranger just smiles, then he disappears.");
            leaderSay("Wait... what did we just agree to?");
        } else {
            leaderSay("Fine. I'm sure it's a very bad person anyway.");
            println("You grab the writ and unroll it to read its contents. When you look up, " +
                    "the mysterious stranger has disappeared once more.");
            // TODO: add AssassinationDestinationTask
            //JournalEntry.printJournalUpdateMessage(model);
        }
    }

    private boolean checkForOutOfTournament(List<GameCharacter> current, GameCharacter chosen) {
        if (!current.contains(chosen)) {
            leaderSay("Well, that's it, we're out of the contest.");
            println("Disappointed in your misfortune, you leave the contest area and try to set your mind to your adventuring instead.");
            return true;
        }
        return false;
    }

    private GameCharacter runRealDuel(Model model, GameCharacter partyMember, GameCharacter opponent) {
        println(partyMember.getName() + " enters the dueling arena...");
        MagicDuelEvent duelEvent = new MagicDuelEvent(model, false, opponent, partyMember);
        if (knownGauges.containsKey(opponent)) {
            duelEvent.setPreselectedOpponentGauge(knownGauges.get(opponent));
            duelEvent.setShowOpponentGauge(true);
        }
        if (knownColors.containsKey(opponent)) {
            duelEvent.setShowOpponentColor(true);
        }
        duelEvent.doTheEvent(model);
        GameCharacter winner = duelEvent.getWinner();
        setCurrentTerrainSubview(model);
        announcerSay("The duel is over! " + winner.getName() + " is the victor!");
        return winner;
    }

    private void announcerStartOfCombat(GameCharacter duelistA, GameCharacter duelistB) {
        announcerSay("And now, ladies and gentlemen, we are about to see a " +
                MyRandom.sample(List.of("fierce", "exciting", "hectic")) + " " +
                MyRandom.sample(List.of("duel", "face off", "standoff", "showdown", "match")) + " between two skilled opponents!");
        announcerSay("One one side, we have a" + present(duelistA) + ". Let's have a big round of applause for... " +
                duelistA.getName() + "!");
        announcerSay("And on the other side, we have a" + present(duelistB) + ". Let's have another big round of applause for... " +
                duelistB.getName() + "!");
        getModel().getLog().waitForAnimationToFinish();
    }

    private GameCharacter runAbstractedNPCDuel(Model model, GameCharacter duelistA, GameCharacter duelistB) {
        println("The two duelist perform well, but in the end, one of them comes out on top.");
        GameCharacter winner = duelistB;
        if (MyRandom.flipCoin()) {
            winner = duelistA;
        }
        announcerSay("Ladies and gentlemen. The duel is over. " + winner.getName() + " is the winner!");
        gatherColorInformationAfterAbstracted(model, duelistA, duelistB, winner);
        return winner;
    }

    private void gatherColorInformationAfterAbstracted(Model model, GameCharacter duelistA, GameCharacter duelistB, GameCharacter winner) {
        this.knownColors.put(duelistA, MagicDuelEvent.findBestMagicColor(duelistA));
        this.knownColors.put(duelistB, MagicDuelEvent.findBestMagicColor(duelistB));
        if (!knownGauges.containsKey(winner)) {
            identifyGauge(model, winner, MagicDuelEvent.makeRandomGauge());
        }
    }

    protected GameCharacter runDetailedNPCDuel(Model model, GameCharacter duelistA, GameCharacter duelistB) {
        println("Two duelists enter the arena.");
        PowerGauge gaugeA = MagicDuelEvent.makeRandomGauge();
        if (knownGauges.containsKey(duelistA)) {
            gaugeA = knownGauges.get(duelistA);
        }
        PowerGauge gaugeB = MagicDuelEvent.makeRandomGauge();
        if (knownGauges.containsKey(duelistB)) {
            gaugeB = knownGauges.get(duelistB);
        }
        MagicDuelEvent duelEvent = new NPCOnlyMagicDuelEvent(model, duelistA, gaugeA, duelistB, gaugeB);

        duelEvent.doTheEvent(model);
        setCurrentTerrainSubview(model);
        GameCharacter winner = duelEvent.getWinner();
        announcerSay("Ladies and gentlemen. The duel is over. " + winner.getName() + " is the winner!");
        gatherColorAndGaugeInformation(model, duelEvent, duelistA, duelistB, winner);
        return winner;
    }

    private void gatherColorAndGaugeInformation(Model model, MagicDuelEvent duelEvent, GameCharacter duelistA, GameCharacter duelistB, GameCharacter winner) {
        this.knownColors.put(duelistA, duelEvent.getDuelist(0).getMagicColor());
        this.knownColors.put(duelistB, duelEvent.getDuelist(1).getMagicColor());
        PowerGauge gauge;
        if (winner == duelEvent.getDuelist(0).getCharacter()) {
            gauge = duelEvent.getDuelist(0).getGauge();
        } else {
            gauge = duelEvent.getDuelist(1).getGauge();
        }
        if (!knownGauges.containsKey(winner)) {
            identifyGauge(model, winner, gauge);
        }
    }

    private void identifyGauge(Model model, GameCharacter winner, PowerGauge gauge) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult result = gc.testSkillHidden(Skill.SpellCasting, 11, gc.getRankForSkill(Skill.Perception));
            if (result.isSuccessful()) {
                println(gc.getName() + " successfully identifies the Power Gauge being used by " +
                        "the duelists (Spell Casting " + result.asString() + ").");
                knownGauges.put(winner, gauge);
                partyMemberSay(gc, "I'm pretty sure " + winner.getName() + " was using a " + gauge.getName() + "-Gauge.");
                leaderSay("That could be good to know if we go up against " + himOrHer(winner.getGender()) + " later.");
                break;
            }
        }
    }

    private GameCharacter performOneDuel(Model model, GameCharacter duelistA, GameCharacter duelistB) {
        if (model.getParty().getPartyMembers().contains(duelistA)) {
            return runRealDuel(model, duelistA, duelistB);
        }
        if (model.getParty().getPartyMembers().contains(duelistB)) {
            return runRealDuel(model, duelistB, duelistA);
        }
        if (model.getParty().isWipedOut()) {
            return null;
        }

        println("You sit down on one of the benches overlooking the dueling arena.");
        announcerStartOfCombat(duelistA, duelistB);
        print("Do you want to skip the details of the duel? (Y/N) ");
        if (yesNoInput()) {
            return runAbstractedNPCDuel(model, duelistA, duelistB);
        }
        return runDetailedNPCDuel(model, duelistA, duelistB);
    }

    private List<GameCharacter> makeDuelists(Model model, int number) {
        List<GameCharacter> result = new ArrayList<>();
        CharacterClass[] noClasses = Classes.NO_OTHER_CLASSES;
        List<GameCharacter> allDuelists = new ArrayList<>(List.of(
                new GameCharacter("Grundolf", "the Green", Race.NORTHERN_HUMAN, Classes.DRU,
                        PortraitSubView.makeRandomPortrait(Classes.DRU, Race.NORTHERN_HUMAN, false), noClasses),
                new GameCharacter("Galdricia", "Goldleaf", Race.WOOD_ELF, Classes.DRU,
                        PortraitSubView.makeOldPortrait(Classes.DRU, Race.WOOD_ELF, true), noClasses),
                new GameCharacter("Redaka", "the Wicked", Race.DARK_ELF, Classes.WIT,
                        PortraitSubView.makeRandomPortrait(Classes.WIT, Race.DARK_ELF, true), noClasses),
                new GameCharacter("Aegbert", "Inderwald", Race.HALFLING, Classes.WIT,
                        PortraitSubView.makeRandomPortrait(Classes.WIT, Race.HALFLING, false), noClasses),
                new GameCharacter("Godric", "Gorthaur", Race.SOUTHERN_HUMAN, Classes.SOR,
                        PortraitSubView.makeRandomPortrait(Classes.SOR, Race.SOUTHERN_HUMAN, false), noClasses),
                new GameCharacter("Lilith", "Everdark", Race.HIGH_ELF, Classes.SOR,
                        PortraitSubView.makeRandomPortrait(Classes.SOR, Race.HIGH_ELF, true), noClasses),
                new GameCharacter("Arbock", "Dimbelthor", Race.DWARF, Classes.WIZ,
                        PortraitSubView.makeRandomPortrait(Classes.WIZ, Race.DWARF, false), noClasses),
                new GameCharacter("Jonathan", "Helmsplitter", Race.DARK_ELF, Classes.SOR,
                        PortraitSubView.makeRandomPortrait(Classes.SOR, Race.DARK_ELF, false), noClasses),
                new GameCharacter("Ruthenia", "Moon", Race.SOUTHERN_HUMAN, Classes.WIZ,
                        PortraitSubView.makeRandomPortrait(Classes.WIZ, Race.SOUTHERN_HUMAN, false), noClasses),
                new GameCharacter("Jerome", "Granger", Race.SOUTHERN_HUMAN, Classes.WIZ,
                        PortraitSubView.makeRandomPortrait(Classes.WIZ, Race.SOUTHERN_HUMAN, false), noClasses),
                new GameCharacter("Mary-Jean", "Lockwell", Race.HALFLING, Classes.MAG,
                        PortraitSubView.makeRandomPortrait(Classes.MAG, Race.HALFLING, true), noClasses),
                new GameCharacter("Wolunda", "Knull", Race.HALF_ORC, Classes.MAG,
                        PortraitSubView.makeRandomPortrait(Classes.MAR, Race.HALF_ORC, true), noClasses),
                new GameCharacter("Anaethim", "Redstone", Race.DARK_ELF, Classes.PRI,
                        PortraitSubView.makeRandomPortrait(Classes.PRI, Race.DARK_ELF, true), noClasses),
                new GameCharacter("Marduk", "Cabbage", Race.WOOD_ELF, Classes.MAGE,
                        PortraitSubView.makeRandomPortrait(Classes.MAGE, Race.WOOD_ELF, false), noClasses),
                new GameCharacter("Leovald", "Bluesea", Race.HIGH_ELF, Classes.ARCANIST,
                        PortraitSubView.makeRandomPortrait(Classes.ARCANIST, Race.HIGH_ELF, false), noClasses)));


        Collections.shuffle(allDuelists);
        for (int i = 0; i < number; ++i) {
            GameCharacter duelist = allDuelists.get(0);
            allDuelists.remove(0);
            duelist.setLevel((int)Math.ceil(GameState.calculateAverageLevel(model)));
            result.add(duelist);
        }
        return result;
    }
}
