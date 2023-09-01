package model.states.events;

import model.items.ItemDeck;
import model.Model;
import model.characters.GameCharacter;
import model.characters.GoblinCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.*;
import model.map.CastleLocation;
import model.races.Race;
import model.states.ArcheryState;
import model.states.RecruitState;
import model.states.ShootBallsState;
import model.states.ShopState;
import util.MyRandom;
import util.MyStrings;
import view.sprites.Sprite;
import view.subviews.*;

import java.util.*;

import static model.classes.Classes.None;

public class ArcheryContestEvent extends TournamentEvent {
    private final CastleLocation castle;
    private List<Weapon> bows;
    private Map<GameCharacter, Sprite> fletchings;

    public ArcheryContestEvent(Model model, CastleLocation castleLocation) {
        super(model, castleLocation);
        this.castle = castleLocation;
        bows = new ArrayList<>(ItemDeck.allBows());
        bows.add(new CompetitionBow());
        bows.add(new CompetitionBow());
    }

    @Override
    protected void doEvent(Model model) {
        print("The " + castle.getLordTitle() + " is hosting a archery competition today. " +
                "Do you wish to attend? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        println("Outside the castle walls many tents and pavilions have been erected. And there, " +
                "on a long stretch of lawn, several archery targets have been placed.");
        println("As you wander around you see marksmen, fair ladies, noblemen, merchants and commoners " +
                "all bustling about and getting ready for the contest. Some people are lining up at a little booth " +
                "where a small gentleman in fancy clothing is accepting coins and writing things down in big ledgers.");
        model.getLog().waitForAnimationToFinish();
        showOfficial();
        portraitSay("Yes, we're still accepting participants. Are you here to compete in the archery contest?");
        leaderSay("Perhaps. What are the parameters?");
        portraitSay("The entry fee is " + ENTRY_FEE + " gold. Twenty marksmen will enter the competition, which has three rounds. " +
                "In the first round each contestant fires a single arrow at the target. Those who hit the target qualify. " +
                "In the second round each contestant must shoot at balls which have been tossed into the air. The contestants who " +
                "can hit the most balls tossed at the same time will qualify to the third round. In the third round, each contestant is " +
                "given three arrows and score points for how well they hit the target. The contestant who scores the most points in the final " +
                "round wins a marvelous prize - the Golden Bow. Oh and you'll also receive the " + castle.getLordTitle() +
                " blessing - which is probably equally valuable.");
        showOfficial();
        portraitSay("But all of you can't enter the contest, in fact we only have room for one more. Are you still interested?");
        boolean sponsored = false;
        if (model.getParty().getGold() < ENTRY_FEE) {
            println("You are about to reply that you can't afford it when a shady fellow in a hood steps up behind you.");
            showSponsor();
            portraitSay("You look like your the capable sort, I can front the money for the competition. " +
                    "But if you win I'll want the bow you used during the contest. What do you say?");
            print("Do you accept the strangers sponsorship? (Y/N) ");
            sponsored = true;
        } else {
            print("Will you enter one of your party members into the contest? (Y/N) ");
        }
        if (yesNoInput()) {
            enterTournament(model, sponsored);
        } else {
            leaderSay("I think we'll just watch this time.");
            println("You spend the day watching the contest, which is both exciting and interesting. " +
                    "Afterwards you feel inspired to become a better bowman.");
        }
    }

