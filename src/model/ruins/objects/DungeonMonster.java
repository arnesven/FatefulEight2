package model.ruins.objects;

import model.Model;
import model.classes.Skill;
import model.combat.CombatAdvantage;
import model.enemies.*;
import model.items.potions.Potion;
import model.items.potions.SleepingPotion;
import model.states.CombatEvent;
import model.states.ExploreRuinsState;
import model.states.events.RareBirdEvent;
import view.MyColors;
import view.sprites.Animation;
import view.sprites.LoopingSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;
import view.subviews.StripedTransition;

import java.awt.*;
import java.util.List;

public class DungeonMonster extends CenterDungeonObject {
    private static final Sprite SLEEP_ANIMATION = new SleepAnimationSprite();
    private final List<Enemy> enemies;
    private boolean isSleeping = false;
    private SmokeBallAnimation smoke;

    public DungeonMonster(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setSleeping(boolean b) {
        isSleeping = b;
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        boolean surprise = false;
        if (isSleeping) {
            exploreRuinsState.print("The " + enemies.get(0).getName() + " hasn't notice you. Do you want to attempt to sneak past it? (Y/N) ");
            if (exploreRuinsState.yesNoInput()) {
                int difficulty= 4;
                if (RareBirdEvent.checkForSquawk(model, exploreRuinsState)) {
                    difficulty = 10;
                }
                boolean result = model.getParty().doCollectiveSkillCheck(model, exploreRuinsState, Skill.Sneak, difficulty);
                if (result) {
                    return;
                }
                exploreRuinsState.println("The " + enemies.get(0).getName() + " has spotted you!");
            } else {
                surprise = true;
            }
            isSleeping = false;
        } else if (canSleep()) {
            useSleepingPotion(model, exploreRuinsState);
        }
        if (isSleeping) {
            return;
        }
        doCombatWithMonster(model, exploreRuinsState, surprise);
    }

    protected boolean canSleep() {
        return true;
    }

    private void doCombatWithMonster(Model model, ExploreRuinsState exploreRuinsState, boolean surprise) {
        exploreRuinsState.print(enemies.get(0).getName() + "s attack you! Press enter to continue.");
        exploreRuinsState.waitForReturn();

        CombatEvent combat = new CombatEvent(model, enemies, exploreRuinsState.getCombatTheme(),
                true, surprise ? CombatAdvantage.Party : CombatAdvantage.Neither);
        if (getTimeLimit() != -1) {
            combat.setTimeLimit(getTimeLimit());
        }
        combat.run(model);
        StripedTransition.transition(model, exploreRuinsState.getSubView());
        if (combat.fled()) {
            exploreRuinsState.println("The monsters chase you out of the dungeon!");
            exploreRuinsState.setDungeonExited(true);
            enemies.removeIf(Enemy::isDead);
        } else {
            exploreRuinsState.getCurrentRoom().removeObject(this);
            exploreRuinsState.addDefeatedMonster(this);
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
        boolean surprise = false;
        if (isSleeping) {
            state.print("The " + enemies.get(0).getName() + " hasn't noticed you. Do you want to provoke it? (Y/N) ");
            if (state.yesNoInput()) {
                isSleeping = false;
                surprise = true;
            }
        }

        if (!isSleeping) {
            doCombatWithMonster(model, state, surprise);
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
