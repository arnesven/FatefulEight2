package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.enemies.behaviors.ParalysisAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SnowyBeastEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new Sprite32x32("snowybeast", "enemies.png", 0x4F,
            MyColors.BLACK, MyColors.WHITE, MyColors.CYAN, MyColors.LIGHT_BLUE);

    public SnowyBeastEnemy(char enemyGroup) {
        super(enemyGroup, "Snowy Beast", RAMPAGING, new ParalysisAttackBehavior(2));
    }

    @Override
    public int getMaxHP() {
        return 14;
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
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}
