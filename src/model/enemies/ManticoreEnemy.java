package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.MagicMeleeAttackBehavior;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ManticoreEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new Sprite32x32("manticore", "enemies.png", 0x6B,
            MyColors.BLACK, MyColors.GOLD, MyColors.DARK_RED, MyColors.DARK_BROWN);

    public ManticoreEnemy(char a) {
        super(a, "Manticore", HOSTILE, new MagicMeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 10;
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
        return MyRandom.randInt(2, 4);
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}
