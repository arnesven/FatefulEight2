package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.enemies.behaviors.MixedAttackBehavior;
import model.states.CombatEvent;
import model.states.events.TentacleEnemy;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class OctopusEnemy extends BeastEnemy {

    private static final Sprite SPRITE = new OctopusSprite();

    public OctopusEnemy(char enemyGroup) {
        super(enemyGroup, "Octopus", RAMPAGING, new MixedAttackBehavior());
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        List<Enemy> others = new ArrayList<>(combatEvent.getEnemies());
        for (Enemy e : others) {
            combatEvent.doDamageToEnemy(e, 99, killer);
        }
    }

    @Override
    public int getMaxHP() {
        return 26;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    protected int getHeight() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    public static List<Enemy> makeEnemyList() {
        return List.of(new TentacleEnemy('A'),
                new TentacleEnemy('A'), new OctopusEnemy('B'),
                new TentacleEnemy('A'), new TentacleEnemy('A'));
    }

    private static class OctopusSprite extends LoopingSprite {
        public OctopusSprite() {
            super("dragon", "enemies.png", 0x50, 64, 64, new ArrayList<>());
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BLUE);
            setColor3(MyColors.YELLOW);
            setColor4(MyColors.DARK_BLUE);
            setFrames(3);
        }
    }
}
