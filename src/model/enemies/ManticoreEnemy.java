package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.combat.StandardCombatLoot;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ManticoreEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new Sprite32x32("manticore", "enemies.png", 0x6B,
            MyColors.BLACK, MyColors.GOLD, MyColors.DARK_RED, MyColors.DARK_BROWN);

    public ManticoreEnemy(char a) {
        super(a, "Manticore", HOSTILE);
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
        return MyRandom.randInt(3, 5);
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }
}
