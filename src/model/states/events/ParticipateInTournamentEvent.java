package model.states.events;

import model.Model;
import model.actions.Loan;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.TournamentEnemy;
import model.map.CastleLocation;
import util.MyLists;
import util.MyRandom;
import view.subviews.TournamentSubView;

import java.util.ArrayList;
import java.util.List;

public class ParticipateInTournamentEvent extends TournamentEvent {
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
        waitForReturnSilently();
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
                handleSponsorWhenLost(model);
                return;
            }
            lookAtBoard(model, current, winners);
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
                handleSponsorWhenLost(model);
                return;
            }
            lookAtBoard(model, current, current);
        }

        doLongBreak(model, winners, losers, fighters, chosen);
        announcerSay("We are about ready to start the semi-finals! Please take your seats.");
        GameCharacter winner = performOneFight(model, fighters.get(0), fighters.get(1));
        model.getParty().unbenchAll();
        announcerSay("That means we have a winner ladies and gentlemen... It's " + winner.getName() + "!");
        if (winner == chosen) {
            println(chosen.getName() + " accepts the prize money of 100 gold!");
            model.getParty().addToGold(100);
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
            if (sponsored) {
                handleSponsorWhenLost(model);
            }
        }
    }

    private void handleSponsorWhenLost(Model model) {
        println("You turn away from the tournament grounds and start back toward the castle when the mysterious stranger " +
                "suddenly pops up as out of nowhere.");
        showSponsor();
        portraitSay("Rotten luck friend. I really thought you were going to win. " +
                "The way I see it, you owe me " + ENTRY_FEE + " gold.");
        if (model.getParty().getGold() >= ENTRY_FEE) {
            print("Let the mystery man have " + ENTRY_FEE + " gold? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-ENTRY_FEE);
                leaderSay("Fine... Now this day can't possibly get any worse.");
                portraitSay("Much appreciated friend. Well good-bye and good luck next time I suppose.");
            } else {
                refuseToPayBack(model);
            }
        } else {
            leaderSay("I'm afraid I just don't have it.");
            portraitSay("That's okay friend, you can pay the Brotherhood back later.");
            addToEntryFeeToLoan(model);
            println("The mysterious stranger just smiles, then he disappears.");
            leaderSay("Wait... what did we just agree to?");
        }
    }

    private void handleSponsorWhenWon(Model model) {
        println("You are about to start celebrating when the mysterious stranger pops out like out of nowhere.");
        showSponsor();
        portraitSay("Congratulations. I knew you had it in you. Now please, pay me my share.");
        print("Do you hand over 50 gold to the stranger? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().addToGold(-50);
            portraitSay("Thank you. The Brotherhood likes it when people pay their dues.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            println("The mystery man then promptly disappears.");
        } else {
            refuseToPayBack(model);
        }
    }

    private void refuseToPayBack(Model model) {
        leaderSay("I think we'll hang on to that gold for now. Perhaps we can pay you back some other time?");
        portraitSay("Certainly. The Brotherhood regularly lends money to those who are in need. " +
                "You can find us at taverns in towns and castles.");
        super.addToEntryFeeToLoan(model);
        super.addToEntryFeeToLoan(model);
        println("The mysterious stranger just smiles, then he disappears.");
        leaderSay("Wait... what did we just agree to?");
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

    private void lookAtBoard(Model model, List<GameCharacter> current, List<GameCharacter> knownFighters) {
        println("You get up from your seats and walk over to the board next to the booth where you signed up " +
                "for the tournament. It has already been updated.");
        model.getLog().waitForAnimationToFinish();
        TournamentSubView tournamentSubView = new TournamentSubView(current);
        tournamentSubView.setFightersAsKnown(knownFighters);
        model.setSubView(tournamentSubView);
        waitForReturnSilently();
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
        MyLists.forEach(winners, (GameCharacter gc) -> gc.addToHP(MyRandom.randInt(1, 5)));
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


}
