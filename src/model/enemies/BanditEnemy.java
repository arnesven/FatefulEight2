package model.enemies;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.races.Race;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.BanditSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class BanditEnemy extends HumanoidEnemy {

    private static final LoopingSprite humanSprite = new BanditSprite(Race.NORTHERN_HUMAN.getColor());
    private static final LoopingSprite halfOrcSprite = new BanditSprite(Race.HALF_ORC.getColor());
    private final Race race;
    private final int maxHp;

    public BanditEnemy(char enemyGroup, String name, int maxHp) {
        super(enemyGroup, name);
        this.race = MyRandom.randInt(2) == 1 ? Race.NORTHERN_HUMAN : Race.HALF_ORC;
        this.maxHp = maxHp;
        setCurrentHp(maxHp);
    }

    public BanditEnemy(char enemyGroup) {
        this(enemyGroup, "Bandit", 5);
    }

        @Override
    public int getMaxHP() {
        return maxHp;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        if (getRace() == Race.NORTHERN_HUMAN) {
            return humanSprite;
        } else {
            return halfOrcSprite;
        }
    }

    protected Race getRace() {
        return race;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        GameStatistics.incrementBanditsKilled();
    }
}
