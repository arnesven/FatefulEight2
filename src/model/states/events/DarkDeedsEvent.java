package model.states.events;

import model.Model;
import model.actions.Loan;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CowardlyCondition;
import model.combat.RoutedCondition;
import model.enemies.BodyGuardEnemy;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.states.DailyEventState;
import util.MyRandom;
import util.MyStrings;

import java.util.*;

public abstract class DarkDeedsEvent extends DailyEventState {
    public static final int PICK_POCKETING_NOTORIETY = 5;
    public static final int MURDER_NOTORIETY = 50;
    public static final int PICK_POCKETING_BASE_SNEAK_DIFFICULTY = 7;
    public static final int PICK_POCKETING_BASE_SECURITY_DIFFICULTY = 6;
    public static final int ASSAULT_NOTORIETY = 10;
    private final int stealMoney;
    private final String interactText;

    public enum ProvokedStrategy {
        ALWAYS_ESCAPE,
        FIGHT_IF_ADVANTAGE,
        FIGHT_TO_DEATH
    }

    public DarkDeedsEvent(Model model, String interactText, int stealMoney) {
        super(model);
        this.interactText = interactText;
        this.stealMoney = stealMoney;
    }

    @Override
    protected final void doEvent(Model model) {
        if (!doIntroAndContinueWithEvent(model)) {
            return;
        }
        if (darkDeedsMenu(getVictimCharacter(model),
                getVictimCompanions(model), getProvokedStrategy(), true)) {
            return;
        }
        if (!doMainEventAndShowDarkDeeds(model)) {
            return;
        }
        if (darkDeedsMenu(getVictimCharacter(model), getVictimCompanions(model),
                getProvokedStrategy(), false)) {
            return;
        }
        doEnding(model);
    }

    protected abstract boolean doIntroAndContinueWithEvent(Model model);
    protected abstract boolean doMainEventAndShowDarkDeeds(Model model);
    protected void doEnding(Model model) {
        println("You part ways with the " + getVictimCharacter(model).getName().toLowerCase() + ".");
    }

    protected abstract GameCharacter getVictimCharacter(Model model);
    protected abstract List<Enemy> getVictimCompanions(Model model);
    protected abstract ProvokedStrategy getProvokedStrategy();

    private boolean darkDeedsMenu(GameCharacter victimChar, List<Enemy> companions,
                                  ProvokedStrategy strat, boolean withInteract) {
        String victim = getVictimCharacter(getModel()).getName().toLowerCase();
        if (!companions.isEmpty()) {
            print("The " + victim + " is traveling with ");
            println(makeCompanionString(companions) + ".");

        }
        println("How would you like to interact with the " + victim + "?");
        List<String> options = new ArrayList<>(List.of("Attack " + victim));
        if (withInteract) {
            options.add(0, this.interactText + " " + victim);
        } else {
            options.add(0, "Leave " + victim);
        }
        if (getModel().getParty().size() > 1) {
            options.add("Steal from " + victim);
        }
        int chosen = multipleOptionArrowMenu(getModel(), 26, 33, options);
        if (chosen == 0) {
            return false;
        }
        if (chosen == 1) {
            attack(victimChar, companions, strat, true);
        }
        if (chosen == 2) {
            attemptPickPocket(victim, victimChar, stealMoney, companions, strat);
        }
        return true;
    }

    private void attemptPickPocket(String victim, GameCharacter victimChar, int stealMoney,
                                   List<Enemy> companions, ProvokedStrategy strat) {
        print("Who should attempt to pick-pocket the " + victim + "?"); // TODO: Add tutorial about this
        getModel().getTutorial().pickPocketing(getModel());
        GameCharacter thief = getModel().getParty().partyMemberInput(getModel(),
                this, getModel().getParty().getPartyMember(0));
        SkillCheckResult result = thief.testSkill(Skill.Sneak,
                PICK_POCKETING_BASE_SNEAK_DIFFICULTY + victimChar.getRankForSkill(Skill.Perception));
        GameCharacter decoy = getModel().getParty().getRandomPartyMember(thief);
        println("While " + decoy.getFirstName() + " distracts " + victim + ", " + thief.getFirstName() +
                " sneaks around from the back (Sneak " + result.asString() + ").");
        if (result.isSuccessful()) {
            result = thief.testSkill(Skill.Security,
                    PICK_POCKETING_BASE_SECURITY_DIFFICULTY + victimChar.getLevel());
            println(thief.getFirstName() + " attempts to grap the " + victim +
                    "'s purse (Security " + result.asString() + ").");
            if (result.isSuccessful()) {
                println(thief.getFirstName() + " successfully pick-pocketed " + stealMoney + " gold from the " + victim + ".");
                leaderSay("Please excuse us " + victim + ", we have pressing matters to attend.");
                portraitSay(MyRandom.sample(List.of("Oh... alright.", "I see, goodbye.",
                        "Safe travels.", "I understand, farewell")));
                println("You take leave of the " + victim + ".");
                partyMemberSay(decoy, "Nice work " + thief.getName() + ".");
                partyMemberSay(thief, MyRandom.sample(List.of("Child's play", "Easy when you know how.",
                        "Like taking candy from a baby.", heOrSheCap(victimChar.getGender()) + " never suspected a thing.",
                        "What can I say? I'm good.", "Easy money")));
                return;
            }
        }
        addToNotoriety(PICK_POCKETING_NOTORIETY);
        portraitSay(MyRandom.sample(List.of("Hey, there! Keep your fingers to yourself!",
                "Stop! Thief!", "Huh? A pick-pocket? Get away from me!#")));
        if (strat == ProvokedStrategy.ALWAYS_ESCAPE) {
            print("Do you want to attack the " + victim + "? (Y/N) ");
            if (yesNoInput()) {
                attack(victimChar, companions, strat, true);
            } else {
                println("The " + victim + " is enraged and stomps off.");
            }
        } else if (strat == ProvokedStrategy.FIGHT_IF_ADVANTAGE &&
                CowardlyCondition.goodGuysHasTheAdvantage(getModel(), makeEnemyTeam(victimChar, companions),
                        getModel().getParty().getPartyMembers())) {
            portraitSay("I would fight you... but uh... Well, I just don't want to.");
            print("Do you want to attack the " + victim + "? (Y/N) ");
            if (yesNoInput()) {
                attack(victimChar, companions, strat, true);
            } else {
                println("The " + victim + " flees.");
            }
        } else {
            attack(victimChar, companions, strat, false);
        }
    }

