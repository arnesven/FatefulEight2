package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.TournamentEnemy;
import model.map.CastleLocation;
import util.MyRandom;
import view.subviews.TournamentSubView;

import java.util.ArrayList;
import java.util.List;

public class ParticipateInTournamentEvent extends TournamentEvent {
    private static final List<String> ADJECTIVES =  List.of("strong", "mysterious", "rugged",
                                                            "tough", "hardened", "capable", "powerful", "skilled",
                                                            "vicious", "fierce");
    private final boolean sponsored;
    private boolean saidCheating = false;

    public ParticipateInTournamentEvent(Model model, boolean sponsored, CastleLocation castleLocation) {
        super(model, castleLocation);
        this.sponsored = sponsored;
    }

    @Override
    protected void doEvent(Model model) {
        if (!sponsored) {
            println("The party pays " + ENTRY_FEE + " gold to the official.");
            model.getParty().addToGold(-ENTRY_FEE);
        }
        print("Which party member do you wish to enter into the tournament?");
        GameCharacter chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        partyMemberSay(chosen, "I'll enter the tournament.");
        super.showOfficial();
        portraitSay("There needs to be a name on the entry. Do you wish to give your name, or just an alias?");
        if (chosen.getCharClass() == Classes.BKN) {
            partyMemberSay(chosen, "Just put down 'the Black Knight'.");
            if (chosen != model.getParty().getLeader()) {
                leaderSay("Oh come on " + chosen.getFirstName() + ", seriously?");
                partyMemberSay(chosen, "Okay then, I guess there's no reason to hide my identity.");
            } else {
                partyMemberSay(chosen, "No wait... I changed my mind. There's no reason to hide my identity.");
            }
        }
        partyMemberSay(chosen, "You can put down my name, it's " + chosen.getName());
        portraitSay("Okay then. That means we have all eight combatants! Let me just randomize the setup.");
        println("The official places eight slips of paper into his hat, and then pulls them out one by one. For each one " +
                "he writes the name on a large sign next to the booth.");
        portraitSay("This is the match tree. It shows who will fight whom and what the outcome has been as the fights conclude, " +
                "Please have a look at it now.");
        waitForReturn();
        List<GameCharacter> fighters = makeFighters(model, 7);
        fighters.add(MyRandom.randInt(fighters.size()), chosen);
        TournamentSubView tournamentSubView = new TournamentSubView(fighters);
        model.setSubView(tournamentSubView);
        waitForReturn();
        setCurrentTerrainSubview(model);

        List<GameCharacter> winners = new ArrayList<>();
        List<GameCharacter> losers = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            GameCharacter fighterB = fighters.remove(fighters.size() - 1);
            GameCharacter fighterA = fighters.remove(fighters.size() - 1);
            if (i == 0) {
                showOfficial();
                portraitSay("It looks like the first fight is going to be " + fighterA.getName() + " versus " +
                        fighterB.getName() + ", how exciting! You should hurry to the fighting pit!");
                println("You make your way over to the fighting pit. Up on a podium, a man is speaking behind a large cone which " +
                        "is amplifying his voice enough for everyone to hear.");
            } else {
                if (i == 1) {
                    println("The official is busy taking people's bets.");
                    leaderSay("Excuse me, can I place a bet?");
                    super.showOfficial();
                    portraitSay("I'm sorry no. Since you are a close companion of one of the fighters you may not place any bets in this tournament.");
                    println("Before you have time to object the official quickly speaks up again.");
                    portraitSay("It's our policy. Sorry again.");
                }
                println("You hear the announcer start to call out that the fight is about to start, so you walk back over to the " +
                        "fighting pits and take your seats again.");
            }
            GameCharacter winner = performOneFight(model, fighterA, fighterB);
            model.getParty().unbenchAll();
            winners.add(0, winner);
            losers.add(fighterB == winner ? fighterA : fighterB);
            List<GameCharacter> current = new ArrayList<>(fighters);
            current.addAll(winners);
            if (i == 0) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match. " +
                        "Why not place some bets while you are at it?");
            } else if (i < 3) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match.");
            } else {
                announcerSay("That concludes the quarter matches. We will now have a longer break before we proceed " +
                        "to the semi-finals.");
            }
            if (checkForOutOfTournament(current, chosen)) {
                return;
            }
            lookAtBoard(model, current);
        }

        doLongBreak(model, winners, losers, fighters, chosen);
        announcerSay("We are about ready to start the semi-finals! Please take your seats.");

        winners.clear();
        losers.clear();
        for (int i = 0; i < 2; ++i) {
            GameCharacter fighterB = fighters.remove(fighters.size() - 1);
            GameCharacter fighterA = fighters.remove(fighters.size() - 1);
            if (i == 1) {
                println("You hear the announcer start to call out that the fight is about to start, so you walk back over to the " +
                        "fighting pits and take your seats again.");
            }
            GameCharacter winner = performOneFight(model, fighterA, fighterB);
            model.getParty().unbenchAll();
            winners.add(0, winner);
            losers.add(fighterB == winner ? fighterA : fighterB);
            List<GameCharacter> current = new ArrayList<>(fighters);
            current.addAll(winners);
            if (i == 0) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match.");
            } else {
                announcerSay("That concludes the semi-finals. We will now have a longer break before we proceed " +
                        "to the final fight between " + winners.get(0).getName() + " and " + winners.get(1).getName() + "!");
            }
            if (checkForOutOfTournament(current, chosen)) {
                return;
            }
            lookAtBoard(model, current);
        }

        doLongBreak(model, winners, losers, fighters, chosen);
        announcerSay("We are about ready to start the semi-finals! Please take your seats.");
        GameCharacter winner = performOneFight(model, fighters.get(0), fighters.get(1));
        model.getParty().unbenchAll();
        announcerSay("That means we have a winner ladies and gentlemen... It's " + winner.getName() + "!");
        if (winner == chosen) {
            println(chosen.getName() + " accepts the prize money of 100 gold!");
            model.getParty().addToGold(100);
        } else {
            leaderSay("Well, we lost.");
            if (chosen.isDead()) {
                leaderSay("And we lost " + chosen.getFirstName() + "... It hasn't been a good day.");
            } else {
                leaderSay("At least we didn't lose " + chosen.getFirstName() +
                        "... Let's focus on the tasks before us instead.");
            }
        }

    }

    private void doLongBreak(Model model, List<GameCharacter> winners, List<GameCharacter> losers,
                             List<GameCharacter> fighters, GameCharacter chosen) {
        println("Taking advantage of the longer break, you have a stroll around to look at some of the side attractions.");
        new FoodStandsEvent(model).doEvent(model);
        setCurrentTerrainSubview(model);
        fighters.addAll(winners);
        winners.remove(chosen);
        handleQuarterWinners(model, winners);
        handleQuarterLosers(model, losers);
        println("You're surprised at how quickly time has passed when you again hear the voice of the announcer.");
    }

    private void lookAtBoard(Model model, List<GameCharacter> current) {
        println("You get up from your seats and walk over to the board next to the booth where you signed up " +
                "for the tournament. It has already been updated.");
        model.getLog().waitForAnimationToFinish();
        TournamentSubView tournamentSubView = new TournamentSubView(current);
        model.setSubView(tournamentSubView);
        waitForReturn();
        setCurrentTerrainSubview(model);
    }

    private boolean checkForOutOfTournament(List<GameCharacter> current, GameCharacter chosen) {
        if (!current.contains(chosen)) {
            leaderSay("Well, that's it, we're out of the tournament.");
            println("Disappointed in your misfortune, you leave the tournament and try to set your mind to your adventuring instead.");
            return true;
        }
        return false;
    }

    private void handleQuarterWinners(Model model, List<GameCharacter> winners) {
        for (GameCharacter gc : winners) {
            gc.addToHP(MyRandom.randInt(1, 5));
        }
        GameCharacter gc = MyRandom.sample(winners);
        println("You spot one of the fighters from the tournament, " + gc.getName() + ". " + heOrSheCap(gc.getGender()) +
                " is consuming a health potion.");
        if (model.getParty().size() > 1 && !saidCheating) {
            this.saidCheating = true;
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()), "Hey! Isn't that cheating?");
            leaderSay("I don't really see why it would be. We could do the same thing...");
        }
    }


    private void handleQuarterLosers(Model model, List<GameCharacter> losers) {
        losers.removeIf(GameCharacter::isDead);
        if (losers.isEmpty()) {
            println("You spot some men carrying away the bodies of the fallen fighters.");
            leaderSay("Pretty gruesome business this...");
        } else {
            print("You spot one of the fighters from the tournament, ");
            GameCharacter loser = MyRandom.sample(losers);
            println(loser.getName() + ". " + heOrSheCap(loser.getGender()) +
                    " is being bandage by a medic and is sitting with " + hisOrHer(loser.getGender()) + " head hanging.");
            if (loser.getCharClass() == Classes.BKN) {
                showExplicitPortrait(model, loser.getAppearance(), "Black Knight");
                portraitSay("I am defeated.");
                println("The black knight looks up at your party.");
                portraitSay("But you fought well!");
                println("The black knight offers to instruct you in his martial ways, ");
                ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.BKN);
                changeClassEvent.areYouInterested(model);
            } else {
                leaderSay("Poor sod. But we can't all be winners...");
            }
        }
        setCurrentTerrainSubview(model);
    }

    private GameCharacter performOneFight(Model model, GameCharacter fighterA, GameCharacter fighterB) {
        if (model.getParty().getPartyMembers().contains(fighterA)) {
            return runRealCombat(model, fighterA, fighterB);
        }
        if (model.getParty().getPartyMembers().contains(fighterB)) {
            return runRealCombat(model, fighterB, fighterA);
        }

        println("You sit down on one of the benches overlooking the fighting pit.");
        announcerStartOfCombat(fighterA, fighterB);
        print("Do you want to skip the details of the fight? (Y/N) ");
        if (yesNoInput()) {
            runAbstractedNPCFight(model, fighterA, fighterB);
            return announceOutcomeOfCombat(fighterA, fighterB, fighterA.getHP() == 1);
        }
        runDetailedNPCFight(model, fighterA, fighterB);
        return announceOutcomeOfCombat(fighterA, fighterB, fighterA.getHP() == 1);
    }

    private GameCharacter runRealCombat(Model model, GameCharacter partyMember, GameCharacter npcFighter) {
        println(partyMember.getName() + " enters the fighting pit...");
        announcerStartOfCombat(partyMember, npcFighter);
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != partyMember) {
                model.getParty().benchPartyMembers(List.of(gc));
            }
        }
        GameCharacter oldLeader = model.getParty().getLeader();
        model.getParty().setLeader(partyMember);
        List<Enemy> enemies = List.of(new TournamentEnemy(npcFighter));
        runCombat(enemies);
        if (!oldLeader.isDead()) {
            model.getParty().setLeader(oldLeader);
        }
        npcFighter.addToHP(-(enemies.get(0).getMaxHP() - enemies.get(0).getHP()));
        setCurrentTerrainSubview(model);
        return announceOutcomeOfCombat(partyMember, npcFighter, haveFledCombat());
    }

    private void runAbstractedNPCFight(Model model, GameCharacter fighterA, GameCharacter fighterB) {
        println("The two combatants fight well, but in the end, one of them comes out on top.");
        if (MyRandom.flipCoin()) { // TODO : Base the probability on the odds given for the fighters
            fighterA.addToHP(-fighterA.getMaxHP());
            if (MyRandom.flipCoin()) {
                fighterA.addToHP(1);
            }
        } else {
            fighterB.addToHP(-fighterB.getMaxHP());
            if (MyRandom.flipCoin()) {
                fighterB.addToHP(1);
            }
        }
    }

    private GameCharacter announceOutcomeOfCombat(GameCharacter fighterA, GameCharacter fighterB, boolean aYield) {
        announcerSay("Well ladies and gentlemen, it's over.");
        if (fighterA.isDead()) {
            announcerSay(fighterA.getName() + " has been slain, but " + heOrShe(fighterA.getGender()) +
                    " has perished with honor!");
            announcerSay(fighterB.getName() + " is the victor of the fight!");
            return fighterB;
        }
        if (fighterB.isDead()) {
            announcerSay(fighterB.getName() + " has been slain, but " + heOrShe(fighterB.getGender()) +
                    " has perished with honor!");
            announcerSay(fighterA.getName() + " is the victor of the fight!");
            return fighterA;
        }
        if (aYield) {
            announcerSay(fighterA.getName() + " has yielded and must withdraw from the tournament to tend to " +
                    hisOrHer(fighterA.getGender()) + " wounds!");
            announcerSay(fighterB.getName() + " is the victor of the fight!");
            return fighterB;
        }
        announcerSay(fighterB.getName() + " has yielded and must withdraw from the tournament to tend to " +
                hisOrHer(fighterB.getGender()) + " wounds!");
        announcerSay(fighterA.getName() + " is the victor of the fight!");
        return fighterA;
    }

    private void announcerStartOfCombat(GameCharacter fighterA, GameCharacter fighterB) {
        announcerSay("And now, ladies and gentlemen, we are about to see a " +
                MyRandom.sample(List.of("fierce", "exciting", "hectic")) + " " +
                MyRandom.sample(List.of("fight", "face off", "combat", "bout", "match")) + " between two skilled opponents!");
        announcerSay("In one corner, we have a" + present(fighterA) + ". Let's have a big round of applause for... " +
                fighterA.getName() + "!");
        announcerSay("And in the other corner, we have a" + present(fighterB) + ". Let's have another big round of applause for... " +
                fighterB.getName() + "!");
        getModel().getLog().waitForAnimationToFinish();
    }

    private String present(GameCharacter fighter) {
        if (fighter.getCharClass() == Classes.None) {
            return "... uh, well a fellow..";
        }
        return " " + MyRandom.sample(ADJECTIVES) + " " + fighter.getCharClass().getFullName().toLowerCase();

    }

    private void announcerSay(String s) {
        super.showAnnouncer();
        portraitSay(s);
    }
}
