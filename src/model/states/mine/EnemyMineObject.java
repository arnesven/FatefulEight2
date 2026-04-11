package model.states.mine;

import model.enemies.Enemy;
import model.ruins.objects.DungeonMonster;
import view.ScreenHandler;
import view.sprites.Animation;
import view.sprites.SleepAnimationSprite;
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
}
