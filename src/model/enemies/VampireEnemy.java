package model.enemies;

import model.GameStatistics;
import model.Model;
import model.achievements.AlucardAchievement;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.combat.loot.CombatLoot;
import model.combat.loot.StandardCombatLoot;
import model.items.Prevalence;
import model.items.spells.DarkShroudSpell;
import model.items.spells.DrainLifeSpell;
import model.items.spells.EnthrallSpell;
import model.items.spells.GazeOfDeathSpell;
import model.items.weapons.*;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class VampireEnemy extends UndeadEnemy {
    private static final LoopingSprite SPRITE = new VampireEnemySprite();

    public VampireEnemy(char a) {
        super(a, "Vampire", new VampireAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new VampireEnemyLoot(model);
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        GameStatistics.incrementVampiresKilled();
        if (killer.hasCondition(VampirismCondition.class)) {
            combatEvent.completeAchievement(AlucardAchievement.KEY);
        }
    }

    private static class VampireEnemySprite extends LoopingSprite {
        public VampireEnemySprite() {
            super("vampire", "enemies.png", 0x1C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(MyColors.WHITE);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }

    private static class VampireEnemyLoot extends StandardCombatLoot {
        public VampireEnemyLoot(Model model) {
            super(model);
            setGold(MyRandom.rollD10() + getGold());
            if (MyRandom.flipCoin()) {
                getItems().add(MyRandom.sample(List.of(new DrainLifeSpell(), new GazeOfDeathSpell(), new DarkShroudSpell(), new EnthrallSpell())));
            } else {
                List<Weapon> blades = List.of(new RitualDagger(), new Claymore(), new Sai(), new Sicle(),
                        new MorningStar(), new ShortSpear());
                Weapon blade = (Weapon)model.getItemDeck().draw(blades, 1, Prevalence.unspecified, 0.95).getFirst();
                getItems().add(blade);
            }
        }
    }
}
