package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.ParalysisAttackBehavior;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public abstract class SpiderEnemy extends BeastEnemy {

    public SpiderEnemy(char a, String name, int aggressiveness, EnemyAttackBehavior attackBehavior) {
        super(a, name, aggressiveness, attackBehavior);
    }

    public SpiderEnemy(char a) {
        this(a, "Spider", NORMAL, new ParalysisAttackBehavior(2));
    }

    @Override
    public int getMaxHP() {
        return 7;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    public abstract SpiderEnemy copy();

    protected static class SpiderSprite extends LoopingSprite {
        public SpiderSprite(MyColors contour, MyColors fill, MyColors eyes, MyColors body) {
            super("spiderenemy", "enemies.png", 0x47, 32);
            setColor1(contour);
            setColor2(fill);
            setColor3(eyes);
            setColor4(body);
            setFrames(4);
        }
    }
}
