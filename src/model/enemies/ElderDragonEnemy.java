package model.enemies;

import model.classes.Skill;
import model.enemies.behaviors.MultiKnockBackBehavior;
import model.items.Equipment;
import model.items.clothing.ChainMail;
import model.items.weapons.MeleeDragonWeapon;
import view.MyColors;

public class ElderDragonEnemy extends DragonEnemy {
    public ElderDragonEnemy(char a) {
        super(a, "Elder Dragon", 4, 3, new MultiKnockBackBehavior(7, 3),
                new MyColors[]{MyColors.DARK_BROWN, MyColors.GOLD, MyColors.YELLOW, MyColors.BEIGE});
    }

    @Override
    protected int getDragonMaxHp() {
        return 20;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public Skill getMagicSkill() {
        return Skill.MagicAny;
    }

    @Override
    public Equipment getTamedEquipment() {
        return new Equipment(new MeleeDragonWeapon(), new ChainMail(), null);
    }
}
