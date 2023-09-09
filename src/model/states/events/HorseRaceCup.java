package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.horses.Pony;
import model.items.Equipment;
import model.items.accessories.SuedeBoots;
import model.items.clothing.JustClothes;
import model.items.weapons.ShortSword;
import model.map.CastleLocation;
import model.races.Race;
import model.states.GameState;
import model.states.RecruitState;
import model.states.horserace.HorseRacer;
import util.MyRandom;
import util.MyStrings;
import view.subviews.*;

import java.util.*;

public class HorseRaceCup extends TournamentEvent {
    private final CastleLocation castle;

    public HorseRaceCup(Model model, CastleLocation castle) {
        super(model, castle);
        this.castle = castle;
    }

    @Override
    protected void doEvent(Model model) {
        enterCup(model, false);

        print("The " + castle.getLordTitle() + " is hosting a horse racing cup today. " +
                "Do you wish to attend? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        println("Outside the castle walls many tents and pavilions have been erected. And around them all, " +
                "a track have been prepared for horses to race on.");
        println("As you wander around you see riders, fair ladies, noblemen, merchants and commoners " +
                "all bustling about and getting ready for the cup. Some people are lining up at a little booth " +
                "where a small gentleman in fancy clothing is accepting coins and writing things down in big ledgers.");
        model.getLog().waitForAnimationToFinish();
        showOfficial();
        portraitSay("Yes, we're still accepting participants. Are you here to compete in the cup?");
        if (model.getParty().getHorseHandler().isEmpty()) {
            leaderSay("I don't have a horse.");
            portraitSay("Oh... well, I suppose you can borrow one from the " + castle.getLordTitle() + ".");
            leaderSay("How generous... what else is there to know about this cup?");
        } else {
            leaderSay("Perhaps. What's there to know about the cup?");
        }
        portraitSay("The Grand Horse Racing Cup is truly the event of the year. Seven riders will compete in three " +
                "rounds of racing, with two laps each. The riders score points according to their placement in each race. " +
                "Finishing first gives 10 points, second 8 points, third 6. Then 5, 4 and 3 for fourth fifth and sixth place. " +
                "Whoever has most points at the end of the last race is the winner. The prize pot is 100 gold.");
        leaderSay("What's the entry fee?");
        portraitSay("It's " + ENTRY_FEE + " gold. Are you still interested?");
        boolean sponsored = false;
        if (model.getParty().getGold() < ENTRY_FEE) {
            println("You are about to reply that you can't afford it when a shady fellow in a hood steps up behind you.");
            showSponsor();
            portraitSay("You look like your the capable sort, I can front the money for the cup. " +
                    "But if you win I'll want half the prize money. What do you say?");
            print("Do you accept the strangers sponsorship? (Y/N) ");
            sponsored = true;
        } else {
            print("Will you enter one of your party members as a rider in the cup? (Y/N) ");
        }
        if (yesNoInput()) {
            enterCup(model, sponsored);
        } else {
            leaderSay("For today, I think we'll just enjoy the cup from the sidelines.");
        }
    }

