package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.ParalysisAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class MummyEnemy extends UndeadEnemy {
    private static final Sprite SPRTE = new MummySprite();

    public MummyEnemy(char a) {
        super(a, "Mummy", new ParalysisAttackBehavior(2));
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    protected Sprite getSprite() {
        return SPRTE;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    private static class MummySprite extends LoopingSprite {
        public MummySprite() {
            super("mummyenemy", "enemies.png", 0xF4, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.GRAY);
            setColor4(MyColors.GOLD);
            setFrames(4);
        }
    }
}
