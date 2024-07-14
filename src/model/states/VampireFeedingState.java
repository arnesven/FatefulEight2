package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.events.GeneralInteractionEvent;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class VampireFeedingState extends GameState {
    private static final int NOTORIETY_FOR_BEING_SPOTTED = 25;
    private final GameCharacter vampire;
    private static final int NO_OF_ATTEMPTS = 3;

    public VampireFeedingState(Model model, GameCharacter vampire) {
        super(model);
        this.vampire = vampire;
    }

    @Override
    public GameState run(Model model) {
        List<GameCharacter> others = new ArrayList<>(model.getParty().getPartyMembers());
        others.remove(vampire);
        model.getParty().benchPartyMembers(others);
        println(vampire.getFirstName() + " sneaks out at night to find a victim to feed on.");
        for (int i = 0; i < NO_OF_ATTEMPTS; ++i) {
            VampireFeedingHouse house = new VampireFeedingHouse();
            println(heOrSheCap(vampire.getGender()) + " approaches a house, it's a " +
                    MyStrings.numberWord(house.getStories()) + " story building.");
            println(vampire.getFirstName() + " stakes out the house for a few minutes...");
            SkillCheckResult result = vampire.testSkill(model, Skill.Perception);
            println("Perception " + result.asString() + ".");
            if (result.getModifiedRoll() < 5) {
                println("But " + heOrShe(vampire.getGender()) + " discerns nothing.");
            } else {
                String dwellers = "are " + MyStrings.numberWord(house.getDwellers()) + " people";
                if (house.getDwellers() == 1) {
                    dwellers = "is only one person";
                }
                print("And " + hisOrHer(vampire.getGender()) + " vampiric senses tell " +
                        himOrHer(vampire.getGender()) + " that there " + dwellers + " living there");
                if (result.getModifiedRoll() >= 9) {
                    if (house.getDwellers() == 1) {
                        if (house.getSleeping() == 1) {
                            println(", and he or she is asleep.");
                        } else {
                            println(", and he or she is awake.");
                        }
                    } else {
                        println(", " + MyStrings.numberWord(house.getSleeping()) + " of them are asleep.");
                    }
                } else {
                    println(".");
                }
            }
            print("Do you want to try to enter this house? (Y/N) ");
            if (yesNoInput() && enterHouseNightIsOver(model, house)) {
                break;
            } else {
                if (i < NO_OF_ATTEMPTS - 1) {
                    println(vampire.getFirstName() + " leaves the house. But the night is still young.");
                } else {
                    println(vampire.getFirstName() + " leaves the house.");
                }
            }
        }
        println("Dawn is rapidly approaching and " + vampire.getFullName() +
                " must return to the party before anybody notices " + heOrShe(vampire.getGender()) + " is missing.");
        print("Press enter to continue.");
        waitForReturn();
        model.getParty().unbenchAll();
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private boolean enterHouseNightIsOver(Model model, VampireFeedingHouse house) {
        if (!doorStepWasUnlocked(model, house)) {
            return false;
        }

        if (sneakStepWasSpotted(model, house)) {
            return true;
        }

        return feedingStep(model, house);
    }

    private boolean feedingStep(Model model, VampireFeedingHouse house) {
        int peopleAwake = house.getDwellers() - house.getSleeping();
        if (peopleAwake == 0) {
            println("There is nobody sleeping in this house."); // TODO: Add mesmerize ability
            println(vampire.getFirstName() + " quietly leaves the house.");
            return false;
        }
        AdvancedAppearance victim = PortraitSubView.makeRandomPortrait(Classes.None);
        for (int i = 0; i < peopleAwake; ++i) {
            println(vampire.getFirstName() + " approaches the bed of a " + victim.getRace().getName() + ".");
            print("Do you wish to feed on this victim? (Y/N) ");
            if (yesNoInput()) {
                println(vampire.getFirstName() + " descends upon the " + victim.getRace().getName() +
                        " and sinks " + hisOrHer(vampire.getGender()) + " teeth into " + hisOrHer(victim.getGender()) + ".");
                println("The " + victim.getRace().getName() + " gasps and for a moment it seems " + heOrShe(victim.getGender()) +
                        " is about to wake up, but then it appears the dark aura of the vampire lulls " + himOrHer(victim.getGender()) +
                        " back into a lethargic state. At last, " + vampire.getFirstName() + " can drink " +
                        hisOrHer(vampire.getGender()) + " fill.");
                vampire.addToSP(9999);
                println(vampire.getFullName() + " Stamina has fully recovered.");
                return true;
            } else {
                println(vampire.getFirstName() + " steps away from the bed.");
            }
        }
        println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
        return false;
    }

    private boolean doorStepWasUnlocked(Model model, VampireFeedingHouse house) {
        println("The house is locked."); // TODO: If multi-story house, choice of acrobatics.
        boolean success = model.getParty().doSoloLockpickCheck(model, this, house.getLockDifficulty());
        if (!success) {
            println("The door to the house remains firmly locked.");
            return false;
        }
        println(vampire.getFirstName() + " quietly enters the house through the front door.");
        return true;
    }

    private boolean sneakStepWasSpotted(Model model, VampireFeedingHouse house) {
        int peopleAwake = house.getDwellers() - house.getSleeping();
        if (peopleAwake > 0) {
            println(vampire.getFirstName() + " must sneak past the inhabitants who are still awake.");
            SkillCheckResult result = vampire.testSkill(model, Skill.Sneak, 5 + peopleAwake*2);
            if (result.isFailure()) {
                println("You have been spotted!");
                printQuote(manOrWomanCap(MyRandom.flipCoin()), "HEY! Get out of here you creep!");
                GeneralInteractionEvent.addToNotoriety(model, this, NOTORIETY_FOR_BEING_SPOTTED);
                println(vampire.getFirstName() + " flees the house with haste before the constables arrive.");
                return true;
            }
            printQuote(manOrWomanCap(MyRandom.flipCoin()),
                    MyRandom.sample(List.of("Did I hear something? It's probably just the wind.",
                            "What was that? Hmm... naw, it was nothing.",
                            "Huh, someone there? No... just my mind playing tricks on me.")));
        }
        return false;
    }

}
