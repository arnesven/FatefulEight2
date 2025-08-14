package model.enemies;

import model.GameStatistics;
import model.Model;
import model.achievements.AlucardAchievement;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.combat.loot.BossCombatLoot;
import model.combat.loot.CombatLoot;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VampireEnemy extends UndeadEnemy {
    private static final LoopingSprite SPRITE = new VampireEnemySprite();

    public VampireEnemy(char a) {
        super(a, "Vampire", new VampireAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        GameStatistics.incrementVampiresKilled();
        if (killer.hasCondition(VampirismCondition.class)) {
            combatEvent.completeAchievement(AlucardAchievement.KEY);
        }
    }

    private static class VampireEnemySprite extends LoopingSprite {
        public VampireEnemySprite() {
            super("vampire", "enemies.png", 0x1C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(MyColors.WHITE);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }

}
