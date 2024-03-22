package model.enemies;

import model.classes.Skill;
import model.enemies.behaviors.MultiMagicRangedAttackBehavior;
import model.items.Equipment;
import model.items.weapons.RangedDragonWeapon;
import view.MyColors;

public class RedDragonEnemy extends DragonEnemy {
    public RedDragonEnemy(char a) {
        super(a, "Red Dragon", 6, 3, new MultiMagicRangedAttackBehavior(4),
                new MyColors[]{MyColors.DARK_BROWN, MyColors.RED, MyColors.DARK_RED, MyColors.GOLD});
    }

    @Override
    protected int getDragonMaxHp() {
        return 20;
    }

    @Override
    public Skill getMagicSkill() {
        return Skill.MagicRed;
    }

    @Override
    public Equipment getTamedEquipment() {
        return new Equipment(new RangedDragonWeapon(2));
    }
}
