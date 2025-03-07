package model.enemies;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.loot.DemonicLoot;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public abstract class AltarEnemy extends Enemy {

    public AltarEnemy(char a, String name) {
        super(a, name);
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new DemonicLoot(model);
    }

    protected static Sprite makeDemonSprite(String name, int num) {
        return new Sprite32x32(name, "enemies.png", num,
                MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.RED, MyColors.TAN);
    }

    @Override
    public String getDeathSound() {
        return "daemon_death";
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        GameStatistics.incrementDemonsKilled();
    }
}
