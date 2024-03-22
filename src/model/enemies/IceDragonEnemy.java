package model.enemies;

import model.classes.Skill;
import model.enemies.behaviors.MultiMagicRangedAttackBehavior;
import model.items.Equipment;
import model.items.weapons.RangedDragonWeapon;
import view.MyColors;

public class IceDragonEnemy extends DragonEnemy {
    public IceDragonEnemy(char a) {
        super(a, "Ice Dragon", 3, 4, new MultiMagicRangedAttackBehavior(2),
                new MyColors[]{MyColors.DARK_GRAY, MyColors.BLUE, MyColors.DARK_BLUE, MyColors.LIGHT_BLUE});
    }

    @Override
    protected int getDragonMaxHp() {
        return 18;
    }

    @Override
    public Skill getMagicSkill() {
        return Skill.MagicBlue;
    }

    @Override
    public Equipment getTamedEquipment() {
        return new Equipment(new RangedDragonWeapon(1));
    }
}
