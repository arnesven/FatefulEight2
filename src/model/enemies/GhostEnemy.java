package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.MixedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class GhostEnemy extends UndeadEnemy {
    private static final Sprite SPRITE = new GhostSprite();

    public GhostEnemy(char a) {
        super(a, "Ghost", new MixedAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 10;
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
        return new MonsterCombatLoot(model);
    }

    private static class GhostSprite extends LoopingSprite {
        public GhostSprite() {
            super("ghost", "enemies.png", 0x7E, 32);
            setDelay(32);
            setFrames(2);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.CYAN);
            setColor4(MyColors.LIGHT_BLUE);
        }
    }
}
