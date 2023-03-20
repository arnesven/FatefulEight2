package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.combat.StandardCombatLoot;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SnowyBeastEnemy extends Enemy {
    private static final Sprite SPRITE = new Sprite32x32("snowybeast", "enemies.png", 0x4F,
            MyColors.BLACK, MyColors.WHITE, MyColors.CYAN, MyColors.LIGHT_BLUE);

    public SnowyBeastEnemy(char enemyGroup) {
        super(enemyGroup, "Snowy Beast");
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
