package model.states.mine;

import model.Model;
import model.enemies.Enemy;
import model.items.potions.Potion;
import model.items.potions.SleepingPotion;
import model.ruins.objects.DungeonMonster;
import util.MyLists;
import view.ScreenHandler;
import view.sprites.Animation;
import view.sprites.SleepAnimationSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;
import view.subviews.MineSubView;

import java.awt.*;
import java.util.List;

public class EnemyMineObject extends MineObject {
    private static final Sprite SLEEP_ANIMATION = new SleepAnimationSprite();
    private final List<Enemy> enemies;
    private final Sprite avatar;
    private boolean isMoving;
    private boolean isSleeping;
    private SmokeBallAnimation smoke;

    public EnemyMineObject(List<Enemy> enemyGroup, boolean isSleeping) {
        super();
        this.enemies = enemyGroup;
        this.avatar =  enemies.getFirst().getAvatar();
        this.isSleeping = isSleeping;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        if (!isMoving) {
            screenHandler.register(avatar.getName(), screenPosition, avatar);
            if (isSleeping) {
                if (avatar instanceof Animation) {
                    ((Animation) avatar).synch();
                }
                screenHandler.register(SLEEP_ANIMATION.getName(), screenPosition, SLEEP_ANIMATION);
            }
            if (smoke != null && !smoke.isDone()) {
                screenHandler.register(smoke.getName(), screenPosition, smoke);
            }
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public String getDescription() {
        return "a group of " + enemies.getFirst().getName() + "s";
    }

    public void moveToPlayer(MineSubView subView, Point from) {
        this.isMoving = true;
        subView.moveEnemyToPlayer(avatar, from);
        subView.waitForAnimation();
    }

    public boolean doesTriggerCombatFromAdjacent() {
        return !isSleeping;
    }

    public boolean usedSleepingPotion(Model model, AdvancedMineEvent state) {
        Potion pot = MyLists.find(model.getParty().getInventory().getPotions(), p -> p instanceof SleepingPotion);
        if (pot != null) {
            state.print("Do you want to throw your sleeping potion at it? (Y/N) ");
            if (state.yesNoInput()) {
                model.getParty().removeFromInventory(pot);
                isSleeping = true;
                this.smoke = new SmokeBallAnimation();
                return true;
            }
        }
        return false;
    }
}
