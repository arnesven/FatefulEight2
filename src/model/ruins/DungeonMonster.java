package model.ruins;

import model.Model;
import model.enemies.Enemy;
import model.enemies.HermitEnemy;
import model.enemies.SkeletonEnemy;
import model.states.CombatEvent;
import model.states.ExploreRuinsState;
import view.sprites.Sprite;
import view.subviews.DungeonTheme;
import view.subviews.StripedTransition;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class DungeonMonster extends CenterDungeonObject {
    private final List<Enemy> enemies;

    public DungeonMonster(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public static DungeonObject makeRandomEnemies(Random random) {
        return new DungeonMonster(List.of(new SkeletonEnemy('A'), new SkeletonEnemy('A'), new SkeletonEnemy('A')));
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        exploreRuinsState.print(enemies.get(0).getName() + "s attack you! Press enter to continue.");
        exploreRuinsState.waitForReturn();
        CombatEvent combat = new CombatEvent(model, enemies, new DungeonTheme(), true);
        combat.run(model);
        StripedTransition.transition(model, exploreRuinsState.getSubView());
        if (combat.fled()) {
            exploreRuinsState.println("The monsters chase you out of the dungeon!");
            exploreRuinsState.setDungeonExited(true);
        } else {
            exploreRuinsState.getCurrentRoom().removeObject(this);
        }
    }

    @Override
    protected Sprite getSprite() {
        return enemies.get(0).getAvatar();
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(getSprite().getName(), new Point(xPos, yPos), getSprite());
    }

    @Override
    public String getDescription() {
        return enemies.get(0).getName();
    }
}
