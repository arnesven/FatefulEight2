package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.MeleeAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class GiantRatEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new Sprite32x32("giantrat", "enemies.png", 0x4E,
            MyColors.BLACK, MyColors.BROWN, MyColors.LIGHT_PINK, MyColors.BEIGE);

    public GiantRatEnemy(char a) {
        super(a, "Giant Rat", NORMAL, new MeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 4;
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
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}
