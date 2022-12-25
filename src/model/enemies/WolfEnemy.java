package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.RandomMoneyCombatLoot;
import model.combat.RationsCombatLoot;
import view.sprites.Sprite;
import view.sprites.WildBoarSprite;
import view.sprites.WolfSprite;

public class WolfEnemy extends BigEnemy {
    private static Sprite sprite = new WolfSprite("wolf", "enemies.png", 0x30);

    public WolfEnemy(char a) {
        super(a, "Wolf");
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 7;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(1);
    }

    @Override
    public int getWidth() {
        return 2;
    }
}
