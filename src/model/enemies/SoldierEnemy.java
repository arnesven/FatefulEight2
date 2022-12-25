package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import view.MyColors;
import view.sprites.SoldierSprite;
import view.sprites.Sprite;

public class SoldierEnemy extends Enemy {
    private static final Sprite SPRITE = new SoldierSprite(MyColors.GRAY_RED);

    public SoldierEnemy(char a) {
        super(a, "Soldier");

    }
    @Override
    public int getMaxHP() {
        return 7;
    }

    @Override
    public int getSpeed() {
        return 4;
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
        return new StandardCombatLoot(model);
    }
}