    private void enterCup(Model model, boolean sponsored) {
        leaderSay("Okay, we're in.");
        if (!sponsored) {
            println("You pay the official " + ENTRY_FEE + " gold.");
            model.getParty().addToGold(-ENTRY_FEE);
        } else {
            println("The stranger pays the entry fee to the official.");
        }
        showOfficial();
        println("As it turns out, there's only one more slot open. What name should I put down as the racer?");
        GameCharacter chosenRider = model.getParty().getPartyMember(0);
        if (model.getParty().size() > 1) {
            chosenRider = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        }
        partyMemberSay(chosenRider, "I'll race.");
        portraitSay("Okay then. Just give me a moment while I prepare the score chart.");
        println("The official steps out of the booth and puts up seven notes on a big board.");
        portraitSay("This is the score board. We'll update it after each race. Please have a look at it now.");
        List<GameCharacter> riders = makeRiders();
        Map<GameCharacter, Horse> horses = new HashMap<>();
        if (model.getParty().getHorseHandler().isEmpty()) {
            horses.put(chosenRider, HorseHandler.generateHorse());
        } else {
            horses.put(chosenRider, model.getParty().getHorseHandler().get(0));
        }
        for (GameCharacter gc : riders) {
            horses.put(gc, generateNonPony());
        }
        riders.add(MyRandom.randInt(riders.size()), chosenRider);
        waitForReturnSilently();
        SubView prevSubView = model.getSubView();
        HorseRaceCupSubView horseRaceCupSubView = new HorseRaceCupSubView(riders, horses);
        model.setSubView(horseRaceCupSubView);
        waitForReturnSilently();
        CollapsingTransition.transition(model, prevSubView);
        showOfficial();
        portraitSay("You should hurry over to the track, the first race is about to begin!");
        List<Map<GameCharacter, Integer>> allPoints = new ArrayList<>();
        for (int round = 1; round <= 3; ++round) {
            showAnnouncer();
            if (round == 1) {
                portraitSay("Ladies and gentlemen! Welcome to the event of the year, the grand horse racing cup! " +
                        "We have a spectacular line-up this year. Coming onto the track now is...");
                for (GameCharacter rider : riders) {
                    String classString = "";
                    if (rider.getCharClass() != Classes.None) {
                        classString = " the " + rider.getCharClass().getFullName().toLowerCase();
                    }
                    portraitSay(rider.getName() + classString + " on " + hisOrHer(rider.getGender()) +
                            " " + horses.get(rider).getName() + ".");
                }
            }
            portraitSay("Well, the " + MyStrings.nthWord(round) + " race is about to start... Everybody hold on to your hats!");
            HorseRacingEvent event = new HorseRacingEvent(model, chosenRider, horses.get(chosenRider));
            event.setLaps(3);
            for (GameCharacter gc : riders) {
                if (gc != chosenRider) {
                    event.addNPC(gc, horses.get(gc));
                }
            }
            event.doEvent(model);
            setCurrentTerrainSubview(model);
            showAnnouncer();
            if (round < 3) {
                portraitSay("That's it for the " + MyStrings.nthWord(round) + " round folks! We'll now take a short break " +
                        "before we continue. Please get up and stretch your legs!");
            } else {
                portraitSay("Ladies and gentlemen, the race is over. We have a winner for this year's Grand Horse Race Cup!");
            }
            println("You head over to the score board, which has already been updated.");
            waitForReturnSilently();
            prevSubView = model.getSubView();
            Map<GameCharacter, Integer> points = makePointResult(event.getPlacements());
            allPoints.add(points);
            horseRaceCupSubView.addPoints(points);
            model.setSubView(horseRaceCupSubView);
            waitForReturnSilently();
            CollapsingTransition.transition(model, prevSubView);
            if (round < 3) {
                println("You head back to the racing track.");
            }
        }
        List<GameCharacter> finalPlacements = findFinalPlacements(allPoints);
        System.out.println("Cup results:");
        for (int i = 0; i < 7; ++i) {
            System.out.println(" " + (i+1) + ": " + finalPlacements.get(i).getName());
        }
        portraitSay("In third place, we have " + finalPlacements.get(2).getName() + ".");
        portraitSay("In second place, we have " + finalPlacements.get(1).getName() + ".");
        portraitSay("And finally, our grand champion of the horse race cup, is " + finalPlacements.get(0).getName() +
                "! Congratulations, you can collect your winnings at the booth.");
        if (finalPlacements.get(0) == chosenRider) {
            partyMemberSay(chosenRider, "Yaaaay!");
            println("The official at the booth smiles at you.");
            showOfficial();
            portraitSay("Here's your prize money! Spend it well.");
            model.getParty().addToGold(100);
            println("The party receives 100 gold!");
            if (sponsored) {
                println("You are about to leave the racing grounds when the mysterious sponsor shows up out of nowhere.");
                showSponsor();
                portraitSay("Well done... Now please hand me my share.");
                print("Let the mysterious sponsor have 50 gold? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("Oh okay...");
                    model.getParty().addToGold(-50);
                    portraitSay("Much obliged! Toodeloo!");
                    println("The mysterious sponsor then disappears...");
                    leaderSay("How odd...");
                } else {
                    leaderSay("No, I think we'll hang on to it.");
                    portraitSay("Oh okay. You can just pay the Brotherhood back another time then.");
                    addToEntryFeeToLoan(model);
                    addToEntryFeeToLoan(model);
                    println("The mysterious sponsor then disappears...");
                    leaderSay("Wait, did she just say 'the Brotherhood'?");
                }
                println("Bewildered, you leve the racing grounds and soon forget about the strange encounter.");
            }
        } else if (finalPlacements.get(1) == chosenRider || finalPlacements.get(2) == chosenRider) {
            GameCharacter winner = finalPlacements.get(0);
            println("Disappointed, you start to leave the racing ground when you see " + winner.getName() + " approaching you.");
            showExplicitPortrait(model, winner.getAppearance(), winner.getName());
            portraitSay("That was some great racing friend.");
            leaderSay("It was indeed. How will you spend your winnings?");
            portraitSay("I was thinking I would invest in some proper adventuring equipment.");
            leaderSay("Oh really?");
            portraitSay("Yes. I'm a good racer, but I've been longing to join up with a party and travel the world.");
            winner.setEquipment(new Equipment(new ShortSword(), new JustClothes(), new SuedeBoots()));
            RecruitState recruit = new RecruitState(model, List.of(winner));
            recruit.run(model);
            if (model.getParty().getPartyMembers().contains(winner)) {
                removePortraitSubView(model);
                leaderSay("Welcome to the team " + winner.getFirstName() + ".");
                partyMemberSay(winner, "Thanks!");
                println(winner.getName() + " has contributed an extra 25 gold and a horse, a " + horses.get(winner).getName() +
                        ", to the party.");
                model.getParty().addToGold(25);
                model.getParty().getHorseHandler().addHorse(horses.get(winner));
            } else {
                leaderSay("Sorry " + winner.getFirstName() + ". I don't think it's a good match.");
                portraitSay("How unfortunate. Well, so long I guess.");
                leaderSay("Bye.");
            }
            println("You leave the racing grounds, and return to the castle.");
        } else {
            leaderSay("Well, you can't win every time. Let's focus on something else.");
            println("The party heads back to the castle, trying not to think about the prize money you almost got your hands on.");
        }
    }

