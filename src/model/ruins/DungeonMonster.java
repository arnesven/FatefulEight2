package model.ruins;

import model.Model;
import model.enemies.*;
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
        int dieRoll = random.nextInt(11);
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
                return new DungeonMonster(List.of(new DaemonEnemy('A'), new ImpEnemy('A'), new ImpEnemy('A')));
            case 7:
                return new DungeonMonster(List.of(new LizardmanEnemy('A'), new LizardmanEnemy('A'), new CrocodileEnemy('B'), new CrocodileEnemy('B')));
            case 8:
                return new DungeonMonster(List.of(new SpiderEnemy('A'), new SpiderEnemy('A'), new SpiderEnemy('A'), new ScorpionEnemy('B')));
            case 9:
                return new DungeonMonster(List.of(new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'), new RatEnemy('A'),
                        new GiantRatEnemy('B'), new GiantRatEnemy('B'), new GiantRatEnemy('B')));
            default:
                return new DungeonMonster(List.of(new SkeletonEnemy('A'), new SkeletonEnemy('A'), new SkeletonEnemy('A')));
        }
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
