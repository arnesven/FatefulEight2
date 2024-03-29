package model.enemies;

import model.Model;
import model.combat.loot.BossCombatLoot;
import model.combat.loot.CombatLoot;
import model.enemies.behaviors.MagicRangedAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class RedMageEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = new RedMageSprite();

    public RedMageEnemy(char a) {
        super(a, "Red Mage", new MagicRangedAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 7;
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
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }

    private static class RedMageSprite extends LoopingSprite {
        public RedMageSprite() {
            super("redmage", "enemies.png", 0x8C, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.RED);
            setColor3(Race.DARK_ELF.getColor());
        }
    }
}