    private List<GameCharacter> findFinalPlacements(List<Map<GameCharacter, Integer>> allPoints) {
        Map<GameCharacter, Integer> totalPoints = new HashMap<>();
        for (GameCharacter gc : allPoints.get(0).keySet()) {
            int sum = 0;
            for (Map<GameCharacter, Integer> map : allPoints) {
                sum += map.get(gc);
            }
            totalPoints.put(gc, sum);
        }
        List<GameCharacter> placements = new ArrayList<>(allPoints.get(0).keySet());
        placements.sort((c1, c2) -> totalPoints.get(c2) - totalPoints.get(c1));
        return placements;
    }

    private Horse generateNonPony() {
        Horse horse = null;
        do {
            horse = HorseHandler.generateHorse();
        } while (horse instanceof Pony);
        return horse;
    }

    private Map<GameCharacter, Integer> makePointResult(List<HorseRacer> placements) {
        int[] points = new int[]{10, 8, 6, 5, 4, 3, 0};
        Map<GameCharacter, Integer> result = new HashMap<>();
        for (int i = 0; i < placements.size(); ++i) {
            result.put(placements.get(i).getCharacter(), points[i]);
        }
        return result;
    }

    private List<GameCharacter> makeRiders() {
        List<String> girlFirstNames = new ArrayList<>(COMMON_GIRL_FIRST_NAMES);
        List<String> boyFirstNames = new ArrayList<>(COMMON_BOY_FIRST_NAMES);
        List<String> lastNames = new ArrayList<>(COMMON_LAST_NAMES);
        List<GameCharacter> riders = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            CharacterClass cls = Classes.allClasses[MyRandom.randInt(Classes.allClasses.length)];
            Race race = Race.allRaces[MyRandom.randInt(Race.allRaces.length)];
            boolean gender = MyRandom.flipCoin();
            AdvancedAppearance portrait = PortraitSubView.makeRandomPortrait(cls, race, gender);
            String firstName;
            if (gender) {
                firstName = girlFirstNames.remove(MyRandom.randInt(girlFirstNames.size()));
            } else {
                firstName = boyFirstNames.remove(MyRandom.randInt(boyFirstNames.size()));
            }
            String lastName = lastNames.remove(MyRandom.randInt(lastNames.size()));
            GameCharacter gc = new GameCharacter(firstName, lastName, race, cls, portrait,
                    makeRandomClassSet(cls));
            gc.setLevel((int)Math.ceil(GameState.calculateAverageLevel(getModel())));
            riders.add(gc);
        }
        return riders;
    }

    private CharacterClass[] makeRandomClassSet(CharacterClass cls) {
        return new CharacterClass[]{cls, Classes.allClasses[MyRandom.randInt(Classes.allClasses.length)],
                Classes.allClasses[MyRandom.randInt(Classes.allClasses.length)],
                Classes.allClasses[MyRandom.randInt(Classes.allClasses.length)]};
    }
}