    private void enterTournament(Model model, boolean sponsored) {
        print("Which party member should enter the contest?");
        GameCharacter chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        partyMemberSay(chosen, "I'll enter the contest.");
        if (!sponsored) {
            println("You pay the official " + ENTRY_FEE + " gold.");
            model.getParty().addToGold(-ENTRY_FEE);
        } else {
            println("The stranger pays the entry fee to the official.");
        }
        showOfficial();
        portraitSay("Thank you. Naturally you will need a bow. You are allowed to use your own, but we do " +
                "sell excellent bows made especially for competitions.");
        if (chosen.getEquipment().getWeapon() instanceof CrossbowWeapon) {
            partyMemberSay(chosen, "Can I use a crossbow?");
            portraitSay("It is allowed, but I wouldn't recommend it. Crossbows are easy enough to aim with, but it is " +
                    "not very easy to adjust the power of the shot.");
            partyMemberSay(chosen, "I understand.");
        }
        print("Do you wish to purchase a bow from the official? (Y/N) ");
        if (yesNoInput()) {
            buyBow(model);
        } else {
            partyMemberSay(chosen, "I already have a bow.");
        }

        stillNeedABow(model, chosen);

        do {
            println("Please ensure " + chosen.getFirstName() + " is equipped with the bow " +
                    "you would like to use in the tournament. Then press enter to continue.");
            waitForReturnSilently();
        } while (!bowEquipped(chosen));

        BowWeapon bowToUse = (BowWeapon)chosen.getEquipment().getWeapon();
        println(chosen.getName() + " will use a " + bowToUse.getName().toLowerCase() + " in the contest.");

        List<GameCharacter> contestants = makeMarksmen(model);
        contestants.add(chosen);
        this.fletchings = makeFletchings(contestants);
        Collections.shuffle(contestants);
        Map<GameCharacter, Integer> points = playRoundOne(model, chosen, bowToUse, contestants);
        if (didWinInRound1(points, chosen)) {
            winContest(model, chosen, sponsored, bowToUse);
            return;
        }
        if (points.get(chosen) == 0) { // Lost in round 1
            loseContest();
            return;
        }
        Map<GameCharacter, Integer> points2 = playRoundTwo(model, chosen, bowToUse, contestants, points);
        int maxBalls = findMaxPoints(points2);
        if (didWinInRound2(points2, maxBalls, chosen)) {
            winContest(model, chosen, sponsored, bowToUse);
            return;
        }
        System.out.println("Max balls is " + maxBalls);
        if (points2.get(chosen) < maxBalls) { // Lost in round 2
            loseContest();
            return;
        }
        Map<GameCharacter, Integer> points3 = playRoundThree(model, chosen, bowToUse, contestants,
                                                             points, points2, maxBalls);
        ArcheryContestBoardSubView board = new ArcheryContestBoardSubView(contestants, fletchings);
        board.addPoints(points);
        board.addPoints(points2);
        board.addPoints(points3);
        model.setSubView(board);
        waitForReturnSilently();
        int maxPoints = findMaxPoints(points3);
        if (points3.get(chosen) == maxPoints) {
            winContest(model, chosen, sponsored, bowToUse);
        } else {
            loseContest();
        }
    }

    private void loseContest() {
        if (MyRandom.flipCoin()) {
            leaderSay("Well, that's it. We didn't qualify for the second round. Better luck next time I suppose.");
            println("The party leaves the competition grounds and tries to put their minds on their future adventures.");
        } else {
            leaderSay("We gave it our best shot, literally. But we didn't qualify. " +
                    "We should focus on the tasks before us instead of this little side show.");
            println("The party leaves the competition grounds.");
        }
    }

    private void stillNeedABow(Model model, GameCharacter chosen) {
        for (GameCharacter chara : model.getParty().getPartyMembers()) {
            if (chara.getEquipment().getWeapon() instanceof BowWeapon) {
                return;
            }
        }

        for (Item it : model.getParty().getInventory().getAllItems()) {
            if (it instanceof BowWeapon) {
                return;
            }
        }

        partyMemberSay(chosen, "Wait... I actually don't have a bow.");
        portraitSay("Well... I guess you can use this bow.");
        model.getParty().getInventory().add(new TrainingBow());
        println("The party receives " + (new TrainingBow()).getName() + ".");
    }

    private Map<GameCharacter, Integer> playRoundOne(Model model, GameCharacter chosen,
                                                     BowWeapon bowToUse, List<GameCharacter> contestants) {
        portraitSay("To my left is a board with the names of all the contestants. Please have a look at it now.");
        waitForReturnSilently();
        SubView prevSubView = model.getSubView();
        model.setSubView(new ArcheryContestBoardSubView(contestants, fletchings));
        waitForReturnSilently();
        model.setSubView(prevSubView);

        setCurrentTerrainSubview(model);
        showAnnouncer();
        portraitSay("Ladies and gentlemen! Please take your seats, for it is time to witness the first round of " +
                "this archery competition. Each contestant will shoot a single arrow at the target. Only those who hit " +
                "the target will qualify to the next round.");
        ArcheryState state = new ArcheryState(model, chosen, bowToUse, ArcheryState.MEDIUM_DISTANCE);
        List<GameCharacter> others = new ArrayList<>(contestants);
        others.remove(chosen);
        state.addNPCShooters(others);
        state.useFletchings(fletchings);
        state.run(model);
        print("Press enter to continue.");
        waitForReturn();
        setCurrentTerrainSubview(model);
        showAnnouncer();
        portraitSay("The first round is over. Any contestant who managed to hit the target will qualify for the next round. " +
                "We will now have a short break before continuing the competition.");
        return state.getPoints();
    }

