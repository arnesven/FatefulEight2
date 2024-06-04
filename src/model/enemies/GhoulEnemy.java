package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.PoisonAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class GhoulEnemy extends UndeadEnemy {
    private static final Sprite SPRITE = new GhoulSprite();

    public GhoulEnemy(char a) {
        super(a, "Ghoul", new PoisonAttackBehavior(2));
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    private static class GhoulSprite extends LoopingSprite {
        public GhoulSprite() {
            super("ghoulenemy", "enemies.png", 0xF0, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.LIGHT_GRAY);
            setColor4(MyColors.GRAY);
            setFrames(4);
        }
    }
}
