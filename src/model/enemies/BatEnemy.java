package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.BleedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class BatEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new BatSprite();

    public BatEnemy(char enemyGroup) {
        super(enemyGroup, "Bat", NORMAL, new BleedAttackBehavior(1));
    }

    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 3;
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

    private static class BatSprite extends LoopingSprite {
        public BatSprite() {
            super("batenemy", "enemies.png", 0x7A, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.LIGHT_GRAY);
            setColor3(MyColors.DARK_GRAY);
            setFrames(3);
        }
    }
}