    private boolean didWinInRound1(Map<GameCharacter, Integer> points, GameCharacter chosen) {
        for (GameCharacter gc : points.keySet()) {
            if (points.get(gc) > 0 && gc != chosen) {
                return false;
            }
        }
        return points.get(chosen) > 0;
    }

    private Map<GameCharacter, Integer> playRoundTwo(Model model, GameCharacter chosen, BowWeapon bowToUse,
                              List<GameCharacter> contestants, Map<GameCharacter, Integer> points) {
        println("You walk over to the booth with the board. It has already been updated with the scores from the first round.");
        ArcheryContestBoardSubView board = new ArcheryContestBoardSubView(contestants, fletchings);
        board.addPoints(points);
        model.setSubView(board);
        waitForReturnSilently();
        setCurrentTerrainSubview(model);
        announcerSay("And now ladies and gentlemen, round two of this contest is about to begin! In this round each contestant " +
                "will have to prove their accuracy as well as their quickness. Three balls will be thrown into the air and the " +
                "contestant must try to hit them before they hit the ground.");
        Map<GameCharacter, Integer> newPoints = new HashMap<>();
        for (GameCharacter chara : contestants) {
            if (points.get(chara) == 0) {
                continue;
            }
            if (chara == chosen) {
                println("It is now " + chosen.getName() + "'s turn.");
                model.getLog().waitForAnimationToFinish();
                ShootBallsState state = new ShootBallsState(model, chosen, bowToUse);
                state.useFletching(fletchings.get(chosen));
                state.run(model);
                newPoints.put(chosen, state.getPoints());
                setCurrentTerrainSubview(model);
            } else {
                BowWeapon bow = (BowWeapon) chara.getEquipment().getWeapon();
                print(chara.getName() + " gets ready with " + hisOrHer(chara.getGender()) +" " + bow.getName() +
                        ". Three balls are tossed into the air... ");
                int roll = MyRandom.rollD10();
                int balls = 0;
                if (roll == 10 && bow.getReloadSpeed() < 4) {
                    balls = 3;
                } else if (roll > 7 && bow.getReloadSpeed() < 4) {
                    balls = 2;
                } else if (roll > 4) {
                    balls = 1;
                }
                if (balls == 0) {
                    println(heOrSheCap(chara.getGender()) + " doesn't hit a single ball.");
                    println(chara.getName() + ": \"" + MyRandom.sample(List.of("Darn!", "That's disappointing.", "Phooey!",
                            "Rats.", "Oh come on!", "I'm out.", "Too tricky.", "Hey! It's harder than it looks.", "I just can't do it.")));
                } else if (balls > 1) {
                    println(heOrSheCap(chara.getGender()) + " hits " + MyStrings.numberWord(balls) + " balls!");
                    println(chara.getName() + ": \"" + MyRandom.sample(List.of("Sweet!", "Yes!", "Alright!", "Did it!", "Gotcha!")));
                } else {
                    println(heOrSheCap(chara.getGender()) + " hits one ball.");
                }
                newPoints.put(chara, balls);
            }
        }
        portraitSay("The second round is over. We will now have a short break before continuing the competition.");
        return newPoints;
    }

    private int findMaxPoints(Map<GameCharacter, Integer> points) {
        int max = 0;
        for (GameCharacter gc : points.keySet()) {
            if (points.get(gc) > max) {
                max = points.get(gc);
            }
        }
        return max;
    }

    private boolean didWinInRound2(Map<GameCharacter, Integer> points2, int maxBalls, GameCharacter chosen) {
        for (GameCharacter gc : points2.keySet()) {
            if (points2.get(gc) == maxBalls && gc != chosen) {
                return false;
            }
        }
        return points2.get(chosen) == maxBalls;
    }

