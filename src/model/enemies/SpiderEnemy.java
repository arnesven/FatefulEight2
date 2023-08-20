package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.combat.ParalysisCondition;
import model.combat.StandardCombatLoot;
import model.enemies.behaviors.ParalysisAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class SpiderEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new SpiderSprite();

    public SpiderEnemy(char a) {
        super(a, "Spider", NORMAL, new ParalysisAttackBehavior(1));
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

    private static class SpiderSprite extends LoopingSprite {
        public SpiderSprite() {
            super("spiderenemy", "enemies.png", 0x47, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_RED);
            setColor3(MyColors.RED);
            setColor4(MyColors.DARK_GRAY);
            setFrames(4);
        }
    }
}
