package model.enemies;

import model.Model;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class RedKnightEnemy extends Enemy {
    private static Sprite avatar = Classes.RED_KNIGHT.getAvatar(Race.NORTHERN_HUMAN, null);

    public RedKnightEnemy(char enemyGroup) {
        super(enemyGroup, "Red Knight");
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getDamageReduction() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
