package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.MonsterCombatLoot;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.ArrayList;

public class OctopusEnemy extends Enemy {


    private static final Sprite SPRITE = new OctopusSprite();

    public OctopusEnemy(char enemyGroup) {
        super(enemyGroup, "Octopus");
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        for (Enemy e : combatEvent.getEnemies()) {
            combatEvent.doDamageToEnemy(e, 99, killer);
        }
    }

    @Override
    public int getMaxHP() {
        return 26;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    protected int getHeight() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    private static class OctopusSprite extends LoopingSprite {
        public OctopusSprite() {
            super("dragon", "enemies.png", 0x50, 64, 64, new ArrayList<>());
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BLUE);
            setColor3(MyColors.YELLOW);
            setColor4(MyColors.DARK_BLUE);
            setFrames(3);
        }
    }
}