package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.RandomMoneyCombatLoot;
import view.MyColors;
import view.sprites.SoldierSprite;
import view.sprites.Sprite;

public class SoldierLeaderEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = new SoldierSprite(MyColors.LIGHT_BLUE);

    public SoldierLeaderEnemy(char a) {
        super(a, "Officer");
    }

    @Override
    public int getMaxHP() {
        return 8;
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
        return new RandomMoneyCombatLoot(5);
    }
}
