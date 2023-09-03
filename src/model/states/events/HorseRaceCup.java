package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.horses.Pony;
import model.map.CastleLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import model.states.horserace.HorseRacer;
import util.MyRandom;
import util.MyStrings;
import view.subviews.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            horseRaceCupSubView.addPoints(makePointResult(event.getPlacements()));
            model.setSubView(horseRaceCupSubView);
            waitForReturnSilently();
            CollapsingTransition.transition(model, prevSubView);
            if (round < 3) {
                println("You head back to the racing track.");
            }
        }
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
        List<String> girlFirstNames = new ArrayList<>(List.of("Bella", "Steffi", "Ronya", "Felixa",
                "Ipona", "Esmeralda", "Gemma", "Petra", "Sinorin", "Adalia", "Cormona"));
        List<String> boyFirstNames = new ArrayList<>(List.of("Golbert", "Voldo", "Maxim", "Nestor",
                "Karg", "Tobert", "Roger", "Sammy", "Oleg", "Trevor", "Quellic", "Ben"));
        List<String> lastNames = new ArrayList<>(List.of("Wildfeather", "Cleareyes", "Al-Zaman",
                "Gerson", "Essex", "Overhill", "Sloch", "Petty", "Inderfelt", "Sharptooth", "Zeltic"));
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
            gc.setLevel((int)Math.ceil(RecruitState.calculateAverageLevel(getModel())));
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
