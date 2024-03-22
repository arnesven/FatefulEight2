package model.enemies;

import model.classes.Skill;
import model.enemies.behaviors.MultiAttackBehavior;
import model.items.Equipment;
import model.items.weapons.MeleeDragonWeapon;
import view.MyColors;

public class BoneDragonEnemy extends DragonEnemy {
    public BoneDragonEnemy(char a) {
        super(a, "Bone Dragon", 1, 7, new MultiAttackBehavior(2),
                new MyColors[]{MyColors.DARK_GRAY, MyColors.WHITE, MyColors.GRAY, MyColors.BEIGE});
    }

    @Override
    protected int getDragonMaxHp() {
        return 14;
    }

    @Override
    public Skill getMagicSkill() {
        return Skill.MagicWhite;
    }

    @Override
    public Equipment getTamedEquipment() {
        return new Equipment(new MeleeDragonWeapon());
    }
}
