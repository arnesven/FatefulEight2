package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.SoldierSprite;
import view.sprites.Sprite;

public class RisenWarriorEnemy extends UndeadEnemy {

    private static final Sprite SPRITE = new SoldierSprite(MyColors.GOLD, Race.WITCH_KING.getColor());

    public RisenWarriorEnemy(char b) {
        super(b, "Risen Warrior", new MeleeAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 4;
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
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }
}
