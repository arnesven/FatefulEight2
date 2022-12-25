package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.RationsCombatLoot;
import view.sprites.Sprite;
import view.sprites.ViperSprite;

public class ViperEnemy extends Enemy {
    private static Sprite sprite = new ViperSprite("viper", "enemies.png", 0x00);

    public ViperEnemy(char enemyGroup) {
        super(enemyGroup, "Viper");
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(1);
    }

}
