package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class FaeryEnemy extends Enemy {
    private static final Sprite SPRITE = new Sprite32x32("faery", "enemies.png", 0x6D,
            MyColors.BLACK, MyColors.PINK, MyColors.LIGHT_BLUE, MyColors.BLUE);

    public FaeryEnemy(char a) {
        super(a, "Faery");
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
    protected int getFightingStyle() {
        return FIGHTING_STYLE_RANGED;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}
