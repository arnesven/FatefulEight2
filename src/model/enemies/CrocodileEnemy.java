package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.combat.StandardCombatLoot;
import model.enemies.behaviors.BleedAttackBehavior;
import model.enemies.behaviors.MeleeAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class CrocodileEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new Sprite32x32("crocodile", "enemies.png", 0x6C,
                                                   MyColors.BLACK, MyColors.WHITE, MyColors.GREEN, MyColors.DARK_GREEN);

    public CrocodileEnemy(char a) {
        super(a, "Crocodile", HOSTILE, new BleedAttackBehavior(1));
    }

    @Override
    public int getMaxHP() {
        return 14;
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
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}
