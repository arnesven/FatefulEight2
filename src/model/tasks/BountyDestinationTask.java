package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.BountyEnemy;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.weapons.Weapon;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.FindBountyEvent;
import util.MyRandom;
import view.combat.CaveTheme;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;
import view.combat.MansionTheme;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BountyDestinationTask extends DestinationTask {
    public static final int SHORT_RANGE = 8;
    public static final int LONG_RANGE = 15;
    public static final int SEEK_INFO_DIFFICULTY = 8;
    private static final int LOOKING_FOR_BOUNTY = 0;
    private static final int GOT_CLUE = 1;
    private static final int BOUNTY_KILLED = 2;
    private static final int TURNED_IN = 3;
    private static final List<CharacterClass> BOUNTY_CLASSES = List.of(
            Classes.BKN, Classes.THF, Classes.SPY,
            Classes.WIT, Classes.ASN, Classes.SOR, Classes.BBN);
    private int state;
    private final Bounty bounty;

    public BountyDestinationTask(Bounty bounty) {
        super(bounty.getPosition(), bounty.getDestinationShortDescription());
        this.state = LOOKING_FOR_BOUNTY;
        this.bounty = bounty;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new BountyJournalEntry();
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null; // This task cannot fail.
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Find Bounty", new FindBountyEvent(model, this));
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return state < BOUNTY_KILLED && model.getParty().getPosition().equals(bounty.getPosition());
    }

    public CombatTheme getCombatTheme() {
        if (bounty.getDestinationShortDescription().contains("cave")) {
            return new CaveTheme();
        }
        if (bounty.getDestinationShortDescription().contains("tower")) {
            return new DungeonTheme();
        }
        return new MansionTheme();
    }

    public List<Enemy> makeEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        CharacterClass cls = MyRandom.sample(BOUNTY_CLASSES);
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(cls);
        enemies.add(new BountyEnemy(app, cls, bounty.getFirstName(), bounty.getLastName(), bounty.getReward() / 20,
                (Weapon)model.getItemDeck().getRandomWeapon().makeHigherTierCopy(1)));
        if (bounty.getWithCompanions()) {
            for (int i = MyRandom.randInt(8); i > 0; --i) {
                cls = MyRandom.sample(BOUNTY_CLASSES);
                app = PortraitSubView.makeRandomPortrait(cls);
                enemies.add(new CompanionEnemy(app, cls, model.getItemDeck().getRandomWeapon()));
            }
        }
        return enemies;
    }

    public String getBountyName() {
        return bounty.getFullName();
    }

    public CharacterAppearance getBountyPortrait() {
        return bounty.getAppearance();
    }

    public void setBountyKilled() {
        state = BOUNTY_KILLED;
    }

    public String getTurnInTown() {
        return bounty.getTurnInTown();
    }

    public void turnInIfAble(Model model, GameState state) {
        if (model.getCurrentHex().getLocation() == null ||
                !(model.getCurrentHex().getLocation() instanceof UrbanLocation) ||
                    this.state != BOUNTY_KILLED) {
            return;
        }
        UrbanLocation urb = (UrbanLocation) model.getCurrentHex().getLocation();
        if (urb.getPlaceName().equals(getTurnInTown())) {
            this.state = TURNED_IN;
            JournalEntry.printJournalUpdateMessage(model);
            state.println("You visit the constabulary and turn in the bounty for " + getBountyName() + "!");
            state.println("The party gains " + bounty.getReward() + " gold.");
            model.getParty().addToGold(bounty.getReward());
            state.leaderSay(MyRandom.sample(List.of("One bad guy less in the world.", "Ka-ching!",
                    "It's nice working with the law.", "Bounty collected. Now, what's next?")));
        }
    }

    @Override
    public boolean isCompleted() {
        return state == TURNED_IN;
    }

    public boolean canGetClue() {
        return this.state == LOOKING_FOR_BOUNTY;
    }

    public void askForClue() {
        this.state = GOT_CLUE;
    }

    public String getClue() {
        return bounty.getClue();
    }

    private class BountyJournalEntry implements JournalEntry {
        @Override
        public String getName() {
            return "Bounty: " + bounty.getFullName();
        }

        @Override
        public String getText() {
            String start =  bounty.getFullName() +
                    " in order to collect a bounty of " + bounty.getReward() +
                    " gold from the constabulary in "  + bounty.getTurnInTown() + ".\n\n";
            if (state == LOOKING_FOR_BOUNTY) {
                return "You are looking for " + start +
                        "Unfortunately you only have a name and a face and have no idea where " +
                        bounty.getFirstName() + " could be hiding.";
            }
            if (state == GOT_CLUE) {
                return "You are hunting down " + start + "You have been told " +
                        bounty.getFirstName() + " is hiding out in " +
                        bounty.getClue() + ".";
            }
            if (state == BOUNTY_KILLED) {
                return "You have killed " + start;
            }
            return "You collected the bounty on " + bounty.getFullName() + ".\n\nCompleted";
        }

        @Override
        public boolean isComplete() {
            return BountyDestinationTask.this.isCompleted();
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
            if (state == GOT_CLUE) {
                return bounty.getPosition();
            }
            if (state == BOUNTY_KILLED) {
                model.getWorld().getPositionForLocation((HexLocation) model.getWorld().getUrbanLocationByPlaceName(getTurnInTown()));
            }
            return null;
        }
    }
}
