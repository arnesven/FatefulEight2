package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.OrcWarrior;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class OrcsEvent extends DailyEventState {
    private static final String ATTITUDE_KEY = "ORCS_ATTITUDE";

    public OrcsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters a group of orcs.");
        if (interactWithOrcs(model)) {
            println("The group of orcs let you continue on your journey.");
        } else {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < MyRandom.randInt(3, 6); ++i) {
                enemies.add(new OrcWarrior('A'));
            }
            runCombat(enemies);
        }
    }

    public boolean interactWithOrcs(Model model) {
        int noOfHalfOrcs = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getRace().id() == Race.HALF_ORC.id()) {
                noOfHalfOrcs++;
            }
        }
        if (noOfHalfOrcs > 0 || getAttitude(model) >= 0) {
            if (noOfHalfOrcs == model.getParty().size()) {
                print("The orcs seem to ignore you. Do you wish to attack them? (Y/N) ");
                if (yesNoInput()) {
                    println("You attack the orcs!");
                    decreaseAttitude(model);
                    return false;
                }
                return true;
            }
            int attitude = getAttitude(model);
            println("The orcs seem aggressive but can perhaps be persuaded to not attack you.");
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 11 - noOfHalfOrcs - attitude);
            if (result) {
                print("The orcs have calmed down. Do you wish to attack them? (Y/N) ");
                if (yesNoInput()) {
                    println("You attack the orcs!");
                    decreaseAttitude(model);
                    return false;
                }
            }
        }
        decreaseAttitude(model);
        println("The orcs attack you!");
        return false;
    }

    public static String getOrcsFactionString(Model model) {
        if (!model.getSettings().getMiscCounters().containsKey(ATTITUDE_KEY)) {
            return "";
        }
        int attitude = model.getSettings().getMiscCounters().get(ATTITUDE_KEY);
        if (attitude < -10) {
            return "Sworn enemy";
        }
        if (attitude < -5) {
            return "Enemy";
        }
        if (attitude < 0) {
            return "Unliked";
        }
        if (attitude == 0) {
            return "Neutral";
        }
        if (attitude > 5) {
            return "Good Friend";
        }
        return "Friend";
    }

    public static int getAttitude(Model model) {
        int attitude = 0;
        if (model.getSettings().getMiscCounters().containsKey(ATTITUDE_KEY)) {
            attitude = model.getSettings().getMiscCounters().get(ATTITUDE_KEY);
        }
        return attitude;
    }

    public static void decreaseAttitude(Model model) {
        model.getSettings().getMiscCounters().put(ATTITUDE_KEY, getAttitude(model) - 1);
    }


    public static void increaseAttitude(Model model) {
        model.getSettings().getMiscCounters().put(ATTITUDE_KEY, getAttitude(model) + 1);
    }

}
