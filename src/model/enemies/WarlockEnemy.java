package model.enemies;

import model.Model;
import model.combat.BossCombatLoot;
import model.combat.CombatLoot;
import model.enemies.behaviors.MagicMeleeAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class WarlockEnemy extends Enemy {
    private static final Sprite SPRITE = new WarlockEnemySprite();

    public WarlockEnemy(char a) {
        super(a, "Warlock");
        setAttackBehavior(new MagicMeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    public int getSpeed() {
        return 6;
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
        return new BossCombatLoot(model);
    }

    private static class WarlockEnemySprite extends LoopingSprite {
        public WarlockEnemySprite() {
            super("warlock", "enemies.png", 0x94, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(Race.NORTHERN_HUMAN.getColor());
            setColor4(MyColors.GREEN);
            setFrames(4);
        }
    }
}
