package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.KnockDownAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class WerebearEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new WerebearSprite();

    public WerebearEnemy(char a) {
        super(a, "Werebear", RAMPAGING, new KnockDownAttackBehavior(3));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    private static class WerebearSprite extends LoopingSprite {
        public WerebearSprite() {
            super("werebear", "enemies.png", 0x130, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.PINK);
            setColor4(MyColors.TAN);
        }
    }
}
