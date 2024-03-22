package model.enemies;

import model.classes.Skill;
import model.enemies.behaviors.MultiMagicAttackBehavior;
import model.items.Equipment;
import model.items.weapons.MeleeDragonWeapon;
import view.MyColors;

public class BlackDragonEnemy extends DragonEnemy {
    public BlackDragonEnemy(char a) {
        super(a, "Black Dragon", 4, 3, new MultiMagicAttackBehavior(2),
                new MyColors[]{MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.GRAY, MyColors.LIGHT_GRAY});
    }

    @Override
    protected int getDragonMaxHp() {
        return 22;
    }

    @Override
    public Skill getMagicSkill() {
        return Skill.MagicBlack;
    }

    @Override
    public Equipment getTamedEquipment() {
        return new Equipment(new MeleeDragonWeapon());
    }

}
