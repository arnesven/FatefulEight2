package model.states.events;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.Enemy;
import model.enemies.behaviors.PullForwardAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class TentacleEnemy extends Enemy {
    private static final Sprite SPRITE = new TentacleSprite();

    public TentacleEnemy(char a) {
        super(a, "Tentacle", new PullForwardAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 4;
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
        return 3;
    }

    @Override
    public String getDeathSound() {
        return null;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    private static class TentacleSprite extends LoopingSprite {
        public TentacleSprite() {
            super("tentacle", "enemies.png", 0x90, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BLUE);
            setColor3(MyColors.YELLOW);
            setColor4(MyColors.DARK_BLUE);
            setFrames(4);
        }
    }
}
