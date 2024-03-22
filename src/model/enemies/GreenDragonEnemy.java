package model.enemies;

import model.classes.Skill;
import model.enemies.behaviors.MagicMeleeAttackBehavior;
import model.items.Equipment;
import model.items.weapons.MeleeDragonWeapon;
import view.MyColors;

public class GreenDragonEnemy extends DragonEnemy {
    public GreenDragonEnemy(char a) {
        super(a, "Green Dragon", 5, 4, new MagicMeleeAttackBehavior(),
                new MyColors[]{MyColors.DARK_GREEN, MyColors.GREEN, MyColors.LIGHT_GREEN, MyColors.GRAY});
        reduceAggressiveness();
    }

    @Override
    protected int getDragonMaxHp() {
        return 16;
    }

    @Override
    public Skill getMagicSkill() {
        return Skill.MagicGreen;
    }

    @Override
    public Equipment getTamedEquipment() {
        return new Equipment(new MeleeDragonWeapon());
    }
}