    private Map<GameCharacter, Integer> playRoundThree(Model model, GameCharacter chosen, BowWeapon bowToUse,
                                                       List<GameCharacter> contestants,
                                                        Map<GameCharacter, Integer> points,
                                                       Map<GameCharacter, Integer> points2, int maxBalls) {
        println("You walk over to the booth with the board. It has already been updated with the scores from the second round.");
        ArcheryContestBoardSubView board = new ArcheryContestBoardSubView(contestants, fletchings);
        board.addPoints(points);
        board.addPoints(points2);
        model.setSubView(board);
        waitForReturnSilently();
        setCurrentTerrainSubview(model);
        showAnnouncer();
        List<GameCharacter> finalists = new ArrayList<>();
        for (GameCharacter gc : points2.keySet()) {
            if (points2.get(gc) == maxBalls) {
                finalists.add(gc);
            }
        }
        portraitSay("Ladies and gentlemen, we made it to the final round. It looks like " +
                MyStrings.numberWord(finalists.size()) + " contestants have qualified. In this round, each contestant is given " +
                "three arrows, with which they must score as many points as possible. Oh, and we've also moved the targets further away! " +
                "Good luck!");
        ArcheryState state = new ArcheryState(model, chosen, bowToUse, ArcheryState.VERY_FAR_DISTANCE);
        List<GameCharacter> others = new ArrayList<>(finalists);
        others.remove(chosen);
        state.addNPCShooters(others);
        state.setShots(3);
        state.useFletchings(fletchings);
        state.run(model);
        print("Press enter to continue.");
        waitForReturn();
        setCurrentTerrainSubview(model);
        portraitSay("The contest is over! Ladies and gentlemen let's look at the score board!");
        return state.getPoints();
    }


