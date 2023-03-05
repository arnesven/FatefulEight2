package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.ParalysisCondition;
import model.combat.PoisonCondition;
import model.combat.StandardCombatLoot;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ScorpionEnemy extends Enemy {
    private static final Sprite SPRITE = new Sprite32x32("scorpion", "enemies.png", 0x6A,
            MyColors.BLACK, MyColors.GOLD, MyColors.BROWN, MyColors.RED);

    public ScorpionEnemy(char a) {
        super(a, "Scorpion");
    }

    @Override
    public void attack(Model model, GameCharacter target, CombatEvent combatEvent) {
        int hpBefore = target.getHP();
        super.attack(model, target, combatEvent);
        if (hpBefore > target.getHP()) {
            combatEvent.println(target.getName() + " has been poisoned!");
            target.addCondition(new PoisonCondition());
        }
    }

    @Override
    public int getMaxHP() {
        return 8;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
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
