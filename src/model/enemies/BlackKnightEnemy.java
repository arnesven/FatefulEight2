package model.enemies;

import model.Model;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class BlackKnightEnemy extends Enemy {
    private static Sprite avatar = Classes.BKN.getAvatar(Race.SOUTHERN_HUMAN, null);

    public BlackKnightEnemy(char enemyGroup) {
        super(enemyGroup, "Black Knight");
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