    private void winContest(Model model, GameCharacter chosen, boolean sponsored, BowWeapon bow) {
        portraitSay("We have a winner of the contest, it's " + chosen.getName() + "! You can claim " +
                "your prize, the golden bow, at the booth.");
        println("The party congratulates " + chosen.getFirstName() + " and head over to the booth.");
        showOfficial();
        portraitSay("Congratulations. Here's your prize.");
        Weapon prize = new GoldenBow();
        model.getParty().getInventory().add(prize);
        println("The party receives " + prize.getName() + ".");
        if (sponsored) {
            println("As out of nowhere, the mysterious sponsor shows up behind the party.");
            showSponsor();
            portraitSay("Masterfully done. I hope you haven't forgotten about or deal?");
            println("Hand over the " + bow.getName() + " to the stranger? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Fine. We've got a new one anyway.");
                remove(model, bow);
                println("You have lost the " + bow.getName() + ".");
                portraitSay("Much obliged. I think you have some fans who will pay handsomely for " +
                        "the bow which won you the contest. Bye bye now...");
                leaderSay("What an odd fellow.");
            } else {
                leaderSay("You know what? I think we'll hang on to it for now.");
                portraitSay("Oh, that's alright. You can just pay the brotherhood back later. Bye bye now.");
                addToEntryFeeToLoan(model);
                println("The mysterious stranger disappears as quickly as he appeared.");
                leaderSay("Did he say, 'the brotherhood'?");
            }
            println("Although puzzled by the interaction with the stranger, the party returns to the castle to celebrate.");
        } else {
            println("Filled with bravado and glee, the party returns to the castle for a night of whimsy and celebration.");
        }
        partyMemberSay(model.getParty().getRandomPartyMember(), "Huzzah!");
    }

    private void remove(Model model, BowWeapon bow) {
        if (model.getParty().getInventory().getAllItems().contains(bow)) {
            model.getParty().getInventory().remove(bow);
            return;
        }
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getEquipment().getWeapon() == bow) {
                gc.unequipWeapon();
                model.getParty().getInventory().remove(bow);
                return;
            }
        }
    }

    private List<GameCharacter> makeMarksmen(Model model) {
        List<GameCharacter> result = new ArrayList<>();
        CharacterClass[] noClasses = new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None};
        result.add(new GameCharacter("Ruben", "Hodd", Race.NORTHERN_HUMAN, Classes.MAR,
                PortraitSubView.makeRandomPortrait(Classes.MAR, Race.NORTHERN_HUMAN, false), noClasses, randomBow()));
        result.add(new GameCharacter("Ivar", "Longfellow", Race.NORTHERN_HUMAN, Classes.MAR,
                PortraitSubView.makeRandomPortrait(Classes.MAR, Race.NORTHERN_HUMAN, false), noClasses, randomBow()));
        result.add(new GameCharacter("Fanny", "Ovuldsgain", Race.DWARF, Classes.NOB,
                PortraitSubView.makeRandomPortrait(Classes.NOB, Race.DWARF, true), noClasses, randomBow()));
        result.add(new GameCharacter("Tessa", "Crowvald", Race.HALF_ORC, Classes.AMZ,
                PortraitSubView.makeRandomPortrait(Classes.AMZ, Race.HALF_ORC, true), noClasses, randomBow()));
        result.add(new GameCharacter("Masked Ranger", "", Race.HALFLING, Classes.ASN,
                PortraitSubView.makeRandomPortrait(Classes.ASN, Race.HALFLING, MyRandom.flipCoin()), noClasses, randomBow()));
        result.add(new GameCharacter("Vanessa", "Firsov", Race.SOUTHERN_HUMAN, Classes.CAP,
                PortraitSubView.makeRandomPortrait(Classes.CAP, Race.SOUTHERN_HUMAN, true), noClasses, randomBow()));
        result.add(new GameCharacter("Goblin", "", Race.GOBLIN, Classes.GOBLIN,
                new GoblinCharacter(), new CharacterClass[]{Classes.GOBLIN, None, None, None}, randomBow()));
        result.add(new GameCharacter("Locky", "Roseville", Race.WOOD_ELF, Classes.FOR,
                PortraitSubView.makeRandomPortrait(Classes.FOR, Race.WOOD_ELF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Endath", "Virmansee", Race.DARK_ELF, Classes.MAR,
                PortraitSubView.makeRandomPortrait(Classes.MAR, Race.DARK_ELF, true), noClasses, randomBow()));
        result.add(new GameCharacter("Filavandrel", "Oakborn", Race.HIGH_ELF, Classes.NOB,
                PortraitSubView.makeRandomPortrait(Classes.NOB, Race.HIGH_ELF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Marcus", "Felbottom", Race.DWARF, Classes.AMZ,
                PortraitSubView.makeRandomPortrait(Classes.AMZ, Race.DWARF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Woods Girl", "", Race.WOOD_ELF, None,
                PortraitSubView.makeRandomPortrait(None, Race.WOOD_ELF, true), noClasses, randomBow()));
        result.add(new GameCharacter("Anthony", "McCormic", Race.HALFLING, Classes.CAP,
                PortraitSubView.makeRandomPortrait(Classes.CAP, Race.HALFLING, false), noClasses, randomBow()));
        result.add(new GameCharacter("Shagra", "Vull", Race.HALF_ORC, Classes.BBN,
                PortraitSubView.makeRandomPortrait(Classes.BBN, Race.HALF_ORC, false), noClasses, randomBow()));
        result.add(new GameCharacter("Jeremy", "Inklepod", Race.SOUTHERN_HUMAN, Classes.DRU,
                PortraitSubView.makeRandomPortrait(Classes.DRU, Race.SOUTHERN_HUMAN, false), noClasses, randomBow()));
        result.add(new GameCharacter("The Fool", "", Race.NORTHERN_HUMAN, None,
                PortraitSubView.makeRandomPortrait(None, Race.NORTHERN_HUMAN, MyRandom.flipCoin()), noClasses, new Equipment(new ShortBow())));
        result.add(new GameCharacter("Jeim", "Ecthelion", Race.WOOD_ELF, Classes.NOB,
                PortraitSubView.makeRandomPortrait(Classes.NOB, Race.WOOD_ELF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Rutte", "Kolantis", Race.DWARF, Classes.BRD,
                PortraitSubView.makeRandomPortrait(Classes.BRD, Race.DWARF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Esmeralda", "Vix", Race.DARK_ELF, Classes.THF,
                PortraitSubView.makeRandomPortrait(Classes.THF, Race.DARK_ELF, true), noClasses, randomBow()));
        int lvl = (int)Math.round(RecruitState.calculateAverageLevel(model));
        for (GameCharacter chara : result) {
            chara.setLevel(lvl);
        }
        return result;
    }

    private Equipment randomBow() {
        return new Equipment(MyRandom.sample(bows));
    }

    private boolean bowEquipped(GameCharacter chosen) {
        return chosen.getEquipment().getWeapon() instanceof BowWeapon;
    }

    private void buyBow(Model model) {
        List<Item> items = new ArrayList<>();
        items.add(new CompetitionBow());
        ShopState merchantShop = new ShopState(model, "Contest Official", items, new int[]{14});
        merchantShop.setSellingEnabled(false);
        merchantShop.run(model);
        setCurrentTerrainSubview(model);
    }

    private Map<GameCharacter, Sprite> makeFletchings(List<GameCharacter> contestants) {
        Map<GameCharacter, Sprite> fletchings = new HashMap<>();
        for (GameCharacter gc : contestants) {
            fletchings.put(gc, AimingSubView.makeArrowSprite());
        }
        return fletchings;
    }
}
