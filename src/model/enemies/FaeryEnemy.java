package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.MagicRangedAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class FaeryEnemy extends Enemy {
    private static final Sprite SPRITE = new Sprite32x32("faery", "enemies.png", 0x6D,
            MyColors.BLACK, MyColors.PINK, MyColors.LIGHT_BLUE, MyColors.BLUE);

    public FaeryEnemy(char a) {
        super(a, "Faery", new MagicRangedAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 7;
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

    @Override
    public String getDeathSound() {
        return "squeek";
    }
}
