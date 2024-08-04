package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.RationsCombatLoot;
import model.enemies.behaviors.PoisonAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.ViperSprite;

public class CobraEnemy extends BeastEnemy {
    private static Sprite SPRITE = new ViperSprite("cobra", "enemies.png", 0x00, MyColors.GOLD, MyColors.YELLOW);

    public CobraEnemy(char a) {
        super(a, "Cobra", HOSTILE, new PoisonAttackBehavior(2));
    }

    @Override
    public int getMaxHP() {
        return 10;
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
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new RationsCombatLoot(5);
    }
}