    private void addToNotoriety(int notoriety) {
        println("!Your notoriety has increased by " + notoriety + "!");
        if (getModel().getParty().getNotoriety() == 0) {
            getModel().getTutorial().notoriety(getModel());
        }
        getModel().getParty().addToNotoriety(notoriety);
    }


    private void attack(GameCharacter victimChar, List<Enemy> companions,
                        ProvokedStrategy combStrat, boolean isAggressor) {
        if (combStrat == ProvokedStrategy.ALWAYS_ESCAPE) {
            portraitSay(MyRandom.sample(List.of("Help! I'm being attacked!",
                    "What are you doing? Please don't hurt me!",
                    "Help! Bandits are attacking me!",
                    "Stay away!")));
        } else if (combStrat == ProvokedStrategy.FIGHT_IF_ADVANTAGE) {
            portraitSay(MyRandom.sample(List.of("You want to fight? Fine, I'll teach you a lesson!",
                    "Come on then. I'll give you a proper thrashing!")));
        } else {
            portraitSay(MyRandom.sample(List.of("Now you die!",
                    "You messed with the wrong " + victimChar.getRace().getName().toLowerCase() + "!")));
        }
        List<Enemy> enemies = makeEnemyTeam(victimChar, companions);
        for (Enemy e : enemies) {
            if (combStrat == ProvokedStrategy.ALWAYS_ESCAPE) {
                e.addCondition(new RoutedCondition());
            } else if (combStrat == ProvokedStrategy.FIGHT_IF_ADVANTAGE) {
                e.addCondition(new CowardlyCondition(enemies));
            }
        }
        runCombat(enemies);
        int numberOfDead = 0;
        for (Enemy e : enemies) {
            if (e.isDead()) {
                numberOfDead++;
            }
        }
        if (numberOfDead < enemies.size()) {
            println("Some of your enemies have escaped and reported your crime to the local authorities.");
            addToNotoriety(numberOfDead * MURDER_NOTORIETY);
            if (isAggressor) {
                addToNotoriety(enemies.size() - numberOfDead * ASSAULT_NOTORIETY);
            }
        } else if (getModel().getCurrentHex().getLocation() instanceof UrbanLocation) {
            println("Your crime has been witnessed and reported to the local authorities.");
            addToNotoriety(numberOfDead * MURDER_NOTORIETY);
        }
        println("You continue on your journey.");
    }

    private List<Enemy> makeEnemyTeam(GameCharacter victimChar, List<Enemy> companions) {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new FormerPartyMemberEnemy(victimChar));
        enemies.addAll(companions);
        Collections.shuffle(enemies);
        return enemies;
    }

    protected static List<Enemy> makeBodyGuards(int number, char group) {
        List<Enemy> list = new ArrayList<>();
        for (int i = number; i > 0; --i) {
            list.add(new BodyGuardEnemy(group));
        }
        return list;
    }

    private String makeCompanionString(List<Enemy> companions) {
        Map<String, Integer> countMap = new HashMap<>();
        for (Enemy e : companions) {
            if (countMap.containsKey(e.getName())) {
                countMap.put(e.getName(), countMap.get(e.getName()) + 1);
            } else {
                countMap.put(e.getName(), 1);
            }
        }

        StringBuilder bldr = new StringBuilder();
        for (String s : countMap.keySet()) {
            bldr.append(MyStrings.numberWord(countMap.get(s)));
            bldr.append(" ");
            bldr.append(s.toLowerCase());
            if (countMap.get(s) > 1) {
                bldr.append("s");
            }
            bldr.append(", ");
        }

        String result =  bldr.substring(0, bldr.length()-2);
        int index = result.lastIndexOf(", ");
        if (index != -1) {
            result = result.substring(0, index) + " and" + result.substring(index + 1);
        }
        return result;
    }

    public static DailyEventState generateEvent(Model model, WorldHex worldHex) {
        if (model.getParty().getNotoriety() > 0) {
            if (worldHex.getLocation() != null && worldHex.getLocation() instanceof UrbanLocation) {
                if (MyRandom.rollD10() > 5) {
                    return new ConstableEvent(model);
                }
            }
        }
        return null;
    }
}
