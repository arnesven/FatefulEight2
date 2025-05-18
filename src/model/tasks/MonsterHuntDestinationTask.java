package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.enemies.Enemy;
import model.journal.JournalEntry;
import model.map.TownLocation;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import view.combat.CaveTheme;
import view.combat.DungeonTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MonsterHuntDestinationTask extends DestinationTask {
    public static final int TRACK_DIFFICULTY = 7;
    private final MonsterHunt monsterHunt;
    private boolean completed = false;

    public MonsterHuntDestinationTask(MonsterHunt monsterHunt) {
        super(monsterHunt.getPosition(), monsterHunt.getShortDescription());
        this.monsterHunt = monsterHunt;
    }

    public static boolean hasTaskAtCurrentLocation(Model model) {
        return MyLists.any(model.getParty().getDestinationTasks(),
                dt -> dt instanceof MonsterHuntDestinationTask &&
                        ((MonsterHuntDestinationTask)dt).canBeTurnedInHere(model));
    }

    public boolean canBeTurnedInHere(Model model) {
        return !isCompleted() && getMonster().isDead() &&
                model.getCurrentHex().getLocation() instanceof TownLocation &&
                ((TownLocation) model.getCurrentHex().getLocation()).getPlaceName().equals(monsterHunt.getTurnInTownName());
    }

    @Override
    public Point getPosition() {
        if (monsterHunt.getMonster().isDead()) {
            return null;
        }
        return super.getPosition();
    }

    public boolean isReadyForTurnIn() {
        return monsterHunt.getMonster().isDead();
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new JournalEntry() {
            @Override
            public String getName() {
                return "Monster Hunt: " + monsterHunt.getMonster().getName();
            }

            @Override
            public String getText() {
                if (isComplete()) {
                    return "You dealt with the monster terrorizing " + monsterHunt.getTurnInTownName() + ".\n\nCompleted";
                }
                if (isReadyForTurnIn()) {
                    TownLocation town = (TownLocation) model.getWorld().getUrbanLocationByPlaceName(monsterHunt.getTurnInTownName());
                    return "You have slain the " + monsterHunt.getMonster().getName() +
                            ". See the " + town.getLordTitle() + " in " + town.getPlaceName() + " for your reward of " + monsterHunt.getReward() + " gold.";
                }
                return "The people of " + monsterHunt.getTurnInTownName() + " are being terrorized by a savage monster.\n\n" +
                        "If you slay the " + monsterHunt.getMonster().getName() + " who dwells in " +
                        monsterHunt.getDestination() + " you can collect a reward of " + monsterHunt.getReward() + " gold.";
            }

            @Override
            public boolean isComplete() {
                return MonsterHuntDestinationTask.this.isCompleted();
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                if (monsterHunt.getMonster().isDead()) {
                    TownLocation town = (TownLocation) model.getWorld().getUrbanLocationByPlaceName(monsterHunt.getTurnInTownName());
                    return model.getWorld().getPositionForLocation(town);
                }
                return monsterHunt.getPosition();
            }
        };
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null; // Can't fail
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Track Monster", new TrackMonsterEvent(model));
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return !isCompleted() && model.partyIsInOverworldPosition(monsterHunt.getPosition()) &&
                !monsterHunt.getMonster().isDead();
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public Enemy getMonster() {
        return monsterHunt.getMonster();
    }

    public int getReward() {
        return monsterHunt.getReward();
    }

    public void setCompleted(boolean b) {
        completed = b;
    }

    private class TrackMonsterEvent extends DailyEventState {
        public TrackMonsterEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            if (!monsterHunt.isTracked()) {
                println("You attempt to track the monster mentioned on the note from " + monsterHunt.getTurnInTownName() + ".");
                boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Survival, TRACK_DIFFICULTY);
                if (!success) {
                    println("You spend hours searching for the " + monsterHunt.getMonster().getName() +
                            ", but are unable to locate its lair.");
                    leaderSay(iOrWeCap() + "'ll just have to try again tomorrow I guess.");
                    randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(), "What a waste of time.");
                    return;
                }
            }
            monsterHunt.setTracked(true);
            println("You find the lair of the " + monsterHunt.getMonster().getName() + ". It attacks you!");
            if (monsterHunt.getShortDescription().contains("cave")) {
                runCombat(List.of(monsterHunt.getMonster()), new CaveTheme(), true);
            } else if (monsterHunt.getShortDescription().contains("crypt")) {
                runCombat(List.of(monsterHunt.getMonster()), new DungeonTheme(), true);
            } else {
                runCombat(List.of(monsterHunt.getMonster()));
            }
            if (!model.getParty().isWipedOut()) {
                if (monsterHunt.getMonster().isDead()) {
                    leaderSay("That's done. Now " + iOrWe() + " just have to head back to " + monsterHunt.getTurnInTownName() + " " +
                            "for " + myOrOur() + " reward.");
                } else {
                    leaderSay("That " + monsterHunt.getMonster().getName() + " was tougher than I thought! " + iOrWeCap() +
                            "'ll come back later to deal with it once and for all.");
                }
            }
        }
    }
}
