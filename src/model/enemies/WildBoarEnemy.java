package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.RationsCombatLoot;
import view.sprites.BearSprite;
import view.sprites.Sprite;
import view.sprites.WildBoarSprite;

import java.awt.*;

public class WildBoarEnemy extends BigEnemy {
    private static Sprite sprite = new WildBoarSprite("wildboar", "enemies.png", 0x20);

    public WildBoarEnemy(char a) {
        super(a, "Wild Boar");
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
        return sprite;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(5);
    }
}
