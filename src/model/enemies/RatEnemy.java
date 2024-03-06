package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.enemies.behaviors.MeleeAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class RatEnemy extends BeastEnemy {
    public static final Sprite SPRITE = new Sprite32x32("rat", "enemies.png", 0x4D,
            MyColors.BLACK, MyColors.BROWN, MyColors.LIGHT_PINK, MyColors.BEIGE);

    public RatEnemy(char a) {
        super(a, "Rat", DOCILE, new MeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 2;
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
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
