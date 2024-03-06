package model.ruins.objects;

import model.Model;
import model.classes.Skill;
import view.subviews.CaveTheme;
import model.enemies.*;
import model.items.potions.Potion;
import model.items.potions.SleepingPotion;
import model.ruins.RuinsDungeon;
import model.states.CombatEvent;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Animation;
import view.sprites.LoopingSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;
import view.subviews.CombatTheme;
import view.subviews.DungeonTheme;
import view.subviews.StripedTransition;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class DungeonMonster extends CenterDungeonObject {
    private static final double SLEEP_CHANCE = 0.6667;
    private static final Sprite SLEEP_ANIMATION = new SleepAnimationSprite();
    private final List<Enemy> enemies;
    private boolean isSleeping = false;
    private SmokeBallAnimation smoke;

    public DungeonMonster(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public static DungeonObject makeRandomEnemies(Random random) {
        DungeonMonster monster = monsterFactory(random);
        if (random.nextDouble() < SLEEP_CHANCE) {
            monster.setSleeping(true);
        }
        return monster;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setSleeping(boolean b) {
        isSleeping = b;
    }

    private static DungeonMonster monsterFactory(Random random) {
        int dieRoll = random.nextInt(13);
        switch (dieRoll) {
            case 0:
                return new DungeonMonster(List.of(new SnowyBeastEnemy('A'), new SnowyBeastEnemy('A')));
            case 1:
                return new DungeonMonster(List.of(new GoblinAxeWielder('A'), new GoblinAxeWielder('A'),
                        new GoblinSpearman('B'), new GoblinSpearman('B'), new GoblinSwordsman('C'),
                        new GoblinSwordsman('C')));
            case 2:
                return new DungeonMonster(List.of(new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'),
                        new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A')));
            case 3:
                return new DungeonMonster(List.of(new TrollEnemy('A'), new OrcWarrior('B'), new OrcWarrior('B')));
            case 4:
                return new DungeonMonster(List.of(new FiendEnemy('A'), new SuccubusEnemy('B'), new SuccubusEnemy('B')));
            case 5:
                return new DungeonMonster(List.of(new ManticoreEnemy('A'), new ManticoreEnemy('A')));
            case 6:
                return new DungeonMonster(List.of(new DaemonEnemy('A'), new ImpEnemy('B'), new ImpEnemy('B')));
            case 7:
                return new DungeonMonster(List.of(new LizardmanEnemy('A'), new LizardmanEnemy('A'), new CrocodileEnemy('B'), new CrocodileEnemy('B')));
            case 8:
                return new DungeonMonster(List.of(new SpiderEnemy('A'), new SpiderEnemy('A'), new SpiderEnemy('A'), new ScorpionEnemy('B')));
            case 9:
                return new DungeonMonster(List.of(new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'),
                        new GiantRatEnemy('B'), new GiantRatEnemy('B'), new GiantRatEnemy('B')));
            case 10:
                return new DungeonMonster(List.of(new AutomatonEnemy('A'), new AutomatonEnemy('A')));
            case 11:
                return new DungeonMonster(List.of(new GhostEnemy('A'), new GhostEnemy('A'), new GhostEnemy('A')));
            default:
                return new DungeonMonster(List.of(new SkeletonEnemy('A'), new SkeletonEnemy('A'), new SkeletonEnemy('A')));
        }
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        boolean ambush = false;
        if (isSleeping) {
            exploreRuinsState.print("The " + enemies.get(0).getName() + " hasn't notice you. Do you want to attempt to sneak past it? (Y/N) ");
            if (exploreRuinsState.yesNoInput()) {
                boolean result = model.getParty().doCollectiveSkillCheck(model, exploreRuinsState, Skill.Sneak, 4);
                if (result) {
                    return;
                }
                exploreRuinsState.println("The " + enemies.get(0).getName() + " has spotted you!");
            } else {
                ambush = true;
            }
            isSleeping = false;
        } else if (canSleep()) {
            useSleepingPotion(model, exploreRuinsState);
        }
        if (isSleeping) {
            return;
        }
        doCombatWithMonster(model, exploreRuinsState, ambush);
    }

    protected boolean canSleep() {
        return true;
    }

    private void doCombatWithMonster(Model model, ExploreRuinsState exploreRuinsState, boolean ambush) {
        exploreRuinsState.print(enemies.get(0).getName() + "s attack you! Press enter to continue.");
        exploreRuinsState.waitForReturn();

        CombatTheme theme = new CaveTheme();
        if (exploreRuinsState.getCurrentLevel() < RuinsDungeon.NUMBER_OF_UPPER_LEVELS) {
            theme = new DungeonTheme();
        }
        CombatEvent combat = new CombatEvent(model, enemies, theme, true, ambush);
        if (getTimeLimit() != -1) {
            combat.setTimeLimit(getTimeLimit());
        }
        combat.run(model);
        StripedTransition.transition(model, exploreRuinsState.getSubView());
        if (combat.fled()) {
            exploreRuinsState.println("The monsters chase you out of the dungeon!");
            exploreRuinsState.setDungeonExited(true);
        } else {
            exploreRuinsState.getCurrentRoom().removeObject(this);
        }
    }

    protected int getTimeLimit() {
        return -1;
    }

    @Override
    protected Sprite getSprite(model.ruins.themes.DungeonTheme theme) {
        return enemies.get(0).getAvatar();
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, model.ruins.themes.DungeonTheme theme) {
        model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
        if (isSleeping) {
            if (getSprite(theme) instanceof Animation) {
                ((Animation) getSprite(theme)).synch();
            }
            model.getScreenHandler().register(SLEEP_ANIMATION.getName(), new Point(xPos, yPos), SLEEP_ANIMATION);
        }
        if (smoke != null && !smoke.isDone()) {
            model.getScreenHandler().register(smoke.getName(), new Point(xPos, yPos), smoke);
        }
    }

    @Override
    public String getDescription() {
        return enemies.get(0).getName();
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        boolean ambush = false;
        if (isSleeping) {
            state.print("The " + enemies.get(0).getName() + " hasn't noticed you. Do you want to provoke it? (Y/N) ");
            if (state.yesNoInput()) {
                isSleeping = false;
                ambush = true;
            }
        }

        if (!isSleeping) {
            doCombatWithMonster(model, state, ambush);
        }
    }

    private void useSleepingPotion(Model model, ExploreRuinsState state) {
        Potion pot = null;
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (p instanceof SleepingPotion) {
                pot = p;
            }
        }
        if (pot != null) {
            state.print("Do you want to throw your sleeping potion to distract the " + enemies.get(0).getName() + "? (Y/N) ");
            if (state.yesNoInput()) {
                model.getParty().getInventory().remove(pot);
                state.println("The " + enemies.get(0).getName() + " has fallen asleep!");
                isSleeping = true;
                this.smoke = new SmokeBallAnimation();
            }
        }
    }

    public String getName() {
        return enemies.get(0).getName();
    }

    private static class SleepAnimationSprite extends LoopingSprite {
        public SleepAnimationSprite() {
            super("sleepanimation", "dungeon.png", 0x46, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.PINK);
            setFrames(2);
            setDelay(32);
        }
    }
}
