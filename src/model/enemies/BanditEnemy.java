package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import model.races.Race;
import util.MyRandom;
import view.sprites.BanditSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class BanditEnemy extends Enemy {

    private static final LoopingSprite humanSprite = new BanditSprite(Race.NORTHERN_HUMAN.getColor());
    private static final LoopingSprite halfOrcSprite = new BanditSprite(Race.HALF_ORC.getColor());
    private final Race race;

    public BanditEnemy(char enemyGroup) {
        super(enemyGroup, "Bandit");
        this.race = MyRandom.randInt(2) == 1 ? Race.NORTHERN_HUMAN : Race.HALF_ORC;
    }

    @Override
    public int getMaxHP() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        if (this.race == Race.NORTHERN_HUMAN) {
            return humanSprite;
        } else {
            return halfOrcSprite;
        }
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
