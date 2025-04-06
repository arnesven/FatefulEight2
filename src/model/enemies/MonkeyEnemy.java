package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.RangedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;


public class MonkeyEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new MonkeyEnemySprite();

    public MonkeyEnemy(char a) {
        super(a, "Monkey", NORMAL, new RangedAttackBehavior());
    }


    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    private static class MonkeyEnemySprite extends LoopingSprite {
        public MonkeyEnemySprite() {
            super("peskymonkey", "enemies.png", 0x26, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.GOLD);
            setFrames(4);
        }
    }
}
